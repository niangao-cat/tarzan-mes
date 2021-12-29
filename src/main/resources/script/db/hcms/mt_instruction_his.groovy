package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_instruction_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_instruction_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_instruction_his_s', startValue:"1")
        }
        createTable(tableName: "mt_instruction_his", remarks: "仓储物流指令内容历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "INSTRUCTION_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID ,表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "INSTRUCTION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID ,表示唯一一条记录")  {constraints(nullable:"false")}  
            column(name: "INSTRUCTION_NUM", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "指令编号")  {constraints(nullable:"false")}  
            column(name: "SOURCE_INSTRUCTION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "来源指令ID")   
            column(name: "SOURCE_DOC_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "单据id，当业务过程中用到的是单据指令时使用。或者驱动控制组id")   
            column(name: "INSTRUCTION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "指令移动类型（从供应商接收，向供应商退货，发运给客户，从客户退货，工厂内部转移，组织间调拨，杂项发出，杂项接收、发出自站点、接收至站点）")  {constraints(nullable:"false")}  
            column(name: "INSTRUCTION_STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "指令状态（NEW，RELEASED，CANCEL，COMPLETED，COMPLETE_CANCEL）")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点id")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料ID")   
            column(name: "UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "单位")   
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "在制品 驱动对象是eo，没有物料id")   
            column(name: "DIS_ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "配送路线")   
            column(name: "ORDER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "订单类型（EO, NOTIFICATION,STOCKTAKE)")   
            column(name: "ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "订单ID")   
            column(name: "SOURCE_ORDER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "ERP订单类型（PO，SO）")   
            column(name: "SOURCE_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "ERP订单ID")   
            column(name: "SOURCE_ORDER_LINE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "订单行id")   
            column(name: "SOURCE_ORDER_LINE_LOCATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "订单发运行id（oracle的采购订单发运行id）")   
            column(name: "SOURCE_ORDER_LINE_DIST_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "订单分配行id（oracle的分配行id）")   
            column(name: "SOURCE_OUTSIDE_COMP_LINE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "外协组件行id")   
            column(name: "FROM_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "来源站点id")   
            column(name: "TO_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "目标站点id")   
            column(name: "FROM_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "来源库位id")   
            column(name: "TO_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "目标库位id")   
            column(name: "COST_CENTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "成本中心")   
            column(name: "QUANTITY", type: "decimal(36,6)",  remarks: "指令数量")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商（指令指定供应商时）")   
            column(name: "SUPPLIER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商地点id")   
            column(name: "CUSTOMER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户（指令指定客户）")   
            column(name: "CUSTOMER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户地点id")   
            column(name: "DEMAND_TIME", type: "datetime",  remarks: "需求日期")   
            column(name: "WAVE_SEQUENCE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "波次")   
            column(name: "SHIFT_DATE", type: "date",  remarks: "班次日期")   
            column(name: "SHIFT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "班次代码")   
            column(name: "COVER_QTY", type: "decimal(36,6)",  remarks: "覆盖数量")   
            column(name: "BUSINESS_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "业务类型")  {constraints(nullable:"false")}  
            column(name: "REMARK", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "备注")   
            column(name: "IDENTIFICATION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "实际业务需要的单据编号")   
            column(name: "FROM_OWNER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "所有者类型")   
            column(name: "TO_OWNER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "目标所有者类型")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"INSTRUCTION_ID,EVENT_ID,TENANT_ID",tableName:"mt_instruction_his",constraintName: "MT_INSTRUCTION_HIS_U1")
    }
}