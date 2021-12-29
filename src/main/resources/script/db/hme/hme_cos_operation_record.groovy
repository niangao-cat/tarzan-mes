package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_cos_operation_record.groovy') {
    changeSet(author: "wenzhang.yu@hand-china.com", id: "2020-08-17-hme_cos_operation_record") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_cos_operation_record_s', startValue:"1")
        }
        createTable(tableName: "hme_cos_operation_record", remarks: "来料信息记录") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "operation_record_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "工厂ID")  {constraints(nullable:"false")}  
            column(name: "work_order_id", type: "varchar(" + 100 * weight + ")",  remarks: "工单id")  {constraints(nullable:"false")}  
            column(name: "container_type_id", type: "varchar(" + 100 * weight + ")",  remarks: "容器类型id")   
            column(name: "cos_type", type: "varchar(" + 100 * weight + ")",  remarks: "COS类型")   
            column(name: "average_wavelength", type: "decimal(20,2)",  remarks: "平均波长 Avg λ（nm）")   
            column(name: "type", type: "varchar(" + 100 * weight + ")",  remarks: "类型")   
            column(name: "lot_no", type: "varchar(" + 100 * weight + ")",  remarks: "LOTNO")   
            column(name: "wafer", type: "varchar(" + 100 * weight + ")",  remarks: "wafer")   
            column(name: "remark", type: "varchar(" + 100 * weight + ")",  remarks: "备注")   
            column(name: "job_batch", type: "varchar(" + 100 * weight + ")",  remarks: "作业批次")   
            column(name: "bar_num", type: "bigint(20)",  remarks: "BAR条数")   
            column(name: "cos_num", type: "bigint(20)",  remarks: "芯片数")   
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