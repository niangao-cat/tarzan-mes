package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_area.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_area") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_area_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_area", remarks: "区域") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "AREA_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "AREA_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "区域编号")  {constraints(nullable:"false")}  
            column(name: "AREA_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "区域名称")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "区域描述")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "COUNTRY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "国家")   
            column(name: "PROVINCE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "省")   
            column(name: "CITY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "市")   
            column(name: "COUNTY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "县")   
            column(name: "ADDRESS", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "除国家省市县的详细地址")   
            column(name: "AREA_CATEGORY", type: "varchar(" + 255 * weight + ")",  remarks: "区域分类，自定义类型，后续考虑基于类型拓展对应属性表")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_mod_area", indexName: "MT_MOD_AREA_N1") {
            column(name: "ENABLE_FLAG")
        }
   createIndex(tableName: "mt_mod_area", indexName: "MT_MOD_AREA_N2") {
            column(name: "AREA_CATEGORY")
        }

        addUniqueConstraint(columnNames:"AREA_CODE,TENANT_ID",tableName:"mt_mod_area",constraintName: "MT_MOD_AREA_U1")
    }
}