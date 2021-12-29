package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_event_request_type.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_event_request_type") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_event_request_type_s', startValue:"1")
        }
        createTable(tableName: "mt_event_request_type", remarks: "事件组类型定义") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "REQUEST_TYPE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件组类型ID")  {constraints(primaryKey: true)} 
            column(name: "REQUEST_TYPE_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事件组类型编码")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 4000 * weight + ")",   defaultValue:"",   remarks: "描述")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"REQUEST_TYPE_CODE,TENANT_ID",tableName:"mt_event_request_type",constraintName: "MT_REQUEST_TYPE_U1")
    }
}