package script.db

databaseChangeLog(logicalFilePath: 'script/db/wms_material_bin.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-wms_material_bin") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'wms_material_bin_s', startValue:"1")
        }
        createTable(tableName: "wms_material_bin", remarks: "物料bin值表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID（企业ID）")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_BIN_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "物料ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "物料编码")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_ID", type: "varchar(" + 255 * weight + ")",  remarks: "供应商ID")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "供应商编码")  {constraints(nullable:"false")}  
            column(name: "SPECS", type: "varchar(" + 100 * weight + ")",  remarks: "规格")   
            column(name: "COLOR_BIN", type: "varchar(" + 100 * weight + ")",  remarks: "色BIN")   
            column(name: "LIGHT_BIN", type: "varchar(" + 100 * weight + ")",  remarks: "亮BIN")   
            column(name: "VOLTAGE_BIN", type: "varchar(" + 100 * weight + ")",  remarks: "电压BIN")   
            column(name: "GRADE_CODE", type: "varchar(" + 100 * weight + ")",  remarks: "等级编码")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 20 * weight + ")",  remarks: "等级编码有效性")  {constraints(nullable:"false")}  
            column(name: "OWNER_BY", type: "varchar(" + 100 * weight + ")",  remarks: "资料所有者")   
            column(name: "OWNER_DEPARTMENT", type: "varchar(" + 100 * weight + ")",  remarks: "资料所有者部门")   
            column(name: "MODIFY_BY", type: "varchar(" + 100 * weight + ")",  remarks: "资料修改者")   
            column(name: "MODIFY_DATE", type: "datetime",  remarks: "资料修改日")   
            column(name: "CREATION_DEPARTMENT", type: "varchar(" + 100 * weight + ")",  remarks: "资料建立部门")   
            column(name: "CREATION_BY", type: "varchar(" + 100 * weight + ")",  remarks: "资料建立者")   
            column(name: "ATTRIBUTE1", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE6", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE11", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE12", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE13", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE14", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE15", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "wms_material_bin", indexName: "z_material_bin_index") {
            column(name: "MATERIAL_ID")
        }

    }
}