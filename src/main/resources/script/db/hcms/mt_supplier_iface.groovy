package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_supplier_iface.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_supplier_iface") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_supplier_iface_s', startValue:"1")
        }
        createTable(tableName: "mt_supplier_iface", remarks: "供应商数据接口表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "IFACE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "SUPPLIER_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商代码")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "供应商名称")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_NAME_ALT", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "供应商简称")   
            column(name: "SUPPLIER_DATE_FROM", type: "datetime",  remarks: "生效日期从")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_DATE_TO", type: "datetime",  remarks: "失效日期至")   
            column(name: "SUPPLIER_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商类型")   
            column(name: "SUPPLIER_SITE_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商地点编号")   
            column(name: "SUPPLIER_SITE_ADDRESS", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "供应商详细地址")  {constraints(nullable:"false")}  
            column(name: "COUNTRY", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "国家")   
            column(name: "PROVINCE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "省份")   
            column(name: "CITY", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "城市")   
            column(name: "CONTACT_PHONE_NUMBER", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "联系电话")   
            column(name: "CONTACT_PERSON", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "联系人")   
            column(name: "SITE_DATE_FROM", type: "datetime",  remarks: "地点生效日期")  {constraints(nullable:"false")}  
            column(name: "SITE_DATE_TO", type: "datetime",  remarks: "地点失效日期")   
            column(name: "ERP_CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP创建日期")  {constraints(nullable:"false")}  
            column(name: "ERP_CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "ERP创建人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "ERP最后更新人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP最后更新日期")  {constraints(nullable:"false")}  
            column(name: "BATCH_ID", type: "decimal(36,6)",  remarks: "数据批次ID")  {constraints(nullable:"false")}  
            column(name: "STATUS", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据处理状态，初始为N，失败为E，成功S，处理中P")  {constraints(nullable:"false")}  
            column(name: "MESSAGE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "数据处理消息返回")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,SUPPLIER_CODE,SUPPLIER_SITE_CODE,BATCH_ID",tableName:"mt_supplier_iface",constraintName: "MT_SUPPLIER_IFACE_U1")
    }
}