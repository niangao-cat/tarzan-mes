package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_operation_component_iface.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_operation_component_iface") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_operation_component_iface_s', startValue:"1")
        }
        createTable(tableName: "mt_operation_component_iface", remarks: "工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "IFACE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "PLANT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工厂编码")  {constraints(nullable:"false")}  
            column(name: "ROUTER_OBJECT_TYPE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "ROUTER类型（物料工艺写入：MATERIAL，工单工艺写入：WO）")  {constraints(nullable:"false")}  
            column(name: "ROUTER_CODE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "ROUTER编码")   
            column(name: "ROUTER_ALTERNATE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "ROUTER版本（Oracle将ROUTER替代项写入，SAP将ROUTER计数器写入）")   
            column(name: "BOM_OBJECT_TYPE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "BOM类型（物料BOM写入：MATERIAL，工单BOM写入：WO）")  {constraints(nullable:"false")}  
            column(name: "BOM_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "BOM编码")   
            column(name: "BOM_ALTERNATE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "BOM版本（Oracle将BOM替代项写入，SAP将BOM计数器写入）")   
            column(name: "LINE_NUM", type: "bigint(20)",  remarks: "组件行号")  {constraints(nullable:"false")}  
            column(name: "OPERATION_SEQ_NUM", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "工序号")  {constraints(nullable:"false")}  
            column(name: "COMPONENT_ITEM_NUM", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "组件物料编码")   
            column(name: "BOM_USAGE", type: "decimal(36,6)",  remarks: "组件单位用量")   
            column(name: "UOM", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "组件单位用量")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "工序分配组件有效性（Oracle可不写值，SAP将删除标记勾上的写入N）")   
            column(name: "COMPONENT_START_DATE", type: "datetime",  remarks: "组件开始日期")  {constraints(nullable:"false")}  
            column(name: "COMPONENT_END_DATE", type: "datetime",  remarks: "组件结束日期")   
            column(name: "ERP_CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP创建日期")  {constraints(nullable:"false")}  
            column(name: "ERP_CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "ERP创建人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "ERP最后更新人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP最后更新日期")  {constraints(nullable:"false")}  
            column(name: "BATCH_ID", type: "decimal(36,6)",  remarks: "数据批次ID")  {constraints(nullable:"false")}  
            column(name: "STATUS", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据处理状态")  {constraints(nullable:"false")}  
            column(name: "MESSAGE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "数据处理消息")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ROUTER_OBJECT_TYPE,ROUTER_CODE,ROUTER_ALTERNATE,OPERATION_SEQ_NUM,BOM_OBJECT_TYPE,BOM_CODE,BOM_ALTERNATE,LINE_NUM,BATCH_ID",tableName:"mt_operation_component_iface",constraintName: "MT_OPERATION_COMP_IFACE_U1")
    }
}