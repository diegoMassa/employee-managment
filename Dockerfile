FROM openjdk:11
RUN apt-get update && apt-get install bash
EXPOSE 8080
ADD target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]