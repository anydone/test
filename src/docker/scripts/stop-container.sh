#!/bin/bash

CONFIG_FILE=$1

if [[ -z "$CONFIG_FILE" ]]; then
   echo "Config file is required."
   exit 0;
fi

CONFIG_FILE_JSON=$(cat $1 | jq .)
IMAGE_NAME=$(echo "$CONFIG_FILE_JSON" | jq ".Image" | sed 's/"//g' | awk -F: '{print $1}' )

CONTAINER_NAME=$(echo "$IMAGE_NAME" | sed 's/oyster-//g' | awk -F: '{print $1}')
CONTAINER_NAME="oyster-$CONTAINER_NAME"

echo "Looking $CONTAINER_NAME into container list"

LOCAL_DOCKER_CONTAINER=$(curl -s -G --unix-socket /var/run/docker.sock -X GET http:/v1.40/containers/json?all=true --data-urlencode  "filters={\"name\":[\"${CONTAINER_NAME}\"]}" | jq .)

echo "container found:"
echo "$LOCAL_DOCKER_CONTAINER"

DOCKER_CONTAINER_ID=$(echo "$LOCAL_DOCKER_CONTAINER" | jq  .[0].Id | sed 's/"//g' )

if [[ ! -z "$DOCKER_CONTAINER_ID"  ]]; then
   echo "stopping container $CONTAINER_NAME with id $DOCKER_CONTAINER_ID"
   curl -s --unix-socket /var/run/docker.sock -X POST http:/v1.40/containers/$DOCKER_CONTAINER_ID/stop
   echo "removing container $CONTAINER_NAME with id $DOCKER_CONTAINER_ID"
   curl -s --unix-socket /var/run/docker.sock -X DELETE http:/v1.40/containers/$DOCKER_CONTAINER_ID
fi
