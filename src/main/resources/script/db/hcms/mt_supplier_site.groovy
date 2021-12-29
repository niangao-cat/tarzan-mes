package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_supplier_site.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_supplier_site") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_supplier_site_s', startValue:"1")
        }
        createTable(tableName: "mt_supplier_site", remarks: "供应商地点") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "唯一性主键标识")  {constraints(primaryKey: true)} 
            column(name: "SUPPLIER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商ID")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_SITE_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "供应商地点编码")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_SITE_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "供应商地点名称")  {constraints(nullable:"false")}  
            column(name: "COUNTRY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "国家")   
            column(name: "PROVINCE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "省")   
            column(name: "CITY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "市")   
            column(name: "COUNTY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "区")   
            column(name: "ADDRESS", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "详细地址")   
            column(name: "PHONE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "电话")   
            column(name: "PERSON", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "联系人")   
            column(name: "DATE_FROM", type: "date",  remarks: "生效时间")  {constraints(nullable:"false")}  
            column(name: "DATE_TO", type: "date",  remarks: "失效时间")   
            column(name: "SOURCE_IDENTIFICATION_ID", type: "decimal(36,6)",  remarks: "外部来源标识Id")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"SUPPLIER_SITE_CODE,SUPPLIER_ID,TENANT_ID",tableName:"mt_supplier_site",constraintName: "MT_SUPPLIER_SITE_U1")
    }
}