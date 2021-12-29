package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_customer_iface.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_customer_iface") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_customer_iface_s', startValue:"1")
        }
        createTable(tableName: "mt_customer_iface", remarks: "客户数据接口表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "IFACE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "CUSTOMER_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户编号，Oracle将ACCOUNT_NUMBER写入")  {constraints(nullable:"false")}  
            column(name: "CUSTOMER_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "客户名称，Oracle将PARTY_NAME写入")  {constraints(nullable:"false")}  
            column(name: "CUSTOMER_NAME_ALT", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户简称")   
            column(name: "CUSTOMER_SITE_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"-1",   remarks: "客户地点编码")  {constraints(nullable:"false")}  
            column(name: "CUSTOMER_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户类型")   
            column(name: "CUSTOMER_DATE_FROM", type: "datetime",  remarks: "客户生效日期")  {constraints(nullable:"false")}  
            column(name: "CUSTOMER_DATE_TO", type: "datetime",  remarks: "客户失效日期")   
            column(name: "CUSTOMER_SITE_NUMBER", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户地点编号：oracle将PARTY_SITE_NUMBER写入该字段")   
            column(name: "SITE_USE_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "地点用途。Oracle存在bill_to 和ship_to两种")   
            column(name: "ADDRESS", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "客户地址：Oracle将HZ_location表的location写入该字段")   
            column(name: "COUNTRY", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "国家")   
            column(name: "PROVINCE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "省份")   
            column(name: "CITY", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "城市")   
            column(name: "CONTACT_PHONE_NUMBER", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "联系电话")   
            column(name: "CONTACT_PERSON", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "联系人")   
            column(name: "SITE_DATE_FROM", type: "datetime",  remarks: "地点生效日期")   
            column(name: "SITE_DATE_TO", type: "datetime",  remarks: "地点失效日期")   
            column(name: "ERP_CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP创建日期")  {constraints(nullable:"false")}  
            column(name: "ERP_CREATED_BY", type: "varchar(" + 20 * weight + ")",   defaultValue:"-1",   remarks: "ERP创建人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATED_BY", type: "varchar(" + 20 * weight + ")",   defaultValue:"-1",   remarks: "ERP最后更新人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP最后更新日期")  {constraints(nullable:"false")}  
            column(name: "BATCH_ID", type: "decimal(36,6)",  remarks: "数据批次ID")  {constraints(nullable:"false")}  
            column(name: "STATUS", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据处理状态，初始为N，失败为E，成功S，处理中P")  {constraints(nullable:"false")}  
            column(name: "MESSAGE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "数据处理消息返回")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE1", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE6", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,CUSTOMER_CODE,CUSTOMER_SITE_NUMBER,SITE_USE_TYPE,BATCH_ID",tableName:"mt_customer_iface",constraintName: "MT_CUSTOMER_IFACE_U1")
    }
}