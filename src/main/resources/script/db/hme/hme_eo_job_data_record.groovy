package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_eo_job_data_record.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2021-08-23-hme_eo_job_data_record") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_eo_job_data_record_s', startValue:"1")
        }
        createTable(tableName: "hme_eo_job_data_record", remarks: "工序作业平台-数据采集") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "表ID，主键")  {constraints(nullable:"false")}  
            column(name: "job_record_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "job_id", type: "varchar(" + 100 * weight + ")",  remarks: "作业ID")  {constraints(nullable:"false")}  
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位ID")  {constraints(nullable:"false")}  
            column(name: "eo_id", type: "varchar(" + 100 * weight + ")",  remarks: "EO")   
            column(name: "tag_group_id", type: "varchar(" + 100 * weight + ")",  remarks: "数据采集组ID")   
            column(name: "tag_id", type: "varchar(" + 100 * weight + ")",  remarks: "数据采集项ID")  {constraints(nullable:"false")}  
            column(name: "minimum_value", type: "decimal(36,6)",  remarks: "下限值")   
            column(name: "maximal_value", type: "decimal(36,6)",  remarks: "上限值")   
            column(name: "standard_value", type: "decimal(36,6)",  remarks: "标准值")   
            column(name: "group_purpose", type: "varchar(" + 30 * weight + ")",  remarks: "采集组用途")  {constraints(nullable:"false")}  
            column(name: "result", type: "varchar(" + 200 * weight + ")",  remarks: "采集结果")   
            column(name: "data_record_id", type: "varchar(" + 100 * weight + ")",  remarks: "数据采集实绩ID")   
            column(name: "is_supplement", type: "tinyint(1)",  remarks: "补充数据采集标识")   
            column(name: "remark", type: "varchar(" + 1000 * weight + ")",  remarks: "备注")   
            column(name: "cos_status", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "单路状态")   
            column(name: "cid", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "attribute_category", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute4", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute5", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute6", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute7", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute8", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute9", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute10", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute11", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute12", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute13", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute14", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "attribute15", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   

        }
   createIndex(tableName: "hme_eo_job_data_record", indexName: "hme_eo_job_data_record_n1") {
            column(name: "workcell_id")
        }
   createIndex(tableName: "hme_eo_job_data_record", indexName: "hme_eo_job_data_record_n2") {
            column(name: "tenant_id")
            column(name: "job_id")
            column(name: "workcell_id")
            column(name: "tag_group_id")
            column(name: "tag_id")
        }
   createIndex(tableName: "hme_eo_job_data_record", indexName: "hme_eo_job_data_record_n3") {
            column(name: "job_id")
            column(name: "group_purpose")
        }
   createIndex(tableName: "hme_eo_job_data_record", indexName: "hme_eo_job_data_record_n6") {
            column(name: "job_id")
            column(name: "tenant_id")
        }

    }
}