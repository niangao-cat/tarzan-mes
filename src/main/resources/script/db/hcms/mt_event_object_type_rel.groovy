package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_event_object_type_rel.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_event_object_type_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_event_object_type_rel_s', startValue:"1")
        }
        createTable(tableName: "mt_event_object_type_rel", remarks: "事件类型与对象类型关系定义") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "REL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "关系ID")  {constraints(primaryKey: true)} 
            column(name: "EVENT_TYPE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件类型ID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_TYPE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "对象类型ID")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"EVENT_TYPE_ID,OBJECT_TYPE_ID,TENANT_ID",tableName:"mt_event_object_type_rel",constraintName: "MT_EVENT_OBJECT_TYPE_REL_U1")
    }
}