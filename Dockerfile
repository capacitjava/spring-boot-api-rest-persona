FROM openjdk:21
EXPOSE 7575
ADD target/spring-boot-api-rest-persona.jar spring-boot-api-rest-persona.jar
ENTRYPOINT ["java","-jar","/spring-boot-api-rest-persona.jar"]