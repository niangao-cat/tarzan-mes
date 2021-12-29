package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_work_order_iface.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_work_order_iface") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_work_order_iface_s', startValue:"1")
        }
        createTable(tableName: "mt_work_order_iface", remarks: "工单接口表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "IFACE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "PLANT_CODE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "工厂代码")  {constraints(nullable:"false")}  
            column(name: "ITEM_CODE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "物料编码")  {constraints(nullable:"false")}  
            column(name: "WORK_ORDER_NUM", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工单号")  {constraints(nullable:"false")}  
            column(name: "QUANTITY", type: "decimal(36,6)",  remarks: "工单数量")  {constraints(nullable:"false")}  
            column(name: "WORK_ORDER_TYPE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "工单类型")  {constraints(nullable:"false")}  
            column(name: "WORK_ORDER_STATUS", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "工单状态")  {constraints(nullable:"false")}  
            column(name: "SCHEDULE_START_DATE", type: "datetime",  remarks: "NEW、RELEASED、HOLD、COMPLETED、CLOSED、ABANDON")  {constraints(nullable:"false")}  
            column(name: "SCHEDULE_END_DATE", type: "datetime",  remarks: "计划开始时间")  {constraints(nullable:"false")}  
            column(name: "COMPLETE_LOCATOR", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "完工库位")   
            column(name: "PROD_LINE_CODE", type: "varchar(" + 30 * weight + ")",  remarks: "产线代码")   
            column(name: "REMARK", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "备注")   
            column(name: "ERP_CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP创建日期")  {constraints(nullable:"false")}  
            column(name: "ERP_CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "ERP创建人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "ERP最后更新人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP最后更新日期")  {constraints(nullable:"false")}  
            column(name: "BATCH_ID", type: "decimal(36,6)",  remarks: "数据批次ID")  {constraints(nullable:"false")}  
            column(name: "STATUS", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据处理状态，初始为N，失败为E，成功S，处理中P")  {constraints(nullable:"false")}  
            column(name: "MESSAGE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "数据处理消息返回")   
            column(name: "CID", type: "bigint(20)",  remarks: "CID")   
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")   
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")   
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")   
            column(name: "ATTRIBUTE_CATEGORY", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE1", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE6", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE11", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE12", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE13", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE14", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE15", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   

        }

        addUniqueConstraint(columnNames:"PLANT_CODE,WORK_ORDER_NUM,BATCH_ID,TENANT_ID",tableName:"mt_work_order_iface",constraintName: "MT_WORK_ORDER_IFACE_U1")
    }
}