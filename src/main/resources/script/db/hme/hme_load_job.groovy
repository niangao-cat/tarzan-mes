package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_load_job.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2021-02-01-hme_load_job") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_load_job_s', startValue:"1")
        }
        createTable(tableName: "hme_load_job", remarks: "装载信息作业记录表") {
            column(name: "load_job_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "工厂id")  {constraints(nullable:"false")}  
            column(name: "load_sequence", type: "varchar(" + 100 * weight + ")",  remarks: "装载行序列号")  {constraints(nullable:"false")}  
            column(name: "load_job_type", type: "varchar(" + 100 * weight + ")",  remarks: "作业类型")  {constraints(nullable:"false")}  
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "cos芯片物料")   
            column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "条码id")   
            column(name: "load_row", type: "bigint(20)",  remarks: "行")   
            column(name: "load_column", type: "bigint(20)",  remarks: "列")   
            column(name: "source_material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "来源条码id")   
            column(name: "source_load_row", type: "bigint(20)",  remarks: "来源行")   
            column(name: "source_load_column", type: "bigint(20)",  remarks: "来源列")   
            column(name: "cos_num", type: "bigint(20)",  remarks: "芯片数")   
            column(name: "hot_sink_code", type: "varchar(" + 100 * weight + ")",  remarks: "热沉编码")   
            column(name: "status", type: "varchar(" + 100 * weight + ")",  remarks: "状态")   
            column(name: "nc_code_id", type: "varchar(" + 100 * weight + ")",  remarks: "不良代码id")   
            column(name: "operation_id", type: "varchar(" + 100 * weight + ")",  remarks: "工艺id")   
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位id")   
            column(name: "work_order_id", type: "varchar(" + 100 * weight + ")",  remarks: "工单id")   
            column(name: "wafer_num", type: "varchar(" + 100 * weight + ")",  remarks: "wafer")   
            column(name: "cos_type", type: "varchar(" + 100 * weight + ")",  remarks: "cos类型")   
            column(name: "remark", type: "varchar(" + 100 * weight + ")",  remarks: "备注")   
            column(name: "bom_material_id", type: "varchar(" + 100 * weight + ")",  remarks: "投料物料id")   
            column(name: "bom_material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "投料物料条码id")   
            column(name: "bom_material_lot_supplier", type: "varchar(" + 100 * weight + ")",  remarks: "投料物料条码供应商")   
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
   createIndex(tableName: "hme_load_job", indexName: "hme_load_job_n1") {
            column(name: "material_id")
        }
   createIndex(tableName: "hme_load_job", indexName: "hme_load_job_n2") {
            column(name: "material_lot_id")
        }

    }
}