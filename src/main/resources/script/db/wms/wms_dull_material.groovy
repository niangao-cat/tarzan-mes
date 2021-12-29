package script.db

databaseChangeLog(logicalFilePath: 'script/db/wms_dull_material.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-wms_dull_material") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'wms_dull_material_s', startValue:"1")
        }
        createTable(tableName: "wms_dull_material", remarks: "呆滞物料表") {
            column(name: "DULL_MATERIAL_ID", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_LOT_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"MATERIAL_LOT_CODE",tableName:"wms_dull_material",constraintName: "WMS_DULL_MATERIAL_U1")
    }
}