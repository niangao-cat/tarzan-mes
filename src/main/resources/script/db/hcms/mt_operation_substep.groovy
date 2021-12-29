package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_operation_substep.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_operation_substep") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_operation_substep_s', startValue:"1")
        }
        createTable(tableName: "mt_operation_substep", remarks: "工艺子步骤") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "OPERATION_SUBSTEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺子步骤关系唯一标识")  {constraints(primaryKey: true)} 
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺标识")  {constraints(nullable:"false")}  
            column(name: "SUBSTEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "子步骤标识")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(100)",  remarks: "顺序")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_operation_substep", indexName: "MT_OPERATION_SUBSTEP_N1") {
            column(name: "SEQUENCE")
        }

        addUniqueConstraint(columnNames:"OPERATION_ID,SUBSTEP_ID,TENANT_ID",tableName:"mt_operation_substep",constraintName: "MT_OPERATION_SUBSTEP_U1")
        addUniqueConstraint(columnNames:"OPERATION_ID,SEQUENCE,TENANT_ID",tableName:"mt_operation_substep",constraintName: "MT_OPERATION_SUBSTEP_U2")
    }
}