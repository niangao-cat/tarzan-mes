package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_site_plant_releation.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_site_plant_releation") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_site_plant_releation_s', startValue:"1")
        }
        createTable(tableName: "mt_site_plant_releation", remarks: "ERP工厂与站点映射关系") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "RELEATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "SITE_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "站点类型:PURCHASE , SCHEDULE , MANUFACTURING")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "PLANT_ID", type: "varchar(" + 100 * weight + ")",  remarks: "工厂 ID")   
            column(name: "PLANT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工厂 CODE")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"SITE_TYPE,PLANT_CODE,TENANT_ID",tableName:"mt_site_plant_releation",constraintName: "MT_SITE_PLANT_RELEATION_U1")
        addUniqueConstraint(columnNames:"SITE_TYPE,SITE_ID,TENANT_ID",tableName:"mt_site_plant_releation",constraintName: "MT_SITE_PLANT_RELEATION_U2")
    }
}