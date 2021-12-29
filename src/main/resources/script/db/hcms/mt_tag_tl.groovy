package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_tag_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_tag_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_tag_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_tag_tl", remarks: "") {
            column(name: "TAG_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "TAG_DESCRIPTION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "")   

        }

        addUniqueConstraint(columnNames:"TAG_ID,LANG",tableName:"mt_tag_tl",constraintName: "mt_tag_tl_pk")
    }
}