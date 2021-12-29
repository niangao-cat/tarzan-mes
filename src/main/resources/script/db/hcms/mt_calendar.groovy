package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_calendar.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_calendar") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_calendar_s', startValue:"1")
        }
        createTable(tableName: "mt_calendar", remarks: "工作日历") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "CALENDAR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "作为日历唯一标识，用于其他数据结构引用")  {constraints(primaryKey: true)} 
            column(name: "CALENDAR_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "该日历编码")  {constraints(nullable:"false")}  
            column(name: "CALENDAR_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "TYPE_GROUP:CALENDAR_TYPE：1.【PLAN】2.【STANDARD】3.【PURCHASE】")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "描述")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效。默认为“N”")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"CALENDAR_CODE,TENANT_ID",tableName:"mt_calendar",constraintName: "MT_CALENDAR_U1")
    }
}