#!/bin/sh

mkdir -p /data/home-bangumi/db
mkdir -p /data/home-bangumi/static-resources
mkdir -p /data/home-bangumi/torrents

/opt/app/home-bangumi-web --spring.config.location=/opt/app/application.yaml