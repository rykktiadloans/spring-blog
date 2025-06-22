(./gradlew -t :frontend-service:bootJar) &
./gradlew frontend:bootRun -PskipDownload=true
