FROM amd64/amazoncorretto:17
WORKDIR /app
COPY ./build/libs/server-0.0.1-SNAPSHOT.jar /app/GUDOK-ME.jar
CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=dev", "GUDOK-ME.jar"]