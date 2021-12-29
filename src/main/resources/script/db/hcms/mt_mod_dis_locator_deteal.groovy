package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_dis_locator_deteal.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_dis_locator_deteal") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_dis_locator_deteal_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_dis_locator_deteal", remarks: "配送节点表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "DISTRIBUTION_LOCATOR_DETEAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，唯一标识")  {constraints(primaryKey: true)} 
            column(name: "LOCATOR_ORGANIZATION_REL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "组织与库位关系ID")  {constraints(nullable:"false")}  
            column(name: "PULL_T0_ARRIVE", type: "decimal(36,6)",  remarks: "触发到送达的周期")   
            column(name: "SOURCE_LOCATOR_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "来源库位标识")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"LOCATOR_ORGANIZATION_REL_ID,TENANT_ID",tableName:"mt_mod_dis_locator_deteal",constraintName: "MT_DISPATCH_STRATEGY_ORG_REL_U1")
    }
}