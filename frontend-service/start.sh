(./gradlew -t --continuous :frontend-service:bootJar) &
./gradlew frontend-service:bootRun -PskipDownload=true
