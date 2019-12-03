FROM openjdk:10
ADD ./target/initializerEA-1.0-SNAPSHOT.jar /app.jar
ADD ./gleam/testfeld/initializer/ /gleam/testfeld/initializer/
ADD ./gleam/gleamParPopEssO_CLV /gleam/gleamParPopEssO_CLV
ENTRYPOINT ["java","-jar", "/app.jar"]
