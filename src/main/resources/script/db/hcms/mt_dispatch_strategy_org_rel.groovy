package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_dispatch_strategy_org_rel.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_dispatch_strategy_org_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_dispatch_strategy_org_rel_s', startValue:"1")
        }
        createTable(tableName: "mt_dispatch_strategy_org_rel", remarks: "调度策略与组织关系表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "STRATEGY_ORG_REL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，唯一标识")  {constraints(primaryKey: true)} 
            column(name: "ORGANIZATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "组织类型")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "组织ID")  {constraints(nullable:"false")}  
            column(name: "RANGE_STRATEGY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "范围策略")  {constraints(nullable:"false")}  
            column(name: "PUBLISH_STRATEGY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "发布策略")  {constraints(nullable:"false")}  
            column(name: "MOVE_STRATEGY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "移动策略")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "启用状态")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ORGANIZATION_TYPE,ORGANIZATION_ID,TENANT_ID",tableName:"mt_dispatch_strategy_org_rel",constraintName: "MT_DISPATCH_STRATEGY_ORG_REL_U1")
    }
}