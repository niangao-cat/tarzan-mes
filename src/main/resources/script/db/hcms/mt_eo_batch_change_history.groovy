package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_eo_batch_change_history.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_eo_batch_change_history") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_eo_batch_change_history_s', startValue:"1")
        }
        createTable(tableName: "mt_eo_batch_change_history", remarks: "执行作业变更记录") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EO_BATCH_CHANGE_HISTORY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "变更后EO,EO主键，标识唯一EO")  {constraints(nullable:"false")}  
            column(name: "SOURCE_EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "变更来源EO，EO主键，标识唯一EO")  {constraints(nullable:"false")}  
            column(name: "REASON", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "变更原因R：下达S：序列化P：拆分A：自动拆分M：合并，合并时可能记录多条记录")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(100)",  remarks: "顺序")  {constraints(nullable:"false")}  
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID，用于表示一次变更操作")  {constraints(nullable:"false")}  
            column(name: "SOURCE_TRX_QTY", type: "decimal(36,6)",  remarks: "来源变更数量")  {constraints(nullable:"false")}  
            column(name: "TRX_QTY", type: "decimal(36,6)",  remarks: "变更数量")  {constraints(nullable:"false")}  
            column(name: "SOURCE_EO_STEP_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "步骤实绩ID")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"SEQUENCE,EVENT_ID,TENANT_ID",tableName:"mt_eo_batch_change_history",constraintName: "MT_EO_BATCH_CHANGE_HISTORY_U1")
    }
}