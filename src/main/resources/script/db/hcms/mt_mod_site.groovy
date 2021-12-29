package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_site.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_site") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_site_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_site", remarks: "站点") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID，主键唯一标识")  {constraints(primaryKey: true)} 
            column(name: "SITE_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "站点编码")  {constraints(nullable:"false")}  
            column(name: "SITE_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "站点名称")  {constraints(nullable:"false")}  
            column(name: "SITE_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "站点类型")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "有效性")  {constraints(nullable:"false")}  
            column(name: "COUNTRY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "国家")   
            column(name: "PROVINCE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "省")   
            column(name: "CITY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "市")   
            column(name: "COUNTY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "县")   
            column(name: "ADDRESS", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "除国家省市县的详细地址")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_mod_site", indexName: "MT_MOD_SITE_N1") {
            column(name: "SITE_TYPE")
        }
   createIndex(tableName: "mt_mod_site", indexName: "MT_MOD_SITE_N2") {
            column(name: "ENABLE_FLAG")
        }

        addUniqueConstraint(columnNames:"SITE_CODE,TENANT_ID",tableName:"mt_mod_site",constraintName: "MT_MOD_SITE_U1")
    }
}