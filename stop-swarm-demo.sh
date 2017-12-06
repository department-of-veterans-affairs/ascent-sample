#!/usr/bin/env bash

eval $(docker-machine env manager1)

docker stack rm ascent-sample
