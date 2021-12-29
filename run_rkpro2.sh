#!/usr/bin/env bash

# 刷新环境变量
source /etc/profile

dirName='tarzan-mes'
appName='tarzan-mes'
port=15050

# 拉取新代码并打包
#mvn clean package -U -DskipTests=true

# 根据端口号查询对应的pid，并删除服务进程
pid=$(netstat -nlp | grep :$port | awk '{print $7}' | awk -F"/" '{ print $1 }');
echo $pid
if [  -n  "$pid"  ];  then
    kill  -9  $pid;
fi

# 删除老文件，复制新文件
#rm /home/mes/hcms/app/$appName.jar -f
#rm /home/mes/hcms/logs/$appName.log -f
#mv ./target/app.jar /home/mes/hcms/app/$appName.jar

# 启动项目
cd /home/mes/hcms/
nohup java -jar -Dspring.profiles.active=rkpro2 \
-Dspring.cloud.config.enabled=false \
-Dspring.cloud.config.uri=http://mespro-2.raycus.com:8010 \
-Deureka.client.serviceUrl.defaultZone=http://mespro-2.raycus.com:8000/eureka \
-Dspring.redis.password=rk2019@ \
-Xms512m -Xmx1024m \
 ./app/$appName.jar > ./logs/$appName.log &

