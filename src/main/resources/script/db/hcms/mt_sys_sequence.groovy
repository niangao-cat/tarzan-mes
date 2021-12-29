package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_sys_sequence.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_sys_sequence") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_sys_sequence_s', startValue:"1")
        }
        createTable(tableName: "mt_sys_sequence", remarks: "") {
            column(name: "NAME", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(primaryKey: true)} 
            column(name: "CURRENT_VALUE", type: "bigint(100)",   defaultValue:"0",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "SEQ_NAME", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "")   

        }

    }
}