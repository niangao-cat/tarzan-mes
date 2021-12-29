package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_cos_test_monitor_header.groovy') {
changeSet(author: "wengang.qiang@hand-china.com", id: "2021-10-09-hme_cos_test_monitor_header") {
    def weight = 1
    if(helper.isSqlServer()){
        weight = 2
    } else if(helper.isOracle()){
        weight = 3
    }
    if(helper.dbType().isSupportSequence()){
        createSequence(sequenceName: 'hme_cos_test_monitor_header_s', startValue:"1")
    }
    createTable(tableName: "hme_cos_test_monitor_header", remarks: "COS测试良率监控头表") {
        column(name: "tenant_id", type: "bigint",  remarks: "租户id")  {constraints(nullable:"false")}  
        column(name: "cos_monitor_header_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
        column(name: "monitor_doc_num", type: "varchar(" + 100 * weight + ")",  remarks: "监控单据号")  {constraints(nullable:"false")}  
        column(name: "doc_status", type: "varchar(" + 30 * weight + ")",  remarks: "单据状态")  {constraints(nullable:"false")}  
        column(name: "check_status", type: "varchar(" + 30 * weight + ")",  remarks: "审核状态")  {constraints(nullable:"false")}  
        column(name: "cos_type", type: "varchar(" + 20 * weight + ")",  remarks: "cos类型")  {constraints(nullable:"false")}  
        column(name: "wafer", type: "varchar(" + 50 * weight + ")",  remarks: "WAFER")  {constraints(nullable:"false")}  
        column(name: "test_pass_rate", type: "decimal(10,2)",  remarks: "cos良率")  {constraints(nullable:"false")}  
        column(name: "pass_date", type: "datetime",  remarks: "放行时间")   
        column(name: "pass_by", type: "bigint",  remarks: "放行人")   
        column(name: "test_qty", type: "bigint",  remarks: "测试数量")  {constraints(nullable:"false")}  
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

                createIndex(tableName: "hme_cos_test_monitor_header", indexName: "hme_cos_test_monitor_header_n1") {
                column(name: "wafer")
                column(name: "cos_type")
                column(name: "tenant_id")
            }
    
            addUniqueConstraint(columnNames:"monitor_doc_num,tenant_id",tableName:"hme_cos_test_monitor_header",constraintName: "hme_cos_test_monitor_header_u1")
                }
}