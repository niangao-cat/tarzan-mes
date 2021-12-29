package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_prod_line_dispatch_oper.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_prod_line_dispatch_oper") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_prod_line_dispatch_oper_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_prod_line_dispatch_oper", remarks: "生产线调度指定工艺") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "DISPATCH_OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产线调度指定工艺ID")  {constraints(primaryKey: true)} 
            column(name: "PROD_LINE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产线ID")  {constraints(nullable:"false")}  
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"PROD_LINE_ID,OPERATION_ID,TENANT_ID",tableName:"mt_mod_prod_line_dispatch_oper",constraintName: "MT_MOD_PROD_LINE_DIS_OPER_U1")
    }
}