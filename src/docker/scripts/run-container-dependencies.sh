#!/bin/bash

IMAGE_NAME=$(basename $(pwd '{}'))
MAIN_SERVICE_CONFIG_NAME=src/docker/resources/${IMAGE_NAME}-docker-config.json

echo "main service: $MAIN_SERVICE_CONFIG_NAME"
for d in src/docker/resources/*docker-config.json;
do
  ls -l "${d}" ;
  if [[ "$d" != "$MAIN_SERVICE_CONFIG_NAME" ]]; then
     src/docker/scripts/./run-container.sh "${d}"
  fi
done
