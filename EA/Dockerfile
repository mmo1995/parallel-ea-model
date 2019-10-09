FROM openjdk:10
ADD ./target/EA-1.0-SNAPSHOT.jar /app.jar
ADD ./gleam/gleamParPopCLV /gleam/gleamParPopCLV
ENTRYPOINT ["java","-jar", "/app.jar"]