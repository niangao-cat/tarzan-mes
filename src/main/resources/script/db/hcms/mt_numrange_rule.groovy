package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_numrange_rule.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_numrange_rule") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_numrange_rule_s', startValue:"1")
        }
        createTable(tableName: "mt_numrange_rule", remarks: "号码段定义组合规则表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "NUMRANGE_RULE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "号码段定义组合规则表主键")  {constraints(primaryKey: true)} 
            column(name: "NUM_RULE", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "号段组合规则选项")   
            column(name: "FIX_INPUT", type: "varchar(" + 20 * weight + ")",   defaultValue:"",   remarks: "输入值")   
            column(name: "NUM_LEVEL", type: "varchar(" + 20 * weight + ")",   defaultValue:"",   remarks: "序列号层级")   
            column(name: "NUM_CONNECT_INPUT_BOX", type: "bigint(5)",  remarks: "特定对象关联框")   
            column(name: "NUM_LOWER_LIMIT", type: "varchar(" + 20 * weight + ")",   defaultValue:"",   remarks: "序列号段下限")   
            column(name: "NUM_UPPER_LIMIT", type: "varchar(" + 20 * weight + ")",   defaultValue:"",   remarks: "序列号段上限")   
            column(name: "NUM_ALERT", type: "varchar(" + 20 * weight + ")",   defaultValue:"",   remarks: "号段预警值")   
            column(name: "NUM_ALERT_TYPE", type: "varchar(" + 20 * weight + ")",   defaultValue:"",   remarks: "号段预警类型")   
            column(name: "NUM_RADIX", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "序列号段进制")   
            column(name: "NUM_INCREMENT", type: "bigint(10)",  remarks: "序列号段增量")   
            column(name: "NUM_CURRENT", type: "varchar(" + 20 * weight + ")",   defaultValue:"",   remarks: "当前序号")   
            column(name: "NUM_RESET_TYPE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "序列号段重置周期类型")   
            column(name: "NUM_RESET_PERIOD", type: "bigint(4)",  remarks: "序列号段重置周期时间")   
            column(name: "NUM_RESET_LASTDATE", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "序列号段上一次重置的日期")   
            column(name: "DATE_FORMAT", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "日期格式")   
            column(name: "TIME_FORMAT", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "时间格式")   
            column(name: "CALL_STANDARD_OBJECT", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "标准对象编码")   
            column(name: "INCOME_VALUE_LENGTH", type: "bigint(20)",  remarks: "传入值长度")   
            column(name: "INCOME_VALUE_LENGTH_LIMIT", type: "bigint(20)",  remarks: "输入值长度上限")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}