#!/bin/bash

CONFIG_FILE=$1

if [[ -z "$CONFIG_FILE" ]]; then
   echo "Config file is required."
   exit 0;
fi

CONFIG_FILE_JSON=$(cat $1 | jq .)
ACTUAL_PATH=$(pwd)
CONFIG_FILE_JSON=$(echo "${CONFIG_FILE_JSON/\$\{pwd\}/$ACTUAL_PATH}")

FULL_IMAGE_NAME=$(echo "$CONFIG_FILE_JSON" | jq ".Image" | sed 's/"//g')
IMAGE_NAME=$(echo "$CONFIG_FILE_JSON" | jq ".Image" | sed 's/[\/"]//g' |  awk -F: '{print $1}' )
IMAGE_TAG=$(echo "$CONFIG_FILE_JSON" | jq ".Image" | sed 's/[\/"]//g' | awk -F: '{print $2}' )

CONTAINER_NAME=$(echo "$IMAGE_NAME" | sed 's/oyster-//g' | awk -F: '{print $1}')
CONTAINER_NAME="oyster-$CONTAINER_NAME"

echo "Processing docker image $FULL_IMAGE_NAME"

echo "Looking into local images"

LOCAL_DOCKER_IMAGE=$(curl -s --unix-socket /var/run/docker.sock -X GET http:/v1.40/images/json | jq --arg dbn "$FULL_IMAGE_NAME"  '.[]  | select(.RepoTags | index($dbn))' )

if [[ -z "$LOCAL_DOCKER_IMAGE" ]]; then 
  echo "Pulling image from docker hub as it was not found locally"

  PULL_RESPONSE=$(curl -s --unix-socket /var/run/docker.sock -X POST "http:/v1.40/images/create?fromImage=${IMAGE_NAME}&tag=${IMAGE_TAG}")

  echo "$PULL_RESPONSE"

  PULL_RESPONSE_SIZE=$(echo "$PULL_RESPONSE" | wc -l )
  PULL_RESPONSE_MSG=$(echo "$PULL_RESPONSE" | jq ".message")

  if [[ "$PULL_RESPONSE_SIZE" -eq 1 ]] && [[ ! -z "$PULL_RESPONSE_MSG" ]];  then
     echo "$PULL_RESPONSE_MSG"
     echo "Error getting image ${IMAGE_NAME} from dockerhub. Looking into oyster repository"

     OYSTER_REPOSITORY_USER="AWS"
     OYSTER_REPOSITORY_PWD=$(aws ecr get-login-password)
     OYSTER_REPOSITORY_URI=136531923082.dkr.ecr.us-west-2.amazonaws.com

     DOCKER_AUTH_JSON=$(echo "{ \"username\": \"$OYSTER_REPOSITORY_USER\", \"password\": \"$OYSTER_REPOSITORY_PWD\", \"email\": \"\", \"serveraddress\": \"${OYSTER_REPOSITORY_URI}\" }")
     #echo $DOCKER_AUTH_JSON
     echo | base64 -w0 > /dev/null 2>&1
     if [ $? -eq 0 ]; then
         # GNU coreutils base64, '-w' supported
         DOCKER_AUTH_BASE64=$(echo $DOCKER_AUTH_JSON | base64 -w 0)
     else
         # Openssl base64, no wrapping by default
         DOCKER_AUTH_BASE64=$(echo $DOCKER_AUTH_JSON | base64)
     fi
     COMPOSE_IMAGE_NAME="${OYSTER_REPOSITORY_URI}/${IMAGE_NAME}"
     curl -H "X-Registry-Auth: $DOCKER_AUTH_BASE64"  --unix-socket /var/run/docker.sock -X POST "http:/v1.40/images/create?fromImage=${COMPOSE_IMAGE_NAME}&tag=${IMAGE_TAG}"

     CONFIG_FILE_JSON=$(echo "$CONFIG_FILE_JSON" | sed "s/${FULL_IMAGE_NAME}/${OYSTER_REPOSITORY_URI}\/${FULL_IMAGE_NAME}/" )
     echo "Parsed config for oyster repository"
     echo "$CONFIG_FILE_JSON"
  fi
fi

if [[ "${IMAGE_NAME}" == *"/"* ]]; then
	IMAGE_NAME="${IMAGE_NAME/\//-}" 
fi

echo "Creating Docker container: $CONTAINER_NAME"

DOCKER_CONTAINER_RESPONSE=$(curl -s --unix-socket /var/run/docker.sock -H "Content-Type: application/json" -d "$CONFIG_FILE_JSON" -X POST http:/v1.40/containers/create?name=${CONTAINER_NAME})

echo "Docker create response: $DOCKER_CONTAINER_RESPONSE"

DOCKER_CONTAINER_ID=$(echo $DOCKER_CONTAINER_RESPONSE | jq ".Id" | sed 's/"//g')

echo "Docker Id resolved: $DOCKER_CONTAINER_ID"
curl --unix-socket /var/run/docker.sock -X POST http:/v1.40/containers/$DOCKER_CONTAINER_ID/start

echo "Docker detail info for id $DOCKER_CONTAINER_ID"

curl -s -G --unix-socket /var/run/docker.sock -X GET http:/v1.40/containers/json?all=true --data-urlencode  "filters={\"id\":[\"${DOCKER_CONTAINER_ID}\"]}" | jq .

#wait until the container is in health state or reach its timeout
start=$((SECONDS))
end=$((start + 15))
HEALTH_STATUS=

echo "Waiting for a healthy status from container $DOCKER_CONTAINER_ID"

while [[ "$HEALTH_STATUS" != "healthy" ]] && [[ $((SECONDS)) -lt $end ]]
do
   HEALTH_STATUS=$(curl -s --unix-socket /var/run/docker.sock  http:/v1.40/containers/${DOCKER_CONTAINER_ID}/json | jq ".State.Health.Status" | sed 's/"//g' )
   echo -ne "."
done
echo

echo "docker container status is $HEALTH_STATUS after $((SECONDS - start)) seconds" 
