package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_extend_settings.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_extend_settings") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_extend_settings_s', startValue:"1")
        }
        createTable(tableName: "mt_extend_settings", remarks: "") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EXTEND_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "拓展字段主键")  {constraints(primaryKey: true)} 
            column(name: "EXTEND_TABLE_DESC_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "拓展表")  {constraints(nullable:"false")}  
            column(name: "ATTR_NAME", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "拓展字段")  {constraints(nullable:"false")}  
            column(name: "ATTR_MEANING", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "拓展字段描述")  {constraints(nullable:"false")}  
            column(name: "TL_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "多语言标识")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "有效性")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(100)",  remarks: "顺序")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_extend_settings", indexName: "mt_extend_setting_n1") {
            column(name: "EXTEND_TABLE_DESC_ID")
        }

        addUniqueConstraint(columnNames:"EXTEND_TABLE_DESC_ID,ATTR_NAME,TENANT_ID",tableName:"mt_extend_settings",constraintName: "mt_extend_setting_u1")
    }
}