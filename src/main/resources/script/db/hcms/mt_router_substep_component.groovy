package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_router_substep_component.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_router_substep_component") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_router_substep_component_s', startValue:"1")
        }
        createTable(tableName: "mt_router_substep_component", remarks: "子步骤组件") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ROUTER_SUBSTEP_COMPONENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线子步骤组件标识")  {constraints(primaryKey: true)} 
            column(name: "ROUTER_SUBSTEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线子步骤标识")  {constraints(nullable:"false")}  
            column(name: "BOM_COMPONENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "BOM组件标识")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(100)",  remarks: "子步骤顺序")   
            column(name: "QTY", type: "decimal(36,6)",  remarks: "组件使用数量")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ROUTER_SUBSTEP_ID,BOM_COMPONENT_ID,TENANT_ID",tableName:"mt_router_substep_component",constraintName: "MT_ROUTER_SUBSTEP_COMPONENT_U1")
    }
}