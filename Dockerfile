FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/model-quotes*.jar model-quotes.jar
CMD java ${JAVA_OPTS} -jar model-quotes.jar