package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_exc_wkc_record.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_exc_wkc_record") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_exc_wkc_record_s', startValue:"1")
        }
        createTable(tableName: "hme_exc_wkc_record", remarks: "工序异常反馈记录表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")   
            column(name: "exception_wkc_record_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "sn_num", type: "varchar(" + 100 * weight + ")",  remarks: "在制品sn")   
            column(name: "eo_id", type: "varchar(" + 100 * weight + ")",  remarks: "在制品eo")   
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料ID")   
            column(name: "material_lot_code", type: "varchar(" + 255 * weight + ")",  remarks: "异常物料批")   
            column(name: "equipment_id", type: "varchar(" + 100 * weight + ")",  remarks: "异常设备ID")   
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位ID")  {constraints(nullable:"false")}  
            column(name: "wkc_shift_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位班次ID")   
            column(name: "shift_id", type: "varchar(" + 100 * weight + ")",  remarks: "班组ID")   
            column(name: "exception_id", type: "varchar(" + 100 * weight + ")",  remarks: "异常ID")  {constraints(nullable:"false")}  
            column(name: "exception_status", type: "varchar(" + 30 * weight + ")",  remarks: "异常状态")  {constraints(nullable:"false")}  
            column(name: "attachment_uuid", type: "varchar(" + 50 * weight + ")",  remarks: "附件uuid")   
            column(name: "exception_group_id", type: "varchar(" + 100 * weight + ")",  remarks: "异常收集组ID")  {constraints(nullable:"false")}  
            column(name: "exception_level", type: "int(9)",  remarks: "异常等级")   
            column(name: "exception_remark", type: "varchar(" + 1000 * weight + ")",  remarks: "异常备注")   
            column(name: "respond_time", type: "datetime",  remarks: "响应时间")   
            column(name: "responded_by", type: "bigint(20)",  remarks: "响应人")   
            column(name: "respond_remark", type: "varchar(" + 1000 * weight + ")",  remarks: "响应备注")   
            column(name: "close_time", type: "datetime",  remarks: "关闭时间")   
            column(name: "closed_by", type: "bigint(20)",  remarks: "异常关闭人")   
            column(name: "prod_flag", type: "varchar(" + 1 * weight + ")",  remarks: "数据处理标识")  {constraints(nullable:"false")}  
            column(name: "prod_batch_id", type: "varchar(" + 100 * weight + ")",  remarks: "处理批次号")   
            column(name: "prod_date", type: "datetime",  remarks: "处理时间")   
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

        }
   createIndex(tableName: "hme_exc_wkc_record", indexName: "hme_exc_wkc_record_n1") {
            column(name: "exception_id")
        }
   createIndex(tableName: "hme_exc_wkc_record", indexName: "hme_exc_wkc_record_n2") {
            column(name: "exception_group_id")
        }

    }
}