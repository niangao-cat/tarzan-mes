package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_numrange_object_num.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_numrange_object_num") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_numrange_object_num_s', startValue:"1")
        }
        createTable(tableName: "mt_numrange_object_num", remarks: "号码段按对象序列号记录表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "NUMRANGE_OBJECT_NUM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "按对象序列号记录表主键")  {constraints(primaryKey: true)} 
            column(name: "NUMRANGE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "号码段定义表主键")  {constraints(nullable:"false")}  
            column(name: "OBJECT_COMBINATION", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "对象拼接值")  {constraints(nullable:"false")}  
            column(name: "NUM_CURRENT", type: "varchar(" + 20 * weight + ")",  remarks: "当前序号")  {constraints(nullable:"false")}  
            column(name: "NUM_RESET_LASTDATE", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "序列号段上一次重置的日期")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,NUMRANGE_ID,OBJECT_COMBINATION",tableName:"mt_numrange_object_num",constraintName: "mt_numrange_object_num_u1")
    }
}