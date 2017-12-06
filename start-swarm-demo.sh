#!/usr/bin/env bash

eval $(docker-machine env manager1)

mvn clean install -DskipTests=true

docker tag ascent/ascent-demo-service:latest localhost:5000/ascent/ascent-demo-service:latest
docker push localhost:5000/ascent/ascent-demo-service:latest

docker tag ascent/ascent-document-service:latest localhost:5000/ascent/ascent-document-service:latest
docker push localhost:5000/ascent/ascent-document-service:latest

docker stack deploy --compose-file docker-compose.swarm.yml ascent-sample
