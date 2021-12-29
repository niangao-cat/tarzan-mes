package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_router_next_step_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_router_next_step_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_router_next_step_his_s', startValue:"1")
        }
        createTable(tableName: "mt_router_next_step_his", remarks: "下一步骤历史") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ROUTER_NEXT_STEP_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "历史表主键ID，唯一性标识")  {constraints(primaryKey: true)} 
            column(name: "ROUTER_NEXT_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主表主键ID")  {constraints(nullable:"false")}  
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "当前步骤标识")  {constraints(nullable:"false")}  
            column(name: "NEXT_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "下一步骤标识")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(100)",  remarks: "顺序")   
            column(name: "NEXT_DECISION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "选择策略类型")  {constraints(nullable:"false")}  
            column(name: "NEXT_DECISION_VALUE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "选择策略对应值(MAIN时无值，PRODUCT时为物料ID，NC时为编码)")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ROUTER_NEXT_STEP_ID,EVENT_ID,TENANT_ID",tableName:"mt_router_next_step_his",constraintName: "MT_ROUTER_NEXT_STEP_HIS_U1")
    }
}