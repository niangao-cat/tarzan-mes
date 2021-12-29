package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_customer.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_customer") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_customer_s', startValue:"1")
        }
        createTable(tableName: "mt_customer", remarks: "客户") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "CUSTOMER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "唯一性主键标识")  {constraints(primaryKey: true)} 
            column(name: "CUSTOMER_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "客户编码")  {constraints(nullable:"false")}  
            column(name: "CUSTOMER_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "客户名称")  {constraints(nullable:"false")}  
            column(name: "CUSTOMER_NAME_ALT", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "客户简称")   
            column(name: "DATE_FROM", type: "datetime",  remarks: "生效时间")  {constraints(nullable:"false")}  
            column(name: "DATE_TO", type: "datetime",  remarks: "失效时间")   
            column(name: "CUSTOMER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "客户类型")   
            column(name: "SOURCE_IDENTIFICATION_ID", type: "decimal(36,6)",  remarks: "外部来源标识Id")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"CUSTOMER_CODE,TENANT_ID",tableName:"mt_customer",constraintName: "MT_CUSTOMER_U1")
    }
}