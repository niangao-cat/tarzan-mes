package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_event_object_column.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_event_object_column") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_event_object_column_s', startValue:"1")
        }
        createTable(tableName: "mt_event_object_column", remarks: "对象列定义") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_COLUMN_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "对象列ID")  {constraints(primaryKey: true)} 
            column(name: "OBJECT_TYPE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "关联对象ID")  {constraints(nullable:"false")}  
            column(name: "COLUMN_FIELD", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "展示字段")  {constraints(nullable:"false")}  
            column(name: "COLUMN_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "字段类型")  {constraints(nullable:"false")}  
            column(name: "COLUMN_TITLE", type: "varchar(" + 4000 * weight + ")",   defaultValue:"",   remarks: "字段列标题")   
            column(name: "LINE_NUMBER", type: "bigint(100)",  remarks: "行号")  {constraints(nullable:"false")}  
            column(name: "KID_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否对象主键字段")  {constraints(nullable:"false")}  
            column(name: "EVENT_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否事件字段")  {constraints(nullable:"false")}  
            column(name: "DISPLAY_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否展示")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"OBJECT_TYPE_ID,COLUMN_FIELD,TENANT_ID",tableName:"mt_event_object_column",constraintName: "MT_OBJECT_COLUMN_U1")
    }
}