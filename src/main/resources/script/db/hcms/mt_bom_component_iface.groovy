package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_bom_component_iface.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_bom_component_iface") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_bom_component_iface_s', startValue:"1")
        }
        createTable(tableName: "mt_bom_component_iface", remarks: "BOM接口表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "IFACE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "PLANT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工厂编码")  {constraints(nullable:"false")}  
            column(name: "BOM_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "BOM编码")  {constraints(nullable:"false")}  
            column(name: "BOM_ALTERNATE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "BOM版本（Oracle将BOM替代项写入，SAP将BOM计数器写入）")   
            column(name: "BOM_DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "BOM说明")  {constraints(nullable:"false")}  
            column(name: "BOM_START_DATE", type: "datetime",  remarks: "BOM开始日期")  {constraints(nullable:"false")}  
            column(name: "BOM_END_DATE", type: "datetime",  remarks: "BOM结束日期")   
            column(name: "BOM_STATUS", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "BOM状态（Oracle可不写值，SAP将激活/未激活写入分别对应ACTIVE/UNACTIVE)")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "BOM有效性（Oracle可不写值，SAP将删除标记勾上的写入N）")   
            column(name: "BOM_OBJECT_TYPE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "BOM类型（物料BOM写入：MATERIAL，工单BOM写入：WO）")  {constraints(nullable:"false")}  
            column(name: "BOM_OBJECT_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "BOM对象编码（物料编码或工单号）")  {constraints(nullable:"false")}  
            column(name: "STANDARD_QTY", type: "decimal(36,6)",  remarks: "基准数量（Oracle物料BOM写入1，工单BOM可直接写入工单数量）")  {constraints(nullable:"false")}  
            column(name: "COMPONENT_LINE_NUM", type: "bigint(20)",  remarks: "组件行号")  {constraints(nullable:"false")}  
            column(name: "COMPONENT_ITEM_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "组件物料编码")  {constraints(nullable:"false")}  
            column(name: "OPERATION_SEQUENCE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "工序号")   
            column(name: "BOM_USAGE", type: "decimal(36,6)",  remarks: "组件单位用量")  {constraints(nullable:"false")}  
            column(name: "COMPONENT_SHRINKAGE", type: "decimal(36,6)",  remarks: "组件损耗率")   
            column(name: "WIP_SUPPLY_TYPE", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "组件发料类型（1推式，2，3拉式，6虚拟件）")   
            column(name: "COMPONENT_START_DATE", type: "datetime",  remarks: "组件开始日期")  {constraints(nullable:"false")}  
            column(name: "COMPONENT_END_DATE", type: "datetime",  remarks: "组件结束日期")   
            column(name: "SUBSTITUTE_ITEM_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "替代组件物料编码")   
            column(name: "SUBSTITUTE_ITEM_USAGE", type: "decimal(36,6)",  remarks: "替代组件单位用量")   
            column(name: "SUBSTITUTE_GROUP", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "替代组")   
            column(name: "ERP_CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP创建日期")  {constraints(nullable:"false")}  
            column(name: "ERP_CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "ERP创建人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "ERP最后更新人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP最后更新日期")  {constraints(nullable:"false")}  
            column(name: "ISSUE_LOCATOR_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "")      
            column(name: "ASSEMBLE_METHOD", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "BOM_COMPONENT_TYPE", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "BATCH_ID", type: "decimal(36,6)",  remarks: "数据批次ID")  {constraints(nullable:"false")}  
            column(name: "STATUS", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据处理状态")  {constraints(nullable:"false")}  
            column(name: "MESSAGE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "数据处理消息")   
            column(name: "UPDATE_METHOD", type: "varchar(" + 255 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(20)",  remarks: "CID")   
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")   
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")   
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")   
            column(name: "ATTRIBUTE_CATEGORY", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE1", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE2", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE3", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE4", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE5", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE6", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE7", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE8", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE9", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE10", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE11", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE12", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE13", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE14", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "HEAD_ATTRIBUTE15", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE1", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE2", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE3", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE4", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE5", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE6", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE7", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE8", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE9", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE10", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE11", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE12", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE13", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE14", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "LINE_ATTRIBUTE15", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   

        }

        addUniqueConstraint(columnNames:"BOM_OBJECT_TYPE,BOM_CODE,BOM_ALTERNATE,COMPONENT_LINE_NUM,BATCH_ID",tableName:"mt_bom_component_iface",constraintName: "MT_BOM_COMPONENT_IFACE_U1")
    }
}