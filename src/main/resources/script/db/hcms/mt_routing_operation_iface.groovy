package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_routing_operation_iface.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_routing_operation_iface") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_routing_operation_iface_s', startValue:"1")
        }
        createTable(tableName: "mt_routing_operation_iface", remarks: "工艺路线接口表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "IFACE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "ROUTER_OBJECT_TYPE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "ROUTER类型（物料工艺写入：MATERIAL，工单工艺写入：WO）")  {constraints(nullable:"false")}  
            column(name: "ROUTER_OBJECT_CODE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "ROUTER对象编码（物料编码或工单号）")  {constraints(nullable:"false")}  
            column(name: "PLANT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工厂编码")  {constraints(nullable:"false")}  
            column(name: "ROUTER_DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "ROUTER说明")  {constraints(nullable:"false")}  
            column(name: "ROUTER_CODE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "ROUTER编码")  {constraints(nullable:"false")}  
            column(name: "ROUTER_START_DATE", type: "datetime",  remarks: "ROUTER开始日期")  {constraints(nullable:"false")}  
            column(name: "ROUTER_END_DATE", type: "datetime",  remarks: "ROUTER结束日期")   
            column(name: "ROUTER_STATUS", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "ROUTER状态（Oracle可不写值，SAP将激活/未激活写入分别对应ACTIVE/UNACTIVE)")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "ROUTER有效性（Oracle可不写值，SAP将删除标记勾上的写入N）")   
            column(name: "ROUTING_ALTERNATE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "ROUTER版本（Oracle将ROUTER替代项写入，SAP将ROUTER计数器写入）")   
            column(name: "OPERATION_SEQ_NUM", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工序号")   
            column(name: "STANDARD_OPERATION_CODE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "标准工序编码")  {constraints(nullable:"false")}  
            column(name: "OPERATION_DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "工序描述")   
            column(name: "OPERATION_START_DATE", type: "datetime",  remarks: "工序开始日期")   
            column(name: "OPERATION_END_DATE", type: "datetime",  remarks: "工序结束日期")   
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

        addUniqueConstraint(columnNames:"ROUTER_OBJECT_TYPE,ROUTER_CODE,ROUTING_ALTERNATE,OPERATION_SEQ_NUM,BATCH_ID",tableName:"mt_routing_operation_iface",constraintName: "MT_ROUTING_OPERATION_IFACE_U1")
    }
}