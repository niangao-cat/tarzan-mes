package script.db

databaseChangeLog(logicalFilePath: 'script/db/wms_dull_material_temp.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-wms_dull_material_temp") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'wms_dull_material_temp_s', startValue:"1")
        }
        createTable(tableName: "wms_dull_material_temp", remarks: "") {
            column(name: "MATERIAL_LOT_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "物料批编号")  {constraints(primaryKey: true)} 
            column(name: "DULL_TYPE", type: "varchar(" + 255 * weight + ")",  remarks: "报废类型")   
            column(name: "DULL_DATE", type: "varchar(" + 255 * weight + ")",  remarks: "报废时间")   

        }

    }
}