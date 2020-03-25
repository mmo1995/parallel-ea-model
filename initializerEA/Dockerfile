FROM ubuntu:18.04
FROM openjdk:10

RUN apt-get update -y
RUN wget http://de.archive.ubuntu.com/ubuntu/pool/main/c/curl/libcurl3_7.47.0-1ubuntu2.14_amd64.deb
RUN wget http://archive.ubuntu.com/ubuntu/pool/main/o/openssl/libssl1.0.0_1.0.2g-1ubuntu4.15_amd64.deb
RUN apt-get install -f ./libssl1.0.0_1.0.2g-1ubuntu4.15_amd64.deb
RUN apt-get install -f ./libcurl3_7.47.0-1ubuntu2.14_amd64.deb -y

ADD ./target/initializerEA-1.0-SNAPSHOT.jar /app.jar
ADD ./gleam/testfeld/initializer/ /gleam/testfeld/initializer/
ADD ./gleam/gleamParPopEssO_CLV /gleam/gleamParPopEssO_CLV
ENTRYPOINT ["java","-jar", "/app.jar"]
