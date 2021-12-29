#!/usr/bin/env bash
mkdir -p target
if [ ! -f target/choerodon-tool-liquibase.jar ]
then
    curl https://nexus.choerodon.com.cn/repository/choerodon-release/io/choerodon/choerodon-tool-liquibase/0.9.2.RELEASE/choerodon-tool-liquibase-0.9.2.RELEASE.jar -o target/choerodon-tool-liquibase.jar
fi

java -Dspring.datasource.url="jdbc:mysql://dev.hzero.super.com:3306/tarzan_mes?useUnicode=true&characterEncoding=utf-8&useSSL=false" \
	 -Dspring.datasource.username=hmes \
	 -Dspring.datasource.password=Init1234 \
	 -Ddata.drop=false -Ddata.init=init \
	 -Ddata.dir=src/main/resources \
	 -jar target/choerodon-tool-liquibase.jar



