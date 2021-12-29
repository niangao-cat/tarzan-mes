package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_nc_group_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_nc_group_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_nc_group_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_nc_group_tl", remarks: "") {
            column(name: "NC_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "")   

        }

        addUniqueConstraint(columnNames:"NC_GROUP_ID,LANG",tableName:"mt_nc_group_tl",constraintName: "mt_nc_group_tl_pk")
    }
}