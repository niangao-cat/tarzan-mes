package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_instruction_actual.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_instruction_actual") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_instruction_actual_s', startValue:"1")
        }
        createTable(tableName: "mt_instruction_actual", remarks: "指令实绩表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "指令实绩汇总id")  {constraints(primaryKey: true)} 
            column(name: "INSTRUCTION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "来源指令id")   
            column(name: "INSTRUCTION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "指令移动类型（从供应商接收，向供应商退货，发运给客户，从客户退货，工厂内部转移，组织间调拨，杂项发出，杂项接收、发出自站点、接收至站点）")  {constraints(nullable:"false")}  
            column(name: "BUSINESS_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "业务类型")   
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料id")   
            column(name: "UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "单位")   
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "eoid")   
            column(name: "SOURCE_ORDER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "来源订单类型（PO，SO）")   
            column(name: "SOURCE_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "来源订单ID")   
            column(name: "FROM_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "来源站点id")   
            column(name: "TO_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "目标站点id")   
            column(name: "FROM_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "来源库位id")   
            column(name: "TO_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "目标库位id")   
            column(name: "FROM_OWNER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "所有者类型")   
            column(name: "TO_OWNER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "目标所有者类型")   
            column(name: "COST_CENTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "成本中心")   
            column(name: "SUPPLIER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商（指令指定供应商时）")   
            column(name: "SUPPLIER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商地点id")   
            column(name: "CUSTOMER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户（指令指定客户）")   
            column(name: "CUSTOMER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户地点id")   
            column(name: "ACTUAL_QTY", type: "decimal(36,6)",  remarks: "实绩数量")  {constraints(nullable:"false")}  
            column(name: "LATEST_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "最新一次新增历史表的主键")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}