FROM openjdk:10
ADD ./target/starter-0.0.1-SNAPSHOT.jar /app.jar
ADD taskID.txt /taskID.txt
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar", "/app.jar"]
