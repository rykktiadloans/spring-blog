FROM eclipse-temurin:17

WORKDIR /wd

COPY .. /wd

EXPOSE 8080

RUN chmod +x gateway/start.sh
RUN ./gradlew gateway:getDeps

CMD ["sh", "gateway/start.sh"]