#!/usr/bin/env sh

cd ../../..
docker build -t ecsd-tech-test .
docker run -d -p 3000:3000 ecsd-tech-test:latest &
CONTAINER=$(docker ps -lq)

cd src/test/e2e
PYTHON=$(command -v python3)
$PYTHON -m pip install -r requirements.txt
pytest -v
docker stop $CONTAINER