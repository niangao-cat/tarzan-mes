package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_tag_api.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_tag_api") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_tag_api_s', startValue:"1")
        }
        createTable(tableName: "mt_tag_api", remarks: "API转化表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "API_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "API_CLASS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "类名")  {constraints(nullable:"false")}  
            column(name: "API_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "API名称")  {constraints(nullable:"false")}  
            column(name: "API_FUNCTION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "API函数")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"API_CLASS,API_FUNCTION,TENANT_ID",tableName:"mt_tag_api",constraintName: "mt_tag_api_u1")
    }
}