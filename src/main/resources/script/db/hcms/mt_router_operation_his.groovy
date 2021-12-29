package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_router_operation_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_router_operation_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_router_operation_his_s', startValue:"1")
        }
        createTable(tableName: "mt_router_operation_his", remarks: "工艺路线步骤对应工序历史") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ROUTER_OPERATION_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "历史表主键ID，唯一性标识")  {constraints(primaryKey: true)} 
            column(name: "ROUTER_OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线步骤唯一标识")  {constraints(nullable:"false")}  
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线标识")  {constraints(nullable:"false")}  
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺标识")  {constraints(nullable:"false")}  
            column(name: "MAX_LOOP", type: "bigint(100)",  remarks: "最大循环次数")   
            column(name: "SPECIAL_INTRUCTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "特殊指令，展示在前台的文本")   
            column(name: "REQUIRED_TIME_IN_PROCESS", type: "decimal(36,6)",  remarks: "步骤处理所需时间（分钟）")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ROUTER_OPERATION_ID,EVENT_ID,TENANT_ID",tableName:"mt_router_operation_his",constraintName: "MT_ROUTER_OPERATION_HIS_U1")
    }
}