package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_material_lot_load.groovy') {
    changeSet(author: "wenzhang.yu@hand-china.com", id: "2020-08-13-hme_material_lot_load") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_material_lot_load_s', startValue:"1")
        }
        createTable(tableName: "hme_material_lot_load", remarks: "来料装载位置表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "material_lot_load_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料批id")  {constraints(nullable:"false")}  
            column(name: "load_sequence", type: "bigint(100)",  remarks: "行序号")   
            column(name: "load_row", type: "bigint(2)",  remarks: "行")   
            column(name: "load_column", type: "bigint(2)",  remarks: "列")   
            column(name: "cos_num", type: "bigint(2)",  remarks: "芯片数")   
            column(name: "hot_sink_code", type: "varchar(" + 100 * weight + ")",  remarks: "热沉编号")   
            column(name: "source_material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "来源物料批ID")   
            column(name: "source_load_row", type: "bigint(2)",  remarks: "来源行")   
            column(name: "source_load_column", type: "bigint(2)",  remarks: "来源列")   
            column(name: "cid", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE_CATEGORY", type: "varchar(" + 30 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE6", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE11", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE12", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE13", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE14", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE15", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }

    }
}