package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_customer_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_customer_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_customer_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_customer_tl", remarks: "") {
            column(name: "CUSTOMER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CUSTOMER_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "客户名称")   
            column(name: "CUSTOMER_NAME_ALT", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "客户简称")   

        }

        addUniqueConstraint(columnNames:"CUSTOMER_ID,LANG",tableName:"mt_customer_tl",constraintName: "mt_customer_tl_pk")
    }
}