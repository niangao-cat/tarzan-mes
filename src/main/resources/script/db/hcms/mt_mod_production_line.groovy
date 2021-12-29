package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_production_line.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_production_line") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_production_line_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_production_line", remarks: "生产线") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "PROD_LINE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID ,表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "PROD_LINE_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "生产线编号")  {constraints(nullable:"false")}  
            column(name: "PROD_LINE_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "生产线名称")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "生产线描述")   
            column(name: "PROD_LINE_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "生产线类型，区分生产线类型为自有、外协或是采购")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商ID")   
            column(name: "SUPPLIER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商地点ID")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "PROD_LINE_CATEGORY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "生产线分类")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_mod_production_line", indexName: "MT_MOD_PRODUCTION_LINE_N1") {
            column(name: "ENABLE_FLAG")
        }
   createIndex(tableName: "mt_mod_production_line", indexName: "MT_MOD_PRODUCTION_LINE_N2") {
            column(name: "PROD_LINE_TYPE")
        }
   createIndex(tableName: "mt_mod_production_line", indexName: "MT_MOD_PRODUCTION_LINE_N3") {
            column(name: "SUPPLIER_ID")
            column(name: "SUPPLIER_SITE_ID")
        }

        addUniqueConstraint(columnNames:"PROD_LINE_CODE,TENANT_ID",tableName:"mt_mod_production_line",constraintName: "MT_MOD_PRODUCTION_LINE_U1")
    }
}