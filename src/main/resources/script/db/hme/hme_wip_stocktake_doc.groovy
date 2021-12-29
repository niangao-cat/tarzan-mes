package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_wip_stocktake_doc.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2021-03-03-hme_wip_stocktake_doc") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_wip_stocktake_doc_s', startValue:"1")
        }
        createTable(tableName: "hme_wip_stocktake_doc", remarks: "在制盘点单") {
            column(name: "stocktake_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "stocktake_num", type: "varchar(" + 100 * weight + ")",  remarks: "在制盘点单据编号")  {constraints(nullable:"false")}  
            column(name: "stocktake_status", type: "varchar(" + 30 * weight + ")",  remarks: "状态")  {constraints(nullable:"false")}  
            column(name: "stocktake_last_status", type: "varchar(" + 30 * weight + ")",  remarks: "上一状态，用于状态发生变更时找到上一状态的结果")   
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "area_id", type: "varchar(" + 100 * weight + ")",  remarks: "部门的区域ID，用于用户查看盘点当前区域")  {constraints(nullable:"false")}  
            column(name: "open_flag", type: "varchar(" + 10 * weight + ")",  remarks: "是否明盘，Y为明盘，N为盲盘")  {constraints(nullable:"false")}  
            column(name: "material_range_flag", type: "varchar(" + 10 * weight + ")",  remarks: "是否按物料盘点，Y/N，不能为空")  {constraints(nullable:"false")}  
            column(name: "adjust_timely_flag", type: "varchar(" + 10 * weight + ")",  remarks: "是否允许实时调整，Y/N，不能为空")  {constraints(nullable:"false")}  
            column(name: "material_lot_lock_flag", type: "varchar(" + 10 * weight + ")",  remarks: "物料批停用标识，Y/N，不能为空")  {constraints(nullable:"false")}  
            column(name: "identification", type: "varchar(" + 100 * weight + ")",  remarks: "单据条码")   
            column(name: "remark", type: "varchar(" + 255 * weight + ")",  remarks: "备注")   
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
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
   createIndex(tableName: "hme_wip_stocktake_doc", indexName: "hme_wip_stocktake_doc_n1") {
            column(name: "site_id")
        }
   createIndex(tableName: "hme_wip_stocktake_doc", indexName: "hme_wip_stocktake_doc_n2") {
            column(name: "area_id")
        }

        addUniqueConstraint(columnNames:"stocktake_num,tenant_id",tableName:"hme_wip_stocktake_doc",constraintName: "hme_wip_stocktake_doc_u1")
    }
}