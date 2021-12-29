package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_locator_subinv_releation.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_locator_subinv_releation") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_locator_subinv_releation_s', startValue:"1")
        }
        createTable(tableName: "mt_locator_subinv_releation", remarks: "子库存与库位对应关系") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "RELEATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "库位ID（存储）")  {constraints(nullable:"false")}  
            column(name: "PLANT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工厂 CODE")  {constraints(nullable:"false")}  
            column(name: "SUBINV", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "子库存")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"SITE_ID,LOCATOR_ID",tableName:"mt_locator_subinv_releation",constraintName: "mt_locator_subinv_releation_u1")
    }
}