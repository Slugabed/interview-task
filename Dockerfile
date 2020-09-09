FROM adoptopenjdk/openjdk11:jre-11.0.8_10
VOLUME /tmp
ARG JAR_FILE='build/libs/*.jar'
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]