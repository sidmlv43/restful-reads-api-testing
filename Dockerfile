FROM maven:3.9.11-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
COPY suites ./suites

RUN mvn dependency:go-offline

COPY src ./src

ENV SUITE=smoke
ENV ENVIRONMENT=uat
ENV THREAD_COUNT=5

CMD sh -c "mvn test -P${SUITE},${ENVIRONMENT} -Dthread-count=${THREAD_COUNT}"