package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_prod_line_manufacturing.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_prod_line_manufacturing") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_prod_line_manufacturing_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_prod_line_manufacturing", remarks: "生产线生产属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "PROD_LINE_MANUFACTURING_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID ,表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "PROD_LINE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产线ID，标识唯一一条生产线")  {constraints(nullable:"false")}  
            column(name: "ISSUED_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "默认发料库位")   
            column(name: "COMPLETION_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "默认完工库位")   
            column(name: "INVENTORY_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "默认入库库位")   
            column(name: "DISPATCH_METHOD", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "调度方式")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_mod_prod_line_manufacturing", indexName: "MT_MOD_PROD_LINE_MANU_N1") {
            column(name: "ISSUED_LOCATOR_ID")
        }
   createIndex(tableName: "mt_mod_prod_line_manufacturing", indexName: "MT_MOD_PROD_LINE_MANU_N2") {
            column(name: "COMPLETION_LOCATOR_ID")
        }
   createIndex(tableName: "mt_mod_prod_line_manufacturing", indexName: "MT_MOD_PROD_LINE_MANU_N3") {
            column(name: "INVENTORY_LOCATOR_ID")
        }

        addUniqueConstraint(columnNames:"PROD_LINE_ID,TENANT_ID",tableName:"mt_mod_prod_line_manufacturing",constraintName: "MT_MOD_PROD_LINE_MANU_U1")
    }
}