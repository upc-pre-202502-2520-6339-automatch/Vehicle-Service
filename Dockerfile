FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
ARG JAR_FILE=target/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080 8081
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
