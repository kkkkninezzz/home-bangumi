@echo off
java --enable-preview -agentlib:native-image-agent=config-output-dir=./home-bangumi-web/src/main/resources/META-INF/native-image  -jar ./out/home-bangumi-web-0.0.1-SNAPSHOT.jar
