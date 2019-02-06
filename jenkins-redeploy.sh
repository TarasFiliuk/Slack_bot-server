#!/usr/bin/env bash

COMPANY=forcelate
APP=infrastructure-server
TAG=latest
SPRING_BOOT_PROFILE=prod

docker stop ${APP}

docker pull ${COMPANY}/${APP}:${TAG}

docker run -d -it -p 8484:8484 \
  -e SPRING_BOOT_PROFILE=${SPRING_BOOT_PROFILE} \
  --rm --name ${APP} ${COMPANY}/${APP}:${TAG}
