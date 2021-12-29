package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_organization_rel.groovy') {
    changeSet(author: "liyuan.lv@hand-china.com", id: "2020-05-20-mt_mod_organization_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_organization_rel_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_organization_rel", remarks: "组织结构关系") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_REL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "TOP_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "顶层站点ID")  {constraints(nullable:"false")}  
            column(name: "PARENT_ORGANIZATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "父层关系对象类型")  {constraints(nullable:"false")}  
            column(name: "PARENT_ORGANIZATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "父层关系对象")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "子层对象类型")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "子层对象")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(20)",  remarks: "父层对象下的顺序")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TOP_SITE_ID,PARENT_ORGANIZATION_TYPE,PARENT_ORGANIZATION_ID,ORGANIZATION_TYPE,ORGANIZATION_ID,TENANT_ID",tableName:"mt_mod_organization_rel",constraintName: "MT_MOD_ORGANIZATION_REL_U1")
    }
}