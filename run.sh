#!/usr/bin/env bash

git pull

# 刷新环境变量
source /etc/profile

dirName='tarzan-mes'
appName='tarzan-mes'
port=15050

# 拉取新代码并打包
mvn clean package -U -DskipTests=true

# 根据端口号查询对应的pid，并删除服务进程
pid=$(netstat -nlp | grep :$port | awk '{print $7}' | awk -F"/" '{ print $1 }');
echo $pid
if [  -n  "$pid"  ];  then
    kill  -9  $pid;
fi

# 删除老文件，复制新文件
rm /data/ruike/hwms/app/$appName.jar -f
rm /data/ruike/hwms/logs/$appName.log -f
mv ./target/app.jar /data/ruike/hwms/app/$appName.jar

# 启动项目
cd /data/ruike/hwms/
nohup java -jar -Dspring.profiles.active=dev \
-Dspring.cloud.config.enabled=false \
-Dspring.cloud.config.uri=http://dev.hzero.org:8010 \
-Deureka.client.serviceUrl.defaultZone=http://dev.hzero.org:8000/eureka \
-Xms512m -Xmx1024m \
 ./app/$appName.jar > ./logs/$appName.log &

