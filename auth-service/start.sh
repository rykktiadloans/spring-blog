(./gradlew -t --continuous :auth-service:bootJar) &
./gradlew auth-service:bootRun -PskipDownload=true
