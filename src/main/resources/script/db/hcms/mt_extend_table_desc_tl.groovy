package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_extend_table_desc_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_extend_table_desc_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_extend_table_desc_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_extend_table_desc_tl", remarks: "") {
            column(name: "EXTEND_TABLE_DESC_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "ATTR_TABLE_DESC", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "")   

        }

        addUniqueConstraint(columnNames:"EXTEND_TABLE_DESC_ID,LANG",tableName:"mt_extend_table_desc_tl",constraintName: "mt_extend_table_desc_tl_pk")
    }
}