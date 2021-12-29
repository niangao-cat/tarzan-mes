package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_router_operation_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_router_operation_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_router_operation_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_router_operation_tl", remarks: "") {
            column(name: "ROUTER_OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "SPECIAL_INTRUCTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "")   

        }

        addUniqueConstraint(columnNames:"ROUTER_OPERATION_ID,LANG",tableName:"mt_router_operation_tl",constraintName: "mt_router_operation_tl_pk")
    }
}