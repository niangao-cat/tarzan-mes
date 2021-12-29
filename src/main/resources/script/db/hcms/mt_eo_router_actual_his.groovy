package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_eo_router_actual_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_eo_router_actual_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_eo_router_actual_his_s', startValue:"1")
        }
        createTable(tableName: "mt_eo_router_actual_his", remarks: "EO工艺路线实绩历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EO_ROUTER_ACTUAL_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "EO_ROUTER_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条执行作业工艺路线实绩记录")  {constraints(nullable:"false")}  
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO主键，标识唯一EO")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(20)",  remarks: "顺序(使用路径的顺序）")  {constraints(nullable:"false")}  
            column(name: "ROUTER_ID", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "工艺路线ID")  {constraints(nullable:"false")}  
            column(name: "STATUS", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "状态（值集：未开始、运行中（排队）、部分完成、已完成）")  {constraints(nullable:"false")}  
            column(name: "QTY", type: "decimal(20,6)",  remarks: "此工艺路线加工数量（即进入首工序排队的数量） ")  {constraints(nullable:"false")}  
            column(name: "SUB_ROUTER_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否为分支工艺路线标识")   
            column(name: "SOURCE_EO_STEP_ACTUAL_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "来源实绩步骤")   
            column(name: "COMPLETED_QTY", type: "decimal(20,6)",  remarks: "此工艺路线已完成数量")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "TRX_QTY", type: "decimal(20,6)",  remarks: "事务数量")  {constraints(nullable:"false")}  
            column(name: "TRX_COMPLETED_QTY", type: "decimal(20,6)",  remarks: "完成事务数量")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"EO_ID,SEQUENCE,EVENT_ID,TENANT_ID",tableName:"mt_eo_router_actual_his",constraintName: "MT_EO_ROUTER_ACTUAL_HIS_U1")
        addUniqueConstraint(columnNames:"EO_ID,ROUTER_ID,SOURCE_EO_STEP_ACTUAL_ID,EVENT_ID,TENANT_ID",tableName:"mt_eo_router_actual_his",constraintName: "MT_EO_ROUTER_ACTUAL_HIS_U2")
    }
}