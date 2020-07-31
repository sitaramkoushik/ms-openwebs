FROM openjdk:8-jdk-alpine

VOLUME /tmp

ARG JAR_FILE=target/openwebs.jar

COPY ${JAR_FILE} openwebs.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dserver.port=8091 -Dserver.env=${CLUSTER_ENV}  -Djava.security.egd=file:/dev/./urandom -jar /openwebs.jar"]