package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_workcell_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_workcell_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_workcell_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_workcell_tl", remarks: "") {
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工作单元名称")   
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "工作单元描述")   
            column(name: "WORKCELL_LOCATION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工作单元位置")   

        }

        addUniqueConstraint(columnNames:"WORKCELL_ID,LANG",tableName:"mt_mod_workcell_tl",constraintName: "mt_mod_workcell_tl_pk")
    }
}