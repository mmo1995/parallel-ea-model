import logging
import sys
import time
import redis
from flask import Flask
from flask import request
from kubernetes import client, config
from kubernetes.client.rest import ApiException

logging.basicConfig(
    level=logging.INFO,
    stream=sys.stdout,
    format='%(asctime)s.%(msecs)03d %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)


app = Flask(__name__)
r = redis.StrictRedis(host='redis', port=6379)

number_of_island_key = 'proof.island.amount' # number of EAhybrid and Mi.Sy.
number_of_slaves_key = 'proof.slaves.amount' # number of interpreter and calculation

initialize_interpreter_channel = 'proof.management.initialize.interpreter' # chromosomeinterpreter
initialize_calculation_channel = 'proof.management.initialize.calculation' # calculation service
initialize_islands_channel = 'proof.management.initialize.islands'

JOB_NAME_EAHybrid = "ea-master-hybrid"
JOB_NAME_migration = "migration-hybrid"
JOB_NAME_interpreter = "chromosomeinterpreter-hybrid"
JOB_NAME_calculation = "calculation-hybrid"
NAMESPACE = "energylab"

########################################################################
########################################################################
@app.route('/com/create/islands', methods=['POST'])
def create_islands():
    number_of_islands = request.json
    r.set(number_of_island_key, number_of_islands)
    config.load_incluster_config()
    core_v1_api = client.CoreV1Api()
    for EA_number in range(1, int(number_of_islands) + 1):
        pod_EA = create_pod_object_EA (EA_number)
        create_pod_EA(core_v1_api, pod_EA, EA_number)
    time.sleep(25)
    for migration_number in range(1, int(number_of_islands) + 1):
        pod_migration = create_pod_object_migration (migration_number)
        create_pod_migration(core_v1_api, pod_migration, migration_number)
    time.sleep(25)
    return 'ok'
#################################
def create_pod_object_EA (EA_number):
    # Configurate Pod container
    container = client.V1Container(
        name="ea-master-hybrid",
        image="docker-energylab.iai-artifactory.iai.kit.edu/opt-framework-scheduling-hybrid/ea-master-hybrid:latest",
        image_pull_policy="Always",
        args=["--island.number="+str(EA_number)])
    # Create the specification of pod
    #v1localObjectReference = client.V1LocalObjectReference(name="myregistrykey")
    spec = client.V1PodSpec(containers=[container],
                            restart_policy="Always")
    # Instantiate the pod object
    pod_EA = client.V1Pod(
        api_version="v1",
        kind="Pod",
        metadata=client.V1ObjectMeta(name=JOB_NAME_EAHybrid + str(EA_number), namespace=NAMESPACE, labels={"k8s-app":JOB_NAME_EAHybrid+str(EA_number)}),
        spec=spec)
    return pod_EA

def create_pod_object_migration(migration_number):
    # Configurate Pod container
    container = client.V1Container(
        name="migration-hybrid",
        image="docker-energylab.iai-artifactory.iai.kit.edu/opt-framework-scheduling-hybrid/migration-hybrid:latest",
        image_pull_policy="Always",
        args=["--island.number="+str(migration_number)])
    # Create the specification of pod
    #v1localObjectReference = client.V1LocalObjectReference(name="myregistrykey")
    spec = client.V1PodSpec(containers=[container],
                            restart_policy="Always")
    # Instantiate the pod object
    pod_migration = client.V1Pod(
        api_version="v1",
        kind="Pod",
        metadata=client.V1ObjectMeta(name=JOB_NAME_migration + str(migration_number), namespace=NAMESPACE),
        spec=spec)
    return pod_migration
#################################
def create_pod_EA(api_instance, pod, EA_number):
    # Create pod
    include_uninitialized = True  # bool | If true, partially initialized resources are included in the response. (optional)
    try:
        api_response = api_instance.create_namespaced_pod(
            body=pod,
            namespace=NAMESPACE,
            include_uninitialized=include_uninitialized)
        logging.info("Pod created. status='%s'" % str(api_response.status))
    except ApiException as e:
        if e.reason == "Conflict":
            logging.info("EA  Service already exists, sending initializing signal")
            r.publish(initialize_islands_channel, EA_number)
        else:
            logging.error(e)
    service = client.V1Service()
    service.api_version = "v1"
    service.kind = "Service"
    service.metadata = client.V1ObjectMeta(name=JOB_NAME_EAHybrid + str(EA_number), namespace=NAMESPACE)
    spec = client.V1ServiceSpec()
    spec.ports = [client.V1ServicePort(protocol="TCP", port=8090, target_port=8090)]
    spec.selector = {"k8s-app":JOB_NAME_EAHybrid+str(EA_number)}
    service.spec = spec
    try:
        api_response1 = api_instance.create_namespaced_service(
            body=service,
            namespace=NAMESPACE)
        logging.info("Service created. status='%s'" % str(api_response1.status))
    except ApiException as e:
        if e.reason == "Conflict":
            logging.info("The Service of ea-master-hybrid Service already exists, sending initializing signal")
        else:
            logging.error(e)
#--------------------------------------#
def create_pod_migration(api_instance, pod, migration_number):
    # Create pod
    include_uninitialized = True  # bool | If true, partially initialized resources are included in the response. (optional)
    try:
        api_response = api_instance.create_namespaced_pod(
            body=pod,
            namespace=NAMESPACE,
            include_uninitialized=include_uninitialized)
        logging.info("Pod created. status='%s'" % str(api_response.status))
    except ApiException as e:
        if e.reason == "Conflict":
            logging.info("Migration Service already exists, sending initializing signal")
            r.publish(initialize_islands_channel, migration_number)
        else:
            logging.error(e)
########################################################################
########################################################################
@app.route('/com/create/slaves', methods=['POST'])
def create_slaves():
    number_of_slaves = request.json
    number_of_islands = r.get(number_of_island_key)
    r.set(number_of_slaves_key, number_of_slaves)
    config.load_incluster_config()
    core_v1_api = client.CoreV1Api()
    for island_number in range(1, int(number_of_islands) + 1):
        for interpreter_number in range(1, int(number_of_slaves) + 1):
            pod_interpreter = create_pod_object_interpreter(interpreter_number, island_number)
            create_pod_interpreter(core_v1_api, pod_interpreter, interpreter_number)
    time.sleep(25)
    for island_number in range(1, int(number_of_islands) + 1):
        for calculation_number in range(1, int(number_of_slaves) + 1):
            pod_calculation=create_pod_object_calculation(calculation_number, island_number)
            create_pod_calculation(core_v1_api, pod_calculation, calculation_number)
    time.sleep(25)
    return 'ok'
#################################
def create_pod_object_interpreter(interpreter_number, island_number):
    # Configurate Pod container
    container = client.V1Container(
        name="chromosomeinterpreter-hybrid",
        image="docker-energylab.iai-artifactory.iai.kit.edu/opt-framework-scheduling-hybrid/chromosomeinterpreter-hybrid:latest",
        image_pull_policy="Always",
        args=["--island.number="+str(island_number), "--slave.number="+str(interpreter_number)])
    # Create the specification of pod
    #v1localObjectReference = client.V1LocalObjectReference(name="myregistrykey")
    spec = client.V1PodSpec(containers=[container],
                            restart_policy="Always")
    # Instantiate the pod object
    pod = client.V1Pod(
        api_version="v1",
        kind="Pod",
        metadata=client.V1ObjectMeta(name=JOB_NAME_interpreter+str(island_number)+'.'+str(interpreter_number), namespace=NAMESPACE),
        spec=spec)
    return pod

def create_pod_object_calculation(calculation_number, island_number):
    # Configurate Pod container
    container = client.V1Container(
        name="calculation-hybrid",
        image="docker-energylab.iai-artifactory.iai.kit.edu/opt-framework-scheduling-hybrid/calculation-hybrid:latest",
        image_pull_policy="Always",
        args=["--island.number="+str(island_number), "--slave.number="+str(calculation_number)])
    # Create the specification of pod
    #v1localObjectReference = client.V1LocalObjectReference(name="myregistrykey")
    spec = client.V1PodSpec(containers=[container],
                            restart_policy="Always")
    # Instantiate the pod object
    pod_calculation = client.V1Pod(
        api_version="v1",
        kind="Pod",
        metadata=client.V1ObjectMeta(name=JOB_NAME_calculation +str(island_number)+'.'+ str(calculation_number), namespace=NAMESPACE),
        spec=spec)
    return pod_calculation
#################################
def create_pod_calculation(api_instance, pod, calculation_number):
    # Create pod
    include_uninitialized = True  # bool | If true, partially initialized resources are included in the response. (optional)
    try:
        api_response = api_instance.create_namespaced_pod(
            body=pod,
            namespace=NAMESPACE,
            include_uninitialized=include_uninitialized)
        logging.info("Pod created. status='%s'" % str(api_response.status))
    except ApiException as e:
        if e.reason == "Conflict":
            logging.info("Calculation Service already exists, sending initializing signal")
            r.publish(initialize_calculation_channel, calculation_number)
        else:
            logging.error(e)

def create_pod_interpreter(api_instance, pod, interpreter_number):
    # Create pod
    include_uninitialized = True  # bool | If true, partially initialized resources are included in the response. (optional)
    try:
        api_response = api_instance.create_namespaced_pod(
            body=pod,
            namespace=NAMESPACE,
            include_uninitialized=include_uninitialized)
        logging.info("Pod created. status='%s'" % str(api_response.status))
    except ApiException as e:
        if e.reason == "Conflict":
            logging.info("Chromosomeinterpreter already exists, sending initializing signal")
            r.publish(initialize_interpreter_channel, interpreter_number)
        else:
            logging.error(e)
########################################################################

if __name__ == '__main__':
    logging.info("sf")
    app.run(host="0.0.0.0", port="8073", threaded=True, debug=True, use_reloader=False)
