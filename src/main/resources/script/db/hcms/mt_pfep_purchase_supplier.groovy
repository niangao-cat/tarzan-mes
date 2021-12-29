package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_pfep_purchase_supplier.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_pfep_purchase_supplier") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_pfep_purchase_supplier_s', startValue:"1")
        }
        createTable(tableName: "mt_pfep_purchase_supplier", remarks: "物料供应商采购属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "PFEP_PURCHASE_SUPPLIER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID,标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料站点主键，标识唯一物料站点对应关系，限定为采购站点")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "组织类型，可选计划站点下区域、生产线、工作单元等类型")   
            column(name: "ORGANIZATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元")   
            column(name: "SUPPLIER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商，标识唯一供应商")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商地点，标识唯一供应商地点")   
            column(name: "RECEIVE_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "默认接收库位，物料采购时默认接收的目标库位")   
            column(name: "VISIBLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "优先级，标识物料在多个供应商上的选择顺序，数值越小优先级越高")   
            column(name: "ECONOMIC_LOT_SIZE", type: "decimal(36,6)",  remarks: "经济批量")   
            column(name: "ECONOMIC_SPLIT_PARAMETER", type: "decimal(36,6)",  remarks: "经济批量舍入值%")   
            column(name: "MIN_PACKAGE_QTY", type: "decimal(36,6)",  remarks: "物料采购时最小包装数量")   
            column(name: "MIN_PURCHASE_QTY", type: "decimal(36,6)",  remarks: "起订量，物料采购时最小数量")   
            column(name: "MAX_DAILY_SUPPLY_QTY", type: "decimal(36,6)",  remarks: "最大日供货量")   
            column(name: "SUPPLIER_CALENDAR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商日历")   
            column(name: "RELEASE_TIME_FENCE", type: "decimal(36,6)",  remarks: "下达时间，制单提前时间")   
            column(name: "AUTO_LOCK_TIME", type: "decimal(36,6)",  remarks: "自动计算锁定时间")   
            column(name: "LIMIT_HOUR", type: "decimal(36,6)",  remarks: "限制提前制单时间")   
            column(name: "PAST_DY_NOTIFY_CLOSE_TIME", type: "decimal(36,6)",  remarks: "过期送货通知关闭时间")   
            column(name: "PURCHASE_CYCLE", type: "decimal(36,6)",  remarks: "采购周期")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_pfep_purchase_supplier", indexName: "MT_PFEP_PURCHASE_SUPPLIER_N1") {
            column(name: "ENABLE_FLAG")
        }
   createIndex(tableName: "mt_pfep_purchase_supplier", indexName: "MT_PFEP_PURCHASE_SUPPLIER_N2") {
            column(name: "SUPPLIER_ID")
        }
   createIndex(tableName: "mt_pfep_purchase_supplier", indexName: "MT_PFEP_PURCHASE_SUPPLIER_N3") {
            column(name: "SUPPLIER_SITE_ID")
        }
   createIndex(tableName: "mt_pfep_purchase_supplier", indexName: "MT_PFEP_PURCHASE_SUPPLIER_N4") {
            column(name: "RECEIVE_LOCATOR_ID")
        }
   createIndex(tableName: "mt_pfep_purchase_supplier", indexName: "MT_PFEP_PURCHASE_SUPPLIER_N5") {
            column(name: "SUPPLIER_CALENDAR_ID")
        }

        addUniqueConstraint(columnNames:"MATERIAL_SITE_ID,ORGANIZATION_TYPE,ORGANIZATION_ID,SUPPLIER_ID,SUPPLIER_SITE_ID,TENANT_ID",tableName:"mt_pfep_purchase_supplier",constraintName: "MT_PFEP_PURCHASE_SUPPLIER_U1")
    }
}