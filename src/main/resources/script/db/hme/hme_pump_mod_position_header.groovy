package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_pump_mod_position_header.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2021-08-25-hme_pump_mod_position_header") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_pump_mod_position_header_s', startValue:"1")
        }
        createTable(tableName: "hme_pump_mod_position_header", remarks: "泵浦源模块位置头表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "position_header_id", type: "varchar(" + 100 * weight + ")",  remarks: "头表主键")  {constraints(primaryKey: true)} 
            column(name: "work_order_id", type: "varchar(" + 100 * weight + ")",  remarks: "工单ID")  {constraints(nullable:"false")}  
            column(name: "eo_id", type: "varchar(" + 100 * weight + ")",  remarks: "eo")  {constraints(nullable:"false")}  
            column(name: "comb_material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "组合物料批ID")  {constraints(nullable:"false")}  
            column(name: "qty", type: "decimal(36,6)",  remarks: "数量")  {constraints(nullable:"false")}  
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

        addUniqueConstraint(columnNames:"comb_material_lot_id,eo_id,tenant_id",tableName:"hme_pump_mod_position_header",constraintName: "hme_pump_mod_position_header_u1")
    }
}