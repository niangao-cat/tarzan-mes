package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_operation_attr.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_operation_attr") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_operation_attr_s', startValue:"1")
        }
        createTable(tableName: "mt_operation_attr", remarks: "工艺扩展表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ATTR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "扩展表主键ID，唯一性标识")  {constraints(primaryKey: true)} 
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主表ID")  {constraints(nullable:"false")}  
            column(name: "ATTR_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "扩展属性名")  {constraints(nullable:"false")}  
            column(name: "ATTR_VALUE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "扩展属性值")   
            column(name: "LANG", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "语言类型")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"OPERATION_ID,ATTR_NAME,LANG,TENANT_ID",tableName:"mt_operation_attr",constraintName: "mt_operation_attr_u1")
    }
}