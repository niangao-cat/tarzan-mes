package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_eo_test_data_record.groovy') {
    changeSet(author: "penglin.sui@hand-china.com", id: "2020-09-20-hme_eo_test_data_record") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_eo_test_data_record_s', startValue:"1")
        }
        createTable(tableName: "hme_eo_test_data_record", remarks: "数据采集回测对比记录信息表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "test_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键id")  {constraints(nullable:"false")}  
            column(name: "eo_id", type: "varchar(" + 100 * weight + ")",  remarks: "eoid")  {constraints(nullable:"false")}  
            column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料批次id")  {constraints(nullable:"false")}  
            column(name: "operation_id", type: "varchar(" + 100 * weight + ")",  remarks: "工艺id")  {constraints(nullable:"false")}  
            column(name: "tag_group_id", type: "varchar(" + 100 * weight + ")",  remarks: "数组组id")  {constraints(nullable:"false")}  
            column(name: "tag_id", type: "varchar(" + 100 * weight + ")",  remarks: "数据项id")  {constraints(nullable:"false")}  
            column(name: "rework_flag", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "返修标识")   
            column(name: "min_value", type: "decimal(36,6)",  remarks: "最小值")   
            column(name: "max_value", type: "decimal(36,6)",  remarks: "最大值")   
            column(name: "standard_value", type: "decimal(36,6)",  remarks: "标准值")   
            column(name: "result", type: "varchar(" + 255 * weight + ")",  remarks: "返回结果值")   
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
   createIndex(tableName: "hme_eo_test_data_record", indexName: "hme_eo_test_data_record_n1") {
            column(name: "tenant_id")
        }
   createIndex(tableName: "hme_eo_test_data_record", indexName: "hme_eo_test_data_record_n2") {
            column(name: "eo_id")
            column(name: "tag_id")
            column(name: "material_lot_id")
            column(name: "operation_id")
        }
   createIndex(tableName: "hme_eo_test_data_record", indexName: "hme_eo_test_data_record_n3") {
            column(name: "tag_group_id")
            column(name: "tag_id")
            column(name: "rework_flag")
        }

    }
}