package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_area_purchase.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_area_purchase") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_area_purchase_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_area_purchase", remarks: "区域采购属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "AREA_PURCHASE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "AREA_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "区域ID，标识唯一区域")  {constraints(nullable:"false")}  
            column(name: "INSIDE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "是否厂内区域，主要在采购站点下使用，如区分厂内厂外送货区域")   
            column(name: "SUPPLIER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "厂外区域供应商")   
            column(name: "SUPPLIER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "厂外区域供应商地点")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_mod_area_purchase", indexName: "MT_MOD_AREA_PURCHASE_N1") {
            column(name: "SUPPLIER_ID")
            column(name: "SUPPLIER_SITE_ID")
        }
   createIndex(tableName: "mt_mod_area_purchase", indexName: "MT_MOD_AREA_PURCHASE_N2") {
            column(name: "INSIDE_FLAG")
        }

        addUniqueConstraint(columnNames:"AREA_ID,TENANT_ID",tableName:"mt_mod_area_purchase",constraintName: "MT_MOD_AREA_PURCHASE_U1")
    }
}