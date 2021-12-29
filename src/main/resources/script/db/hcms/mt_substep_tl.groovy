package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_substep_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_substep_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_substep_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_substep_tl", remarks: "") {
            column(name: "SUBSTEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "子步骤描述")   
            column(name: "LONG_DESCRIPTION", type: "varchar(" + 4000 * weight + ")",   defaultValue:"",   remarks: "子步骤长描述")   

        }

        addUniqueConstraint(columnNames:"SUBSTEP_ID,LANG",tableName:"mt_substep_tl",constraintName: "mt_substep_tl_pk")
    }
}