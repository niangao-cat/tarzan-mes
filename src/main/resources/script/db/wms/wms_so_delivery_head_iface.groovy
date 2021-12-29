package script.db

databaseChangeLog(logicalFilePath: 'script/db/wms_so_delivery_head_iface.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-wms_so_delivery_head_iface") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'wms_so_delivery_head_iface_s', startValue:"1")
        }
        createTable(tableName: "wms_so_delivery_head_iface", remarks: "出货单头接口表") {
            column(name: "IFACE_ID", type: "bigint(100)", autoIncrement: true ,   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "INSTRUCTION_DOC_NUM", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "出货单编码")   
            column(name: "INSTRUCTION_DOC_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "单据类型")   
            column(name: "INSTRUCTION_DOC_STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "出货单状态")   
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")   
            column(name: "SITE_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工厂代码")   
            column(name: "CUSTOMER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户ID")   
            column(name: "CUSTOMER_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "客户编码")   
            column(name: "CUSTOMER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户地点ID")   
            column(name: "CUSTOMER_SITE_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "客户地点编码")   
            column(name: "DEMAND_TIME", type: "datetime",  remarks: "需求时间")   
            column(name: "EXPECTED_ARRIVAL_TIME", type: "datetime",  remarks: "预计送达时间")   
            column(name: "REMARK", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "备注")   
            column(name: "REASON", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "原因")   
            column(name: "INVOICE_NUM", type: "varchar(" + 255 * weight + ")",  remarks: "发票号")   
            column(name: "STATUS", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "状态")   
            column(name: "SHIP_TO", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "送货地址")   
            column(name: "CREDIT", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "信用评级")   
            column(name: "CUSTOMER_TEST_FLAG", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "是否需要客验")   
            column(name: "MESSAGE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "消息")   
            column(name: "TENANT_ID", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE1", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE6", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE11", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE12", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE13", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE14", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "ATTRIBUTE15", type: "varchar(" + 400 * weight + ")",  remarks: "预留扩展字段")   
            column(name: "SYSTEAM", type: "varchar(" + 20 * weight + ")",  remarks: "")   
            column(name: "PACK_EXIST_FLAG", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "SHIP_METHOD", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "LCL_FLAG", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "LCL_BASE", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "CUSTOMER_ACCEPT_FLAG", type: "varchar(" + 100 * weight + ")",  remarks: "")   

        }

    }
}