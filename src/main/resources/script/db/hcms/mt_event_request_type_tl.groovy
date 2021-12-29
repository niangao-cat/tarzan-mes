package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_event_request_type_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_event_request_type_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_event_request_type_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_event_request_type_tl", remarks: "") {
            column(name: "REQUEST_TYPE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 4000 * weight + ")",   defaultValue:"",   remarks: "")   

        }

        addUniqueConstraint(columnNames:"REQUEST_TYPE_ID,LANG",tableName:"mt_event_request_type_tl",constraintName: "mt_event_request_type_tl_pk")
    }
}