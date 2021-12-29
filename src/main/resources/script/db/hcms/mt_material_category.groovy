package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_material_category.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_material_category") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_material_category_s', startValue:"1")
        }
        createTable(tableName: "mt_material_category", remarks: "物料类别") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_CATEGORY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID,，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "CATEGORY_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "物料类别编码")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "物料类别描述")   
            column(name: "MATERIAL_CATEGORY_SET_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "所属类别集，取自物料类别集，指向唯一物料类别集")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_material_category", indexName: "MT_MATERIAL_CATEGORY_N1") {
            column(name: "ENABLE_FLAG")
        }

        addUniqueConstraint(columnNames:"CATEGORY_CODE,TENANT_ID",tableName:"mt_material_category",constraintName: "MT_MATERIAL_CATEGORY_U1")
    }
}