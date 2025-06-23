(./gradlew -t --continuous :api-service:bootJar) &
./gradlew api-service:bootRun -PskipDownload=true
