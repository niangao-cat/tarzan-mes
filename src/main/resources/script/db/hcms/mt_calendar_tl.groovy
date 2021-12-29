package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_calendar_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_calendar_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_calendar_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_calendar_tl", remarks: "") {
            column(name: "CALENDAR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "日历描述")   

        }

        addUniqueConstraint(columnNames:"CALENDAR_ID,LANG",tableName:"mt_calendar_tl",constraintName: "mt_calendar_tl_pk")
    }
}