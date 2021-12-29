package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_pump_mod_position_line.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2021-08-25-hme_pump_mod_position_line") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_pump_mod_position_line_s', startValue:"1")
        }
        createTable(tableName: "hme_pump_mod_position_line", remarks: "泵浦源模块位置行表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "position_line_id", type: "varchar(" + 100 * weight + ")",  remarks: "行表主键")  {constraints(nullable:"false")}  
            column(name: "position_header_id", type: "varchar(" + 100 * weight + ")",  remarks: "头表主键")  {constraints(nullable:"false")}  
            column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "泵浦源物料批ID")  {constraints(nullable:"false")}  
            column(name: "sub_barcode_seq", type: "bigint(5)",  remarks: "子条码排序")  {constraints(nullable:"false")}  
            column(name: "position", type: "varchar(" + 10 * weight + ")",  remarks: "位置")  {constraints(nullable:"false")}  
            column(name: "tag_id", type: "varchar(" + 100 * weight + ")",  remarks: "数据项ID")  {constraints(nullable:"false")}  
            column(name: "cid", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
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

        addUniqueConstraint(columnNames:"position_header_id,position,tenant_id",tableName:"hme_pump_mod_position_line",constraintName: "hme_pump_mod_position_line_u1")
    }
}