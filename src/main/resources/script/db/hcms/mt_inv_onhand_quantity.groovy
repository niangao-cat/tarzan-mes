package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_inv_onhand_quantity.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_inv_onhand_quantity") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_inv_onhand_quantity_s', startValue:"1")
        }
        createTable(tableName: "mt_inv_onhand_quantity", remarks: "库存量") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ONHAND_QUANTITY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料ID")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "库位ID")  {constraints(nullable:"false")}  
            column(name: "ONHAND_QUANTITY", type: "decimal(36,6)",  remarks: "库存现有量值")  {constraints(nullable:"false")}  
            column(name: "LOT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "批次CODE")   
            column(name: "OWNER_TYPE", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "所有者类型\r\n包含：\r\nCUSTOMER INVENTORY:客户库存\r\nORDER INVENTORY:销售订单库存\r\nSUPPLIER INVENTORY:供应商寄售\r\nINVENTORY IN SUPPLIER:带料外协，跟踪至发给供应商的库存\r\nPROJECT INVENTORY:按项目管理的库存\r\nINVENTORY IN CUSTOMER:销售寄售，按客户消耗结算\r\n空：表示自有")   
            column(name: "OWNER_ID", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "所有者ID（客户ID或供应商ID）")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,SITE_ID,MATERIAL_ID,LOCATOR_ID,LOT_CODE",tableName:"mt_inv_onhand_quantity",constraintName: "MT_INV_ONHAND_QUANTITY_U1")
    }
}