#!/usr/bin/env sh

cd ../../../..
docker build -t ecsd-tech-test .
docker run -d -p 3000:3000 ecsd-tech-test:latest &
CONTAINER=$(docker ps -lq)

cd src/test/e2e/ecs-e2e-challenge
./gradlew clean build cucumber

docker stop $CONTAINER