#!/bin/bash

# 需要提交代码测试的部署脚本(跑流水线集成测试[SIT]时)
# $1    HOST_GROUP                  代表部署的目标机IP,配置多IP时每次只取一个
# $2    PACKAGE_LOCAL_PATH    部署包的本地路径；镜像的本地目录
# $3    PACKAGE_NAME              部署包的文件名，格式：包名-版本.扩展名;镜像为镜像名-执行编号.IMAGE
# $4    PACKAGE_REMOTE_PATH   部署包推到远程目标机的目录；镜像为空值
# $5    BUILD_SEQ                     构建任务执行编号
# $6    PACKAGE_VERSION           通用压缩包(jar/war/tar/zip/tar.gz等格式)的版本
# $7    REMOTE_REPOSITORY       远程镜像仓库地址
# $8    PACKAGE_ARRAY              部署制品列表，例如：[(buildSeq=,packageVersion=999.99.23,packageName=et-service-e-1.0.0-999.99.23-999.99.23.jar,packageLocalPath=null),(buildSeq=,packageVersion=6.0.0,packageName=et-service-d-1.0.0-6.0.0_h2-6.0.0.jar,packageLocalPath=null)]
# $9    GRAY                              灰度标识
# $10   SPRING_PROFILES_ACTIVE  应用运行环境profile
# $11   K8S_NS                            k8s命名空间

# 调用此部署脚本前会执行以下内容，将镜像从 <研发仓库> 推送到 <测试域仓库>
# docker tag nexus.cmss.com:8034/wap/bc-monitor-manage:V3.8.0-12655701 cicdcsy.harbor.cmss.com:18080/webfullstackprotection/bc-monitor-manage:V3.8.0-12655701
# docker push cicdcsy.harbor.cmss.com:18080/webfullstackprotection/bc-monitor-manage:V3.8.0-12655701

set -x
echo "传递给脚本的参数：【$*】"

# 10.253.71.4
HOST_GROUP=$1
# uselesspath
PACKAGE_LOCAL_PATH=$2
# webfullstackprotection/bc-monitor-manage
PACKAGE_NAME=$3
# null
PACKAGE_REMOTE_PATH=$4
# 9807030
BUILD_SEQ=$5
# V3.8.0-12655701 该版本需要借助流水线产生，格式：流水线版本号+BUILD_SEQ
PACKAGE_VERSION=$6
# cicdcsy.harbor.cmss.com:18080，测试域镜像仓库（部署任务，主机配置中镜像仓库配置）
REMOTE_REPOSITORY=${7:-cicdcsy.harbor.cmss.com:18080}
# 灰度标识
GRAY=${8#*^}
# 应用运行环境profile
SPRING_PROFILES_ACTIVE=${9#*^}
# 应用部署k8s命名空间
K8S_NS=${10#*^}

# bc-monitor-manage
APP_NAME=${PACKAGE_NAME##*/}
# webfullstackprotection
DOCKER_NS=${PACKAGE_NAME%/*}
# /apps/svr/safebox-springboot/bc-monitor-manage
appDir=/apps/svr/safebox-springboot/${APP_NAME}

# cicdcsy.harbor.cmss.com:18080/webfullstackprotection/bc-monitor-manage:V3.8.0-12655701
dockerImage=${REMOTE_REPOSITORY}/${PACKAGE_NAME}:${PACKAGE_VERSION}
DEPLOY_NAME=${APP_NAME}-${GRAY}



ssh root@${HOST_GROUP} "set -x; [ -d ${appDir} ] || mkdir -p ${appDir};
cd ${appDir};
/apps/bin/kubectl set image deployment ${DEPLOY_NAME} ${DEPLOY_NAME}=${dockerImage} -n ${K8S_NS};
[ \$? -eq 0 ] && sleep 15 && docker image prune -a -f && /apps/bin/kubectl get pod -n ${K8S_NS} |grep ${DEPLOY_NAME};"
[ $? -eq 0 ] && echo '---------- Startup Successfully! ----------'
