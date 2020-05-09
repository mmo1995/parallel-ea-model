
import logging
import sys

import redis
from flask import Flask
from flask import request
#from kubernetes import client, config
#from kubernetes.client.rest import ApiException

logging.basicConfig(
    level=logging.INFO,
    stream=sys.stdout,
    format='%(asctime)s.%(msecs)03d %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)


app = Flask(__name__)
r = redis.StrictRedis(host='localhost', port=6379)

number_of_islands_key = 'proof.island.amount'
initialize_islands_channel = 'proof.management.initialize.islands'

JOB_NAME = "island-job"
NAMESPACE = "energylab"


@app.route('/com/create/islands', methods=['POST'])
def create_islands():
    number_of_islands = request.json
    r.set(number_of_islands_key, number_of_islands)
    r.publish(initialize_islands_channel, 1)
    logging.info("Pod created. status='%s'" % str(number_of_islands))
    #Uncomment the following lines to execute this service in a cluster to dynamically create Pods for the
    #islands. Names and configurations have also to be adjusted to dynamically create Pods for the
    #Migration & Synchronization Service.

    #config.load_incluster_config()
    #core_v1_api = client.CoreV1Api()
    #for island_number in range(1, int(number_of_islands) + 1):
    #    pod = create_pod_object(island_number)
    #    create_pod(core_v1_api, pod, island_number)
    return "ok"

@app.route('/com/create/slaves', methods=['POST'])
def create_calculations():
    number_of_slaves = request.json
    logging.info("Pod created. status='%s'" % str(number_of_slaves))
    #Uncomment the following lines to execute this service in a cluster to dynamically create Pods for the
    #islands. Names and configurations have also to be adjusted to dynamically create Pods for the
    #Migration & Synchronization Service.
    #config.load_incluster_config()
    #core_v1_api = client.CoreV1Api()
    #for island_number in range(1, int(number_of_islands) + 1):
    #    pod = create_pod_object(island_number)
    #    create_pod(core_v1_api, pod, island_number)
    return "ok"



# def create_pod_object(island_number):
    # Configurate Pod container
    # container = client.V1Container(
    #    name="island",
    #    image="docker-energylab.iai-artifactory.iai.kit.edu/opt-framework/island:latest",
    #    image_pull_policy="Always",
    #    args=["--island.number="+str(island_number)])
    # Create the specification of pod
    # v1localObjectReference = client.V1LocalObjectReference(name="myregistrykey")
    # spec = client.V1PodSpec(containers=[container],
    #                         image_pull_secrets=[v1localObjectReference],
    #                        restart_policy="Never",
    #                        service_account_name="pods-creator")
    # Instantiate the pod object
    # pod = client.V1Pod(
    #     api_version="v1",
    #     kind="Pod",
    #    metadata=client.V1ObjectMeta(name=JOB_NAME + str(island_number), namespace=NAMESPACE),
    #    spec=spec)
    # return pod


# def create_pod(api_instance, pod, island_number):
    # Create pod
    # include_uninitialized = True  # bool | If true, partially initialized resources are included in the response. (optional)
    # try:
    #     api_response = api_instance.create_namespaced_pod(
    #         body=pod,
    #         namespace=NAMESPACE,
    #         include_uninitialized=include_uninitialized)
    #    logging.info("Pod created. status='%s'" % str(api_response.status))
    # except ApiException as e:
    #     if e.reason == "Conflict":
    #        logging.info("Pod already exists, sending initializing signal")
    #        r.publish(initialize_islands_channel, island_number)
    #    else:
    #        logging.error(e)


if __name__ == '__main__':
    app.run(host="0.0.0.0", port="8073", threaded=True, debug=True, use_reloader=False)

