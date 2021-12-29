package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_exc_wkc_record_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_exc_wkc_record_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_exc_wkc_record_his_s', startValue:"1")
        }
        createTable(tableName: "hme_exc_wkc_record_his", remarks: "工序异常反馈记录历史表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "")   
            column(name: "exception_wkc_record_his_id", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(primaryKey: true)} 
            column(name: "exception_wkc_record_id", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "sn_num", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "eo_id", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "material_lot_code", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "equipment_id", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "wkc_shift_id", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "shift_id", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "exception_id", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "exception_status", type: "varchar(" + 30 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "attachment_uuid", type: "varchar(" + 50 * weight + ")",  remarks: "")   
            column(name: "exception_group_id", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "exception_level", type: "int(9)",  remarks: "")   
            column(name: "exception_remark", type: "varchar(" + 1000 * weight + ")",  remarks: "")   
            column(name: "respond_time", type: "datetime",  remarks: "")   
            column(name: "responded_by", type: "bigint(20)",  remarks: "")   
            column(name: "respond_remark", type: "varchar(" + 1000 * weight + ")",  remarks: "")   
            column(name: "close_time", type: "datetime",  remarks: "")   
            column(name: "closed_by", type: "bigint(20)",  remarks: "")   
            column(name: "prod_flag", type: "varchar(" + 1 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "prod_batch_id", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "prod_date", type: "datetime",  remarks: "")   
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
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

    }
}