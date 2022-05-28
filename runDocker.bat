gradlew bootJar -Pprod jibDockerBuild && docker-compose -f src/main/docker/app.yml up -d
