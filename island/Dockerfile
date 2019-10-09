FROM openjdk:10
ADD ./target/migrationSynchronization-0.1.jar /app.jar
ADD ./gleam/gleamParPopCLV /gleam/gleamParPopCLV
ENTRYPOINT ["java","-jar", "/app.jar"]