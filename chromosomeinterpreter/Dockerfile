FROM openjdk:10
ADD ./target/chromosomeinterpreter-0.0.1-SNAPSHOT.jar /app.jar
ADD ./files/ /files/

ENTRYPOINT ["java","-jar", "/app.jar"]