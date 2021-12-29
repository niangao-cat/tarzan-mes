package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_calendar_org_rel.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_calendar_org_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_calendar_org_rel_s', startValue:"1")
        }
        createTable(tableName: "mt_calendar_org_rel", remarks: "日历组织关系表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "CALENDAR_ORG_REL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "作为日历组织关系唯一标识，主键")  {constraints(primaryKey: true)} 
            column(name: "CALENDAR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "作为日历唯一标识，用于其他数据结构引用")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "对应的组织代码ID")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "如SITE\\AREA\\PRODUCTIONLINE等，TYPE_GROUP:CALENDAR_ORG_TYPE")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效。默认为“N”")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"CALENDAR_ID,ORGANIZATION_ID,ORGANIZATION_TYPE,TENANT_ID",tableName:"mt_calendar_org_rel",constraintName: "MT_CALENDAR_ORG_U1")
    }
}