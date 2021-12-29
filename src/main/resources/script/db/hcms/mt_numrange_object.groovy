package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_numrange_object.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_numrange_object") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_numrange_object_s', startValue:"1")
        }
        createTable(tableName: "mt_numrange_object", remarks: "编码对象属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "编码对象ID")  {constraints(primaryKey: true)} 
            column(name: "OBJECT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "编码对象编码")  {constraints(nullable:"false")}  
            column(name: "OBJECT_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "编码对象名称")   
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "编码对象描述")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"OBJECT_CODE,TENANT_ID",tableName:"mt_numrange_object",constraintName: "MT_NUMRANGE_OBJECT_U1")
    }
}