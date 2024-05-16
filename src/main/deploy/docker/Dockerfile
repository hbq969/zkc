# FROM cicdcsy.harbor.cmss.com:18080/elasticip/oraclejdk:8u202
FROM openjdk:8

MAINTAINER default

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /opt/app/zkc

ENV LANG=C.UTF-8

COPY / .

ENTRYPOINT ["/bin/bash", "/opt/app/zkc/deploy/docker/bootstrap.sh"]
