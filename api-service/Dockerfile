FROM eclipse-temurin:17

WORKDIR /wd

COPY .. /wd

EXPOSE 8080

RUN chmod +x api-service/start.sh
RUN ./gradlew api-service:getDeps

CMD ["sh", "api-service/start.sh"]