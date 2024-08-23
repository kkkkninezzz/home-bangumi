#!/bin/sh

mkdir -p /data/home-bangumi/db
mkdir -p /data/home-bangumi/static-resources
mkdir -p /data/home-bangumi/torrents
mkdir -p /rename-tasks
mkdir -p /out/rename-tasks

/opt/app/home-bangumi-web --spring.config.location=/opt/app/application.yaml
