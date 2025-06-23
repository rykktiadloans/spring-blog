(./gradlew -t --continuous :gateway:bootJar) &
./gradlew gateway:bootRun -PskipDownload=true
