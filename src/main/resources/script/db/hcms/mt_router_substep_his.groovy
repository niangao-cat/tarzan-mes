package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_router_substep_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_router_substep_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_router_substep_his_s', startValue:"1")
        }
        createTable(tableName: "mt_router_substep_his", remarks: "工艺路线子步骤历史") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ROUTER_SUBSTEP_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线子步骤历史唯一性标识")  {constraints(primaryKey: true)} 
            column(name: "ROUTER_SUBSTEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线子步骤")  {constraints(nullable:"false")}  
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线标识")  {constraints(nullable:"false")}  
            column(name: "SUBSTEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "子步骤")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(100)",  remarks: "子步骤顺序")  {constraints(nullable:"false")}  
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ROUTER_SUBSTEP_ID,EVENT_ID,TENANT_ID",tableName:"mt_router_substep_his",constraintName: "MT_ROUTER_SUBSTEP_HIS_U1")
    }
}