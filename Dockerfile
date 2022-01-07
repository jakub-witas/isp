FROM openjdk:15-jdk-alpine
COPY /out/artifacts/database/database.jar database.jar
CMD ["java","-jar","database.jar","develop"]