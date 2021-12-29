package script.db

databaseChangeLog(logicalFilePath: 'script/db/wms_object_transaction.groovy') {
    changeSet(author: "liyuan.lv@hand-china.com", id: "2020-05-20-wms_object_transaction") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'wms_object_transaction_s', startValue:"1")
        }
        createTable(tableName: "wms_object_transaction", remarks: "事务生成") {
            column(name: "TENANT_ID", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "TRANSACTION_ID", type: "varchar(" + 100 * weight + ")",  remarks: "事务ID")  {constraints(primaryKey: true)} 
            column(name: "TRANSACTION_TYPE_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "事务类型编码")  {constraints(nullable:"false")}  
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",  remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "BARCODE", type: "varchar(" + 255 * weight + ")",  remarks: "事务条码")   
            column(name: "PLANT_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "工厂编码")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "物料编码")  {constraints(nullable:"false")}  
            column(name: "TRANSACTION_QTY", type: "decimal(20,6)",  remarks: "事务数量")  {constraints(nullable:"false")}  
            column(name: "LOT_NUMBER", type: "varchar(" + 255 * weight + ")",  remarks: "批次号")   
            column(name: "DELIVERY_BATCH", type: "varchar(" + 255 * weight + ")",  remarks: "接收批")   
            column(name: "TRANSACTION_UOM", type: "varchar(" + 255 * weight + ")",  remarks: "事务单位")   
            column(name: "TRANSACTION_TIME", type: "datetime",  remarks: "事务时间")  {constraints(nullable:"false")}  
            column(name: "TRANSACTION_REASON_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "事务原因")   
            column(name: "ACCOUNT_DATE", type: "datetime",  remarks: "记账日期")   
            column(name: "WAREHOUSE_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "仓库编码")   
            column(name: "LOCATOR_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "货位编码")   
            column(name: "TRANSFER_PLANT_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "目标工厂编码")   
            column(name: "TRANSFER_WAREHOUSE_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "目标仓库编码")   
            column(name: "TRANSFER_LOCATOR_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "目标货位编码")   
            column(name: "COSTCENTER_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "成本中心编码")   
            column(name: "SUPPLIER_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "供应商编码")   
            column(name: "SUPPLIER_SITE_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "供应商地点编码")   
            column(name: "CUSTOMER_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "客户编码")   
            column(name: "CUSTOMER_SITE_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "客户地点编码")   
            column(name: "SOURCE_DOC_TYPE", type: "varchar(" + 255 * weight + ")",  remarks: "来源单据类型")   
            column(name: "SOURCE_DOC_NUM", type: "varchar(" + 255 * weight + ")",  remarks: "来源单据号")   
            column(name: "SOURCE_DOC_LINE_NUM", type: "varchar(" + 255 * weight + ")",  remarks: "来源单据行号")   
            column(name: "WORK_ORDER_NUM", type: "varchar(" + 255 * weight + ")",  remarks: "工单号")   
            column(name: "OPERATION_SEQUENCE", type: "varchar(" + 255 * weight + ")",  remarks: "工序号")   
            column(name: "PROD_LINE_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "生产线编码")
            column(name: "MERGE_FLAG", type: "varchar(" + 1 * weight + ")",  remarks: "是否合并")  {constraints(nullable:"false")}  
            column(name: "MERGE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "接口汇总的数据ID")
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE1", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE6", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE11", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE12", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE13", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE14", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE15", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE16", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE17", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE18", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE19", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE20", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE21", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE22", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE23", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE24", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE25", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE26", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE27", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE28", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE29", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE30", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE31", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE32", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE33", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE34", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE35", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE36", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE37", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE38", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE39", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE40", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "SOURCE_DOC_ID", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "SOURCE_DOC_LINE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "TRANSFER_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "PLANT_ID", type: "varchar(" + 100 * weight + ")",  remarks: "")   
            column(name: "REMARK", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "SALE_DOC_ID", type: "varchar(" + 100 * weight + ")",  remarks: "销售订单id")   
            column(name: "SALE_DOC_LINE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "销售订单行id")   
            column(name: "TRANSFER_SALE_DOC_ID", type: "varchar(" + 100 * weight + ")",  remarks: "目标销售订单id")   
            column(name: "TRANSFER_SALE_DOC_LINE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "目标销售订单行id")   
            column(name: "INSIDE_DOC_NUM", type: "varchar(" + 100 * weight + ")",  remarks: "内部订单号")   
            column(name: "MAKE_ORDER_NUM", type: "varchar(" + 100 * weight + ")",  remarks: "制造订单号")   
            column(name: "OWNER_TYPE", type: "varchar(" + 30 * weight + ")",  remarks: "所有者类型")   
            column(name: "WAREHOUSE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "仓库Id")   
            column(name: "TRANSFER_WAREHOUSE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "目标仓库Id")   
            column(name: "TRANSFER_PLANT_ID", type: "varchar(" + 100 * weight + ")",  remarks: "目标工厂Id")   

        }

    }
}