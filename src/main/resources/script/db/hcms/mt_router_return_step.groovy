package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_router_return_step.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_router_return_step") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_router_return_step_s', startValue:"1")
        }
        createTable(tableName: "mt_router_return_step", remarks: "返回步骤") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ROUTER_RETURN_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "返回步骤唯一标识")  {constraints(primaryKey: true)} 
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线步骤标识")  {constraints(nullable:"false")}  
            column(name: "RETURN_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "返回类型")  {constraints(nullable:"false")}  
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "返回原工艺路线的工艺")   
            column(name: "COMPLETE_ORIGINAL_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "原工艺是否完成标识")   
            column(name: "STEP_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "步骤识别码")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ROUTER_STEP_ID,TENANT_ID",tableName:"mt_router_return_step",constraintName: "MT_ROUTER_RETURN_STEP_U1")
    }
}