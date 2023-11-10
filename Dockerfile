ARG PROJECT_ID
FROM gcr.io/$PROJECT_ID/alpine-java11:latest AS build
ARG GITHUB_TOKEN
ENV GIT_TOKEN $GITHUB_TOKEN
LABEL maintainer = "prabin.shrestha@treeleaf.ai"
LABEL description="java service boilerplate"
LABEL vendor="Treeleaf Technologies"


WORKDIR /app
COPY . /app/

RUN java --version
RUN ./gradlew bootjar

FROM gcr.io/$PROJECT_ID/alpine-java11:latest
WORKDIR /app

COPY --from=build /app/build/libs/java.service-boilerplate*.jar /app/

RUN find /app -iname java.service-boilerplate-\* -exec ln -sf '{}' /app/java.service-boilerplate.jar \;

# COPY src/docker/resources/application-docker.properties /app/conf/application.properties
# COPY src/docker/resources/logback-docker.xml /app/conf/logback.xml

RUN ls -l /app

ENV JAVA_OPTS "-Xms512m -Xmx512m"
ENV APP_ARGS "-Dspring.config.location=/app/conf/application.properties "

HEALTHCHECK CMD nc -z localhost 9090

EXPOSE 9090

CMD java $JAVA_OPTS $APP_ARGS -jar /app/java.service-boilerplate.jar