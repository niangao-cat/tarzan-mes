package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_inv_journal.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_inv_journal") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_inv_journal_s', startValue:"1")
        }
        createTable(tableName: "mt_inv_journal", remarks: "库存日记账") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "JOURNAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "日记账ID")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料ID")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "库位ID")  {constraints(nullable:"false")}  
            column(name: "ONHAND_QUANTITY", type: "decimal(36,6)",  remarks: "库存现有量值")  {constraints(nullable:"false")}  
            column(name: "LOT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "批次CODE")   
            column(name: "CHANGE_QUANTITY", type: "decimal(36,6)",  remarks: "库存变化数量")  {constraints(nullable:"false")}  
            column(name: "OWNER_TYPE", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "所有者类型\r\n包含：\r\nCUSTOMER INVENTORY:客户库存\r\nORDER INVENTORY:销售订单库存\r\nSUPPLIER INVENTORY:供应商寄售\r\nINVENTORY IN SUPPLIER:带料外协，跟踪至发给供应商的库存\r\nPROJECT INVENTORY:按项目管理的库存\r\nINVENTORY IN CUSTOMER:销售寄售，按客户消耗结算\r\n空：表示自有")   
            column(name: "OWNER_ID", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "所有者ID（客户ID或供应商ID）")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "EVENT_TIME", type: "datetime",  remarks: "事件时间")  {constraints(nullable:"false")}  
            column(name: "EVENT_BY", type: "bigint(20)",  remarks: "创建人")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_inv_journal", indexName: "MT_INV_JOURNAL_N1") {
            column(name: "SITE_ID")
            column(name: "MATERIAL_ID")
            column(name: "LOCATOR_ID")
            column(name: "LOT_CODE")
            column(name: "EVENT_ID")
            column(name: "OWNER_TYPE")
            column(name: "OWNER_ID")
        }

    }
}