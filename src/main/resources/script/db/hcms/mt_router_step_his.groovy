package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_router_step_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_router_step_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_router_step_his_s', startValue:"1")
        }
        createTable(tableName: "mt_router_step_his", remarks: "工艺路线步骤历史") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ROUTER_STEP_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线步骤历史唯一性标识")  {constraints(primaryKey: true)} 
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线步骤唯一性标识")  {constraints(nullable:"false")}  
            column(name: "ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线标识")  {constraints(nullable:"false")}  
            column(name: "STEP_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "步骤识别码")  {constraints(nullable:"false")}  
            column(name: "ROUTER_STEP_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "步骤类型")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(100)",  remarks: "大致的执行顺序，实际的顺序看ROUTER_NEXT_STEP")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "工艺路线步骤描述")   
            column(name: "COPRODUCT_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "是否产生联产品")   
            column(name: "QUEUE_DECISION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "路径选择策略")   
            column(name: "ENTRY_STEP_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "入口步骤")   
            column(name: "KEY_STEP_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否关键步骤")   
            column(name: "COPIED_FROM_ROUTER_STEP_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "复制来源 ROUTER_STEP_ID")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ROUTER_STEP_ID,EVENT_ID,TENANT_ID",tableName:"mt_router_step_his",constraintName: "MT_ROUTER_STEP_HIS_U1")
    }
}