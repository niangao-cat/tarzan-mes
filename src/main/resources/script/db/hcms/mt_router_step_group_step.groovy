package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_router_step_group_step.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_router_step_group_step") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_router_step_group_step_s', startValue:"1")
        }
        createTable(tableName: "mt_router_step_group_step", remarks: "工艺路线步骤组行步骤") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ROUTER_STEP_GROUP_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线步骤组分配唯一标识")  {constraints(primaryKey: true)} 
            column(name: "ROUTER_STEP_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线步骤组标识")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(100)",  remarks: "顺序")   
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线步骤标识")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ROUTER_STEP_GROUP_ID,ROUTER_STEP_ID,TENANT_ID",tableName:"mt_router_step_group_step",constraintName: "MT_ROUTER_STEP_GROUP_STEP_U1")
    }
}