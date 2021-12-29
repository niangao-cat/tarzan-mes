package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_load_job_object.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2021-02-02-hme_load_job_object") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_load_job_object_s', startValue:"1")
        }
        createTable(tableName: "hme_load_job_object", remarks: "装载信息作业对象表") {
            column(name: "load_object_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "load_job_id", type: "varchar(" + 100 * weight + ")",  remarks: "装载作业表")  {constraints(nullable:"false")}  
            column(name: "object_type", type: "varchar(" + 60 * weight + ")",  remarks: "对象类型")  {constraints(nullable:"false")}  
            column(name: "object_id", type: "varchar(" + 100 * weight + ")",  remarks: "对象id")  {constraints(nullable:"false")}  
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "cid", type: "bigint(100)",  remarks: "cid")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "attribute_category", type: "varchar(" + 30 * weight + ")",  remarks: "")   
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute5", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute6", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute7", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute8", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute9", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute10", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute11", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute12", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute13", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute14", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute15", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }
   createIndex(tableName: "hme_load_job_object", indexName: "hme_load_job_object_n1") {
            column(name: "object_type")
            column(name: "object_id")
        }

        addUniqueConstraint(columnNames:"load_job_id,object_type,object_id",tableName:"hme_load_job_object",constraintName: "hme_load_job_object_u1")
    }
}