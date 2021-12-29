package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_stocktake_actual_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_stocktake_actual_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_stocktake_actual_his_s', startValue:"1")
        }
        createTable(tableName: "mt_stocktake_actual_his", remarks: "盘点实绩历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "STOCKTAKE_ACTUAL_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "盘点指令实历史ID")  {constraints(primaryKey: true)} 
            column(name: "STOCKTAKE_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "盘点指令实绩ID")  {constraints(nullable:"false")}  
            column(name: "STOCKTAKE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "单据ID")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批ID")  {constraints(nullable:"false")}  
            column(name: "LOT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "批次CODE")   
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料ID")   
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "存储类型的货位ID")   
            column(name: "CONTAINER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批对应的容器ID（最上层）")   
            column(name: "OWNER_TYPE", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "所有者类型")   
            column(name: "OWNER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "所有者类型ID")   
            column(name: "RESERVED_OBJECT_TYPE", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "预留对象类型")   
            column(name: "RESERVED_OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "预留对象")   
            column(name: "UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批主单位ID")   
            column(name: "CURRENT_QUANTITY", type: "decimal(36,6)",  remarks: "物料账上数量")   
            column(name: "FIRSTCOUNT_MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "初盘物料ID")   
            column(name: "FIRSTCOUNT_UOM_ID", type: "varchar(" + 100 * weight + ")",  remarks: "初盘主单位ID")   
            column(name: "FIRSTCOUNT_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "初盘货位ID")   
            column(name: "FIRSTCOUNT_CONTAINER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "初盘容器ID")   
            column(name: "FIRSTCOUNT_LOCATION_ROW", type: "bigint(20)",  remarks: "初盘容器装载物料对应行")   
            column(name: "FIRSTCOUNT_LOCATION_COLUMN", type: "bigint(20)",  remarks: "初盘容器装载物料对应列")   
            column(name: "FIRSTCOUNT_OWNER_TYPE", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "初盘所有者类型")   
            column(name: "FIRSTCOUNT_OWNER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "初盘所有者ID")   
            column(name: "FIRSTCOUNT_RESERVED_OBJECT_TY", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "初盘预留类型")   
            column(name: "FIRSTCOUNT_RESERVED_OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "初盘预留对象ID")   
            column(name: "FIRSTCOUNT_QUANTITY", type: "decimal(36,6)",  remarks: "初盘数量，物料批主单位下的数量")   
            column(name: "FIRSTCOUNT_REMARK", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "初盘备注")   
            column(name: "RECOUNT_MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "复盘物料ID")   
            column(name: "RECOUNT_UOM_ID", type: "varchar(" + 100 * weight + ")",  remarks: "复盘主单位ID")   
            column(name: "RECOUNT_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "复盘货位ID")   
            column(name: "RECOUNT_CONTAINER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "复盘容器ID")   
            column(name: "RECOUNT_LOCATION_ROW", type: "bigint(20)",  remarks: "复盘容器装载物料对应行")   
            column(name: "RECOUNT_LOCATION_COLUMN", type: "bigint(20)",  remarks: "复盘容器装载物料对应列")   
            column(name: "RECOUNT_OWNER_TYPE", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "复盘所有者类型")   
            column(name: "RECOUNT_OWNER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "复盘所有者ID")   
            column(name: "RECOUNT_RESERVED_OBJECT_TYPE", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "复盘预留对象类型")   
            column(name: "RECOUNT_RESERVED_OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "复盘预留对象")   
            column(name: "RECOUNT_QUANTITY", type: "decimal(36,6)",  remarks: "复盘数量，物料批主单位下的数量")   
            column(name: "RECOUNT_REMARK", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "复盘备注")   
            column(name: "ADJUST_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "调整标识")   
            column(name: "EVENT_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"STOCKTAKE_ACTUAL_ID,EVENT_ID,TENANT_ID",tableName:"mt_stocktake_actual_his",constraintName: "MT_STOCKTAKE_ACTUAL_HIS_U1")
    }
}