package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_cos_test_monitor_line.groovy') {
changeSet(author: "wengang.qiang@hand-china.com", id: "2021-09-16-hme_cos_test_monitor_line") {
    def weight = 1
    if(helper.isSqlServer()){
        weight = 2
    } else if(helper.isOracle()){
        weight = 3
    }
    if(helper.dbType().isSupportSequence()){
        createSequence(sequenceName: 'hme_cos_test_monitor_line_s', startValue:"1")
    }
    createTable(tableName: "hme_cos_test_monitor_line", remarks: "COS测试良率监控行表") {
        column(name: "tenant_id", type: "bigint",  remarks: "租户id")  {constraints(nullable:"false")}  
        column(name: "cos_monitor_line_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
        column(name: "cos_monitor_header_id", type: "varchar(" + 100 * weight + ")",  remarks: "头表主键")  {constraints(nullable:"false")}  
        column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "盒子号")  {constraints(nullable:"false")}  
        column(name: "line_check_status", type: "varchar(" + 30 * weight + ")",  remarks: "盒子审核状态")  {constraints(nullable:"false")}  
        column(name: "material_lot_status", type: "varchar(" + 30 * weight + ")",  remarks: "盒子状态")  {constraints(nullable:"false")}  
        column(name: "pass_date", type: "datetime",  remarks: "放行时间")   
        column(name: "pass_by", type: "bigint",  remarks: "放行人")   
        column(name: "cid", type: "bigint",  remarks: "CID")  {constraints(nullable:"false")}  
        column(name: "object_version_number", type: "bigint",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
        column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
        column(name: "created_by", type: "bigint",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
        column(name: "last_updated_by", type: "bigint",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
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

        
            addUniqueConstraint(columnNames:"cos_monitor_header_id,material_lot_id,tenant_id",tableName:"hme_cos_test_monitor_line",constraintName: "hme_cos_test_monitor_line_u1")
            }
}