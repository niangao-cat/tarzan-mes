package script.db

databaseChangeLog(logicalFilePath: 'script/db/wms_po_delivery_rel.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-wms_po_delivery_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'wms_po_delivery_rel_s', startValue:"1")
        }
        createTable(tableName: "wms_po_delivery_rel", remarks: "送货单行与采购订单行关系表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "po_delivery_rel_id", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "delivery_doc_id", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "送货单号（id）")  {constraints(nullable:"false")}  
            column(name: "delivery_doc_line_id", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "送货单行号(id)")  {constraints(nullable:"false")}  
            column(name: "po_id", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "采购订单号(id)")  {constraints(nullable:"false")}  
            column(name: "po_line_id", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "采购订单行号(id)")   
            column(name: "bom_type", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "cid", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "quantity", type: "int(20)",  remarks: "送货单行对应的采购订单数量")   
            column(name: "po_stock_in_qty", type: "decimal(20,6)",  remarks: "采购订单行入库数量")   

        }

    }
}