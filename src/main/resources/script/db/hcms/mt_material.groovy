package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_material.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_material") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_material_s', startValue:"1")
        }
        createTable(tableName: "mt_material", remarks: "物料基础属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "物料编号")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_NAME", type: "varchar(" + 4000 * weight + ")",   defaultValue:"",   remarks: "物料名称")   
            column(name: "MATERIAL_DESIGN_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "物料图号")   
            column(name: "MATERIAL_IDENTIFY_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "物料简称")   
            column(name: "LENGTH", type: "decimal(36,6)",  remarks: "长")   
            column(name: "WIDTH", type: "decimal(36,6)",  remarks: "宽")   
            column(name: "HEIGHT", type: "decimal(36,6)",  remarks: "高")   
            column(name: "SIZE_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "尺寸单位")   
            column(name: "MODEL", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "材质/型号")   
            column(name: "VOLUME", type: "decimal(36,6)",  remarks: "体积")   
            column(name: "VOLUME_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "体积单位")   
            column(name: "WEIGHT", type: "decimal(36,6)",  remarks: "重量")   
            column(name: "WEIGHT_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "重量单位")   
            column(name: "SHELF_LIFE", type: "decimal(36,6)",  remarks: "保质期")   
            column(name: "SHELF_LIFE_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "保质期单位")   
            column(name: "PRIMARY_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "基本计量单位")  {constraints(nullable:"false")}  
            column(name: "SECONDARY_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "辅助单位")   
            column(name: "CONVERSION_RATE", type: "decimal(36,6)",  remarks: "主辅单位转换比例：基本计量单位/辅助单位")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_material", indexName: "MT_MATERIAL_N1") {
            column(name: "ENABLE_FLAG")
        }

        addUniqueConstraint(columnNames:"MATERIAL_CODE,TENANT_ID",tableName:"mt_material",constraintName: "MT_MATERIAL_U1")
    }
}