FROM openjdk:10
ADD ./target/EAHybrid-1.0-SNAPSHOT.jar /app.jar
ADD ./gleam-esso/testfeld/island/ /gleam-esso/testfeld/island/
ADD ./gleam-esso/gleamParPopEssO_CLV /gleam-esso/gleamParPopEssO_CLV
ENTRYPOINT ["java","-jar", "/app.jar"]
