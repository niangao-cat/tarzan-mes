package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_router_site_assign.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_router_site_assign") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_router_site_assign_s', startValue:"1")
        }
        createTable(tableName: "mt_router_site_assign", remarks: "工艺路线站点分配表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ROUTER_SITE_ASSIGN_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线站点分配ID")  {constraints(primaryKey: true)} 
            column(name: "ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线ID")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ROUTER_ID,SITE_ID,TENANT_ID",tableName:"mt_router_site_assign",constraintName: "MT_ROUTER_SITE_ASSIGN_U1")
    }
}