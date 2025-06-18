FROM eclipse-temurin:17

WORKDIR /blog

COPY . /blog

EXPOSE 8080

RUN chmod +x start.sh
RUN ./gradlew getDeps

CMD ["sh", "start.sh"]