FROM openjdk:11
WORKDIR /usr/app
COPY drones-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/drones-0.0.1-SNAPSHOT.jar"]
