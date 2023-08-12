# Build Stage
FROM openjdk:17-jdk-slim AS BUILDER

ENV APP_HOME=/app

WORKDIR $APP_HOME

COPY build.gradle settings.gradle gradlew ${APP_HOME}/

COPY gradle $APP_HOME/gradle

RUN ./gradlew build || return 0 

COPY . .

RUN ./gradlew build


# Package stage
FROM openjdk:17-oracle

VOLUME /tmp

ARG JAR_FILE

ENV APP_HOME=/app

WORKDIR ${APP_HOME}

COPY --from=BUILDER ${APP_HOME}/build/libs/${JAR_FILE} app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --retries=5 CMD curl -f http://localhost:8080/actuator/health 2>&1 || exit 1

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
