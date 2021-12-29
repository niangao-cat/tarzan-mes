package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_material_category_site.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_material_category_site") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_material_category_site_s', startValue:"1")
        }
        createTable(tableName: "mt_material_category_site", remarks: "物料类别站点分配") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_CATEGORY_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_CATEGORY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料类别，来自物料类别表，对应一个物料分类")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点，来自站点表，对应唯一站点")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"MATERIAL_CATEGORY_ID,SITE_ID,TENANT_ID",tableName:"mt_material_category_site",constraintName: "MT_MATERIAL_CATEGORY_SITE")
    }
}