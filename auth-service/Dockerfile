FROM eclipse-temurin:17

WORKDIR /wd

COPY .. /wd

EXPOSE 8080

RUN chmod +x auth-service/start.sh
RUN ./gradlew auth-service:getDeps

CMD ["sh", "auth-service/start.sh"]