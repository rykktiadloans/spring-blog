FROM eclipse-temurin:17

WORKDIR /wd

COPY .. /wd

EXPOSE 8080

RUN chmod +x frontend-service/start.sh
RUN ./gradlew frontend-service:getDeps

CMD ["sh", "frontend-service/start.sh"]