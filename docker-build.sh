#!/usr/bin/env bash

COMPANY=forcelate
APP=infrastructure-server
TAG=dev

mvn clean install
docker build -t ${COMPANY}/${APP}:${TAG} .
