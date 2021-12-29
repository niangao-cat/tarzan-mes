package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_inv_onhand_hold_journal.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_inv_onhand_hold_journal") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_inv_onhand_hold_journal_s', startValue:"1")
        }
        createTable(tableName: "mt_inv_onhand_hold_journal", remarks: "库存保留日记账") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ONHAND_HOLD_JOURNAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID")  {constraints(primaryKey: true)} 
            column(name: "ONHAND_HOLD_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "库存保留主键ID")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料ID")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "库位ID")  {constraints(nullable:"false")}  
            column(name: "LOT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "批次CODE")   
            column(name: "HOLD_QUANTITY", type: "decimal(36,6)",  remarks: "保留数量")  {constraints(nullable:"false")}  
            column(name: "HOLD_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "预留类型（手工/指令）")  {constraints(nullable:"false")}  
            column(name: "ORDER_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "指令类型")   
            column(name: "ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "指令ID")   
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

        addUniqueConstraint(columnNames:"ONHAND_HOLD_ID,EVENT_ID,TENANT_ID",tableName:"mt_inv_onhand_hold_journal",constraintName: "MT_INV_ONHAND_HOLD_JOURNAL_U1")
    }
}