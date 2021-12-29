package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_data_record_extend.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2021-09-29-hme_data_record_extend") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_data_record_extend_s', startValue:"1")
        }
        createTable(tableName: "hme_data_record_extend", remarks: "采集项记录扩展表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "data_record_extend_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "job_record_id", type: "varchar(" + 100 * weight + ")",  remarks: "采集项记录ID")  {constraints(nullable:"false")}
            column(name: "job_id", type: "varchar(" + 100 * weight + ")",  remarks: "作业ID")  {constraints(nullable:"false")}  
            column(name: "standard_value", type: "decimal(36,6)",  remarks: "标准值")   
            column(name: "cos_status", type: "varchar(" + 20 * weight + ")",  remarks: "单路路数")   
            column(name: "remark", type: "varchar(" + 500 * weight + ")",  remarks: "备注")   
            column(name: "cid", type: "bigint(20)",  remarks: "CID")  {constraints(nullable:"false")}  
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
   createIndex(tableName: "hme_data_record_extend", indexName: "hme_data_record_extend_n1") {
            column(name: "job_id")
            column(name: "tenant_id")
        }

        addUniqueConstraint(columnNames:"job_record_id,tenant_id",tableName:"hme_data_record_extend",constraintName: "hme_data_record_extend_u1")
    }
}