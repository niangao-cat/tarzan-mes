package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_operation.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_operation") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_operation_s', startValue:"1")
        }
        createTable(tableName: "mt_operation", remarks: "工序") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺唯一标识")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点标识")  {constraints(nullable:"false")}  
            column(name: "OPERATION_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工艺名称")  {constraints(nullable:"false")}  
            column(name: "REVISION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工艺版本")  {constraints(nullable:"false")}  
            column(name: "CURRENT_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "Y/N，是否当前版本")   
            column(name: "DATE_FROM", type: "datetime",  remarks: "有效时间至")  {constraints(nullable:"false")}  
            column(name: "DATE_TO", type: "datetime",  remarks: "有效时间从")   
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "备注，有条件的话做成多行文本")   
            column(name: "OPERATION_STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工艺状态（STATUS_GROUP:OPERATION_STATUS）：1.【NEW】“新建”：用户无法使用此工艺，无法将其用于工艺路线，但是可以更改此工艺记录；2.【CAN_RELEASE】“可下达”：用户可以在此工艺中处理执行作业；3.【FREEZE】“冻结”：用户可以在此工艺中处理执行作业，但是不可更改此工艺记录；4.【ABANDON】“废弃”：用户无法使用此工艺，而且无法在此工艺中处理执行作业，同时不可更改此工艺记录；5.【HOLD】“保留”：用户无法在此工艺中处理执行作业。当保留释放后，便可继续执行；6.【NCLIMIT】“保留未关闭NC”用户不能在此工艺处理存在未关闭NC的执行作业。")  {constraints(nullable:"false")}  
            column(name: "OPERATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工艺类型（TYPE_GROUP:OPERATION_TYPE）：1.【NORMAL】“正常”：此工艺可以应用于常规和NC类型的工艺路线；2.【SPECIAL】“特殊”：此工艺仅可用于特殊类型的工艺路线的首道步骤；3.【TEST】“测试”：此工艺用于测试。")  {constraints(nullable:"false")}  
            column(name: "SPECIAL_ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "特殊工艺路线ID")   
            column(name: "TOOL_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工具类型")   
            column(name: "TOOL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工具")   
            column(name: "WORKCELL_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工作单元类型")   
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工作单元")   
            column(name: "DEFAULT_NC_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "此操作的缺省不合格代码。操作员针对 EO 编号记录不合格项时，系统会提供此代码。")   
            column(name: "STANDARD_MAX_LOOP", type: "bigint(100)",  remarks: "可以在此操作中处理一个 EO 编号的最大次数此字段不适用于已选中〖松散路线流〗复选框的路线上的车间作业控制编号。是路线上工艺 最大循环次数 的多语言")   
            column(name: "STANDARD_SPECIAL_INTRODUCTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "路线步骤的特殊指令REQUIRED_TIME_IN_PROCESS")   
            column(name: "STANDARD_REQD_TIME_IN_PROCESS", type: "decimal(36,6)",  remarks: "处理车间作业控制所需的占用时间或有效工作时间（按分钟计）")   
            column(name: "COMPLETE_INCONFORMITY_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "完工不一致标识")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_operation", indexName: "MT_OPERATION_N1") {
            column(name: "CURRENT_FLAG")
        }
   createIndex(tableName: "mt_operation", indexName: "MT_OPERATION_N2") {
            column(name: "SPECIAL_ROUTER_ID")
        }
   createIndex(tableName: "mt_operation", indexName: "MT_OPERATION_N3") {
            column(name: "WORKCELL_ID")
        }
   createIndex(tableName: "mt_operation", indexName: "MT_OPERATION_N4") {
            column(name: "DEFAULT_NC_ID")
        }

        addUniqueConstraint(columnNames:"SITE_ID,OPERATION_NAME,REVISION,TENANT_ID",tableName:"mt_operation",constraintName: "MT_OPERATION_U1")
    }
}