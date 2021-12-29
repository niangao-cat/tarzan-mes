package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_material_category_set.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_material_category_set") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_material_category_set_s', startValue:"1")
        }
        createTable(tableName: "mt_material_category_set", remarks: "物料类别集") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_CATEGORY_SET_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "CATEGORY_SET_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "物料类别集编码")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "物料类别集描述")   
            column(name: "DEFAULT_SCHEDULE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否计划默认类别集")   
            column(name: "DEFAULT_PURCHASE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否采购默认类别集")   
            column(name: "DEFAULT_MANUFACTURING_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否生产默认类别集")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_material_category_set", indexName: "MT_MATERIAL_CATEGORY_SET_N1") {
            column(name: "ENABLE_FLAG")
        }
   createIndex(tableName: "mt_material_category_set", indexName: "MT_MATERIAL_CATEGORY_SET_N2") {
            column(name: "DEFAULT_SCHEDULE_FLAG")
        }
   createIndex(tableName: "mt_material_category_set", indexName: "MT_MATERIAL_CATEGORY_SET_N3") {
            column(name: "DEFAULT_PURCHASE_FLAG")
        }
   createIndex(tableName: "mt_material_category_set", indexName: "MT_MATERIAL_CATEGORY_SET_N4") {
            column(name: "DEFAULT_MANUFACTURING_FLAG")
        }

        addUniqueConstraint(columnNames:"CATEGORY_SET_CODE,TENANT_ID",tableName:"mt_material_category_set",constraintName: "MT_MATERIAL_CATEGORY_SET_U1")
    }
}