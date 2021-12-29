package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_event_type.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_event_type") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_event_type_s', startValue:"1")
        }
        createTable(tableName: "mt_event_type", remarks: "事件类型定义") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EVENT_TYPE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件类型ID")  {constraints(primaryKey: true)} 
            column(name: "EVENT_TYPE_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事件类型编码")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 4000 * weight + ")",   defaultValue:"",   remarks: "描述")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "DEFAULT_EVENT_TYPE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "系统初始事件类型标识")  {constraints(nullable:"false")}  
            column(name: "ONHAND_CHANGE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否影响现有量标识")  {constraints(nullable:"false")}  
            column(name: "ONHAND_CHANGE_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "现有量变化类型（增加/减少）")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"EVENT_TYPE_CODE,TENANT_ID",tableName:"mt_event_type",constraintName: "MT_EVENT_TYPE_U1")
    }
}