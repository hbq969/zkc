#!/bin/bash

export op="docker-build"

if [[ -f "../setenv.sh" ]];then
. ../setenv.sh
fi

name=zkc
ver=${tag}
tag=${name}:${ver}

echo "Stop Docker Containers ..."
cid=`docker ps -a|grep ${name}|grep ${ver}|awk '{print $1}'`
if [[ -n "${cid}" ]]; then
  docker rm -f ${cid}
  echo "${tag},${cid} was stop."
fi

echo "Uninstall Docker Images ..."
mid=`docker images|grep "${name}"|grep "${ver}"|awk '{print $3}'`
if [[ -n "${mid}" ]]; then
  docker rmi -f ${mid}
  echo "${tag},${mid} was uninstalled."
fi

cp Dockerfile ../../
echo "Start Docker Images Building ..."
docker build --platform ${platform} -t ${docker_prefix}/${tag} ../../
