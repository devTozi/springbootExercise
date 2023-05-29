FROM adoptopenjdk/openjdk11

LABEL create=coding404

ARG jarfile=build/libs/BootMyweb-0.0.1-SNAPSHOT.war

COPY ${jarfile} /app.war

CMD ["java", "-jar", "/app.war"]