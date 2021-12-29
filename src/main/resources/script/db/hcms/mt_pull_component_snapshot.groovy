package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_pull_component_snapshot.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_pull_component_snapshot") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_pull_component_snapshot_s', startValue:"1")
        }
        createTable(tableName: "mt_pull_component_snapshot", remarks: "拉动订单组件快照") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "CONPONENT_SNAPSHOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，唯一标识")  {constraints(primaryKey: true)} 
            column(name: "DISPATCH_SNAPSHOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "调度快照ID")  {constraints(nullable:"false")}  
            column(name: "COMPONENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "组件ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料ID")  {constraints(nullable:"false")}  
            column(name: "QTY", type: "decimal(36,6)",  remarks: "需求数")   
            column(name: "ACTUAL_QTY", type: "decimal(36,6)",  remarks: "实际装配数量")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"DISPATCH_SNAPSHOT_ID,COMPONENT_ID,TENANT_ID",tableName:"mt_pull_component_snapshot",constraintName: "MT_DISPATCH_STRATEGY_ORG_REL_U1")
    }
}