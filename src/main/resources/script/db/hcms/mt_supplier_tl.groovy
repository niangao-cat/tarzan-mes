package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_supplier_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_supplier_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_supplier_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_supplier_tl", remarks: "") {
            column(name: "SUPPLIER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "供应商名称")   
            column(name: "SUPPLIER_NAME_ALT", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "供应商简称")   

        }

        addUniqueConstraint(columnNames:"SUPPLIER_ID,LANG",tableName:"mt_supplier_tl",constraintName: "mt_supplier_tl_pk")
    }
}