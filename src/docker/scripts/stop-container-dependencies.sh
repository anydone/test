#!/bin/bash

for d in src/docker/resources/*docker-config.json;
do
  ls -l "${d}" ;
  src/docker/scripts/./stop-container.sh "${d}"
done
