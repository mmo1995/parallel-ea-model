FROM java:8
VOLUME /tmp
ADD target/splittingJoining-0.0.1-SNAPSHOT.jar app.jar
ADD taskID.txt /taskID.txt
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Xmx1024m", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
