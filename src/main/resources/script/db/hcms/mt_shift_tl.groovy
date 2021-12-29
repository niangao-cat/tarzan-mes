package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_shift_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_shift_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_shift_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_shift_tl", remarks: "") {
            column(name: "SHIFT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "班次描述")   

        }

        addUniqueConstraint(columnNames:"SHIFT_ID,LANG",tableName:"mt_shift_tl",constraintName: "mt_shift_tl_pk")
    }
}