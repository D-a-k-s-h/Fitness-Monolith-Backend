#FROM ubuntu:latest
#LABEL authors="daksh"
#
#ENTRYPOINT ["top", "-b"]

#Container
FROM eclipse-temurin:26-jre

#Inside container /app should be a place where our application should run
WORKDIR /app

#Copy the .jar file present in /target and make it app.jar in container
COPY target/*.jar app.jar

#Endpoint in which our application runs
EXPOSE 8080

#Run these [] commands to start the application
ENTRYPOINT ["java", "-jar", "app.jar"]