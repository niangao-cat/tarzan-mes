package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_enterprise.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_enterprise") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_enterprise_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_enterprise", remarks: "企业") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ENTERPRISE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "企业ID，主键，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "ENTERPRISE_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "企业编码")  {constraints(nullable:"false")}  
            column(name: "ENTERPRISE_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "企业名称")  {constraints(nullable:"false")}  
            column(name: "ENTERPRISE_SHORT_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "企业简称")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_mod_enterprise", indexName: "MT_MOD_ENTERPRISE_N1") {
            column(name: "ENABLE_FLAG")
        }

        addUniqueConstraint(columnNames:"ENTERPRISE_CODE,TENANT_ID",tableName:"mt_mod_enterprise",constraintName: "MT_MOD_ENTERPRISE_U1")
    }
}