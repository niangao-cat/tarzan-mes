package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_numrange_object_column.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_numrange_object_column") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_numrange_object_column_s', startValue:"1")
        }
        createTable(tableName: "mt_numrange_object_column", remarks: "编码对象属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_COLUMN_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "编码对象列ID")  {constraints(primaryKey: true)} 
            column(name: "OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "编码对象ID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_COLUMN_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "列参数名")  {constraints(nullable:"false")}  
            column(name: "OBJECT_COLUMN_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "列参数含义")   
            column(name: "MODULE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "服务包")   
            column(name: "TYPE_GROUP", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "类型组")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"OBJECT_ID,OBJECT_COLUMN_CODE,TENANT_ID",tableName:"mt_numrange_object_column",constraintName: "MT_NUMRANGE_OBJECT_COLUMN_U1")
    }
}