package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_wip_stocktake_actual.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2021-03-03-hme_wip_stocktake_actual") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_wip_stocktake_actual_s', startValue:"1")
        }
        createTable(tableName: "hme_wip_stocktake_actual", remarks: "在制盘点实际") {
            column(name: "stocktake_actual_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "stocktake_id", type: "varchar(" + 100 * weight + ")",  remarks: "盘点单ID")  {constraints(nullable:"false")}  
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料批ID")  {constraints(nullable:"false")}  
            column(name: "lot_code", type: "varchar(" + 100 * weight + ")",  remarks: "批次CODE")   
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料ID")   
            column(name: "work_order_id", type: "varchar(" + 100 * weight + ")",  remarks: "在制品的工单ID")   
            column(name: "prod_line_id", type: "varchar(" + 100 * weight + ")",  remarks: "在制品的产线ID")   
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "在制品的工序ID")   
            column(name: "container_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料批对应的容器ID（最上层）")   
            column(name: "owner_type", type: "varchar(" + 30 * weight + ")",  remarks: "所有者类型")   
            column(name: "owner_id", type: "varchar(" + 100 * weight + ")",  remarks: "所有者类型ID")   
            column(name: "reserved_object_type", type: "varchar(" + 30 * weight + ")",  remarks: "预留对象类型")   
            column(name: "reserved_object_id", type: "varchar(" + 100 * weight + ")",  remarks: "预留对象")   
            column(name: "uom_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料批主单位ID")   
            column(name: "current_quantity", type: "decimal(36,6)",  remarks: "物料账上数量")   
            column(name: "firstcount_material_id", type: "varchar(" + 100 * weight + ")",  remarks: "初盘物料ID")   
            column(name: "firstcount_uom_id", type: "varchar(" + 100 * weight + ")",  remarks: "初盘主单位ID")   
            column(name: "firstcount_prod_line_id", type: "varchar(" + 100 * weight + ")",  remarks: "初盘产线ID")   
            column(name: "firstcount_workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "初盘工序ID")   
            column(name: "firstcount_container_id", type: "varchar(" + 100 * weight + ")",  remarks: "初盘容器ID")   
            column(name: "firstcount_location_row", type: "bigint(20)",  remarks: "初盘容器装载物料对应行")   
            column(name: "firstcount_location_column", type: "bigint(20)",  remarks: "初盘容器装载物料对应列")   
            column(name: "firstcount_owner_type", type: "varchar(" + 30 * weight + ")",  remarks: "初盘所有者类型")   
            column(name: "firstcount_owner_id", type: "varchar(" + 100 * weight + ")",  remarks: "初盘所有者ID")   
            column(name: "firstcount_reserved_object_ty", type: "varchar(" + 30 * weight + ")",  remarks: "初盘预留类型")   
            column(name: "firstcount_reserved_object_id", type: "varchar(" + 100 * weight + ")",  remarks: "初盘预留对象ID")   
            column(name: "firstcount_quantity", type: "decimal(36,6)",  remarks: "初盘数量，物料批主单位下的数量")   
            column(name: "firstcount_by", type: "bigint(20)",  remarks: "初盘当前用户id")   
            column(name: "firstcount_date", type: "datetime",  remarks: "当前初盘时间")   
            column(name: "firstcount_remark", type: "varchar(" + 255 * weight + ")",  remarks: "初盘备注")   
            column(name: "recount_material_id", type: "varchar(" + 100 * weight + ")",  remarks: "复盘物料ID")   
            column(name: "recount_uom_id", type: "varchar(" + 100 * weight + ")",  remarks: "复盘主单位ID")   
            column(name: "recount_prod_line_id", type: "varchar(" + 100 * weight + ")",  remarks: "复盘产线ID")   
            column(name: "recount_workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "复盘工序ID")   
            column(name: "recount_container_id", type: "varchar(" + 100 * weight + ")",  remarks: "复盘容器ID")   
            column(name: "recount_location_row", type: "bigint(20)",  remarks: "复盘容器装载物料对应行")   
            column(name: "recount_location_column", type: "bigint(20)",  remarks: "复盘容器装载物料对应列")   
            column(name: "recount_owner_type", type: "varchar(" + 30 * weight + ")",  remarks: "复盘所有者类型")   
            column(name: "recount_owner_id", type: "varchar(" + 100 * weight + ")",  remarks: "复盘所有者ID")   
            column(name: "recount_reserved_object_type", type: "varchar(" + 30 * weight + ")",  remarks: "复盘预留对象类型")   
            column(name: "recount_reserved_object_id", type: "varchar(" + 100 * weight + ")",  remarks: "复盘预留对象")   
            column(name: "recount_quantity", type: "decimal(36,6)",  remarks: "复盘数量，物料批主单位下的数量")   
            column(name: "recount_by", type: "bigint(20)",  remarks: "复盘当前用户id")   
            column(name: "recount_date", type: "datetime",  remarks: "当前复盘时间")   
            column(name: "recount_remark", type: "varchar(" + 255 * weight + ")",  remarks: "复盘备注")   
            column(name: "adjust_flag", type: "varchar(" + 10 * weight + ")",  remarks: "调整标识")   
            column(name: "latest_his_id", type: "varchar(" + 100 * weight + ")",  remarks: "最近历史ID")   
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
   createIndex(tableName: "hme_wip_stocktake_actual", indexName: "hme_wip_stocktake_actual_n1") {
            column(name: "site_id")
        }
   createIndex(tableName: "hme_wip_stocktake_actual", indexName: "hme_wip_stocktake_actual_n2") {
            column(name: "material_lot_id")
        }
   createIndex(tableName: "hme_wip_stocktake_actual", indexName: "hme_wip_stocktake_actual_n3") {
            column(name: "work_order_id")
        }
   createIndex(tableName: "hme_wip_stocktake_actual", indexName: "hme_wip_stocktake_actual_n4") {
            column(name: "prod_line_id")
        }
   createIndex(tableName: "hme_wip_stocktake_actual", indexName: "hme_wip_stocktake_actual_n5") {
            column(name: "workcell_id")
        }

        addUniqueConstraint(columnNames:"stocktake_id,material_lot_id",tableName:"hme_wip_stocktake_actual",constraintName: "hme_wip_stocktake_actual_u1")
    }
}