package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_substep.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_substep") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_substep_s', startValue:"1")
        }
        createTable(tableName: "mt_substep", remarks: "子步骤") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "SUBSTEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "表ID，主键，供其他表做外键")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "SUBSTEP_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "子步骤")  {constraints(nullable:"false")}  
            column(name: "SUBSTEP_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "子步骤类型，包括：Normal：该子步骤是正常且必须完成的；Optional：该子步骤是可选则完成的，有可能被略过；Critical：该子步骤是关键且必须完成的。")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "子步骤描述")   
            column(name: "LONG_DESCRIPTION", type: "varchar(" + 4000 * weight + ")",   defaultValue:"",   remarks: "子步骤长描述")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"SITE_ID,SUBSTEP_NAME,TENANT_ID",tableName:"mt_substep",constraintName: "MT_SUBSTEP_U1")
    }
}