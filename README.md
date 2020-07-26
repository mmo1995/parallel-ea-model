# EA Parallelization Framework

This framework serves the parallelization of Evolutionary Algorithms on a cluster.
Parallelization Models implemented till now:
1. Coarse-Grained Parallelization Model (Island Model)
2. Global Model (Master-Slave Model)
3. Coarse-Grained Global Hybrid Model (Island - Master-Slave Hybrid Model)

## Getting Started

This framework runs on the cluster using Kubernetes orchestration and Container-Virtualization (Docker).

### Prerequisites

* You need to have a kubernetes cluster and the kubectl command tool installed on your local machine.
* linux OS
* Docker installed
* Java 8 or higher
* Spring Boot
* Redis local / on cluster 
* IntelliJ or Eclipse
* Maven

### Usage
1. Upload the Docker images to the cloud using Dockerfile
2. Deploy the Microservices using the .yaml files in [kubernetes folder](./kubernetes)
3. Open [frontend](./frontend/view/home.html) in browser and submit job to be optimized



## Built With

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/) - Dependency Management
* [Redis](https://redis.io/) - Used to implement the publish/subscribe messaging pattern
* [Docker](https://www.docker.com/) - Container Virtualization
* [Kubernetes](https://kubernetes.io) - Used to implement Microservices Architecture on Cluster

## References

Framework Papers:
* Khalloof, H., Mohammad, M., Jakob, W., Shahoud, S., Duepmeier, C., Hagenmeyer, V. A Generic and Scalable Solution for Hierarchical Parallelization of Population-Based Metaheuristics: A Microservices and Container Virtualization Approach
* Khalloof, H., Jakob, W., Liu, J., Braun, E., Shahoud, S., Duepmeier, C., Hagenmeyer, V.: A Generic Distributed Microservices and Container based Framework for Metaheuristic Optimization
*  Khalloof, H., Ostheimer, P., Jakob, W., Shahoud, S., Duepmeier, C., Hagenmeyer, V.: A Distributed Modular Scalable and Generic Framework for Parallelizing Population-Based Metaheuristics.

Contact: [Hatem Khalloof](mailto:hatem.khalloof@kit.edu)

## Authors

* **Hatem Khalloof**

* **Phil Ostheimer**

* **Mohammad Mohammad** 
