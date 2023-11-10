#!/bin/bash

IMAGE_NAME=$(basename $(pwd '{}'))

if [[ -z "$1" ]]; then
   #this value should comes from docker login
   AWS_ACCOUNT_ID=136531923082
else
   AWS_ACCOUNT_ID=$1
fi

if [[ -z "$2" ]]; then
   AWS_DEFAULT_REGION=us-west-2
else
   AWS_DEFAULT_REGION=$2
fi


echo "Creating image ${IMAGE_NAME}:latest"

docker build -t ${IMAGE_NAME}:latest -f Dockerfile . --build-arg AWS_ACCOUNT_ID=$AWS_ACCOUNT_ID --build-arg AWS_DEFAULT_REGION=$AWS_DEFAULT_REGION

src/docker/scripts/./run-container-dependencies.sh

src/docker/scripts/./run-container.sh src/docker/resources/${IMAGE_NAME}-docker-config.json
