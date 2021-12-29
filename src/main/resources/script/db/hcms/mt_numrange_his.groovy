package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_numrange_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_numrange_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_numrange_his_s', startValue:"1")
        }
        createTable(tableName: "mt_numrange_his", remarks: "号码段定义历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "NUMRANGE_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "号码段定义历史表主键")  {constraints(primaryKey: true)} 
            column(name: "NUMRANGE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "号码段定义表主键")  {constraints(nullable:"false")}  
            column(name: "OBJECT_ID", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "号段组合规则选项")  {constraints(nullable:"false")}  
            column(name: "NUMRANGE_GROUP", type: "varchar(" + 3 * weight + ")",   defaultValue:"",   remarks: "号段组号")  {constraints(nullable:"false")}  
            column(name: "NUM_DESCRIPTION", type: "varchar(" + 25 * weight + ")",   defaultValue:"",   remarks: "序列号段下限")  {constraints(nullable:"false")}  
            column(name: "INPUT_BOX1", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "号段组合规则输入框1")  {constraints(nullable:"false")}  
            column(name: "INPUT_BOX2", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "号段组合规则输入框2")   
            column(name: "INPUT_BOX3", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "号段组合规则输入框3")   
            column(name: "INPUT_BOX4", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "号段组合规则输入框4")   
            column(name: "INPUT_BOX5", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "号段组合规则输入框5")   
            column(name: "NUM_EXAMPLE", type: "varchar(" + 40 * weight + ")",   defaultValue:"",   remarks: "号段示例")   
            column(name: "OUTSIDE_NUM_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "外部输入编码标识")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")   
            column(name: "EVENT_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"NUMRANGE_ID,EVENT_ID,TENANT_ID",tableName:"mt_numrange_his",constraintName: "MT_NUMRANGE_HIS_U1")
    }
}