package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_eo_dispatch_history.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_eo_dispatch_history") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_eo_dispatch_history_s', startValue:"1")
        }
        createTable(tableName: "mt_eo_dispatch_history", remarks: "调度历史表，记录历史发布的调度结果和版本") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EO_DISPATCH_HISTORY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "执行作业主键，标识该步骤分配对应的执行作业结构")  {constraints(nullable:"false")}  
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO工艺路线步骤主键，用于标识唯一EO工艺路线步骤实绩")  {constraints(nullable:"false")}  
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺ID")  {constraints(nullable:"false")}  
            column(name: "STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "状态")   
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "步骤执行工作单元，工作单元主键，标识唯一工作单元")  {constraints(nullable:"false")}  
            column(name: "PRIORITY", type: "bigint(100)",  remarks: "执行顺序")  {constraints(nullable:"false")}  
            column(name: "PLAN_START_TIME", type: "datetime",  remarks: "调度计划开始时间")   
            column(name: "PLAN_END_TIME", type: "datetime",  remarks: "调度计划结束时间")   
            column(name: "SHIFT_DATE", type: "date",  remarks: "日历日期")  {constraints(nullable:"false")}  
            column(name: "SHIFT_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "班次")  {constraints(nullable:"false")}  
            column(name: "ASSIGN_QTY", type: "decimal(20,6)",  remarks: "本次分配数量（分到不同的WKC）")  {constraints(nullable:"false")}  
            column(name: "REVISION", type: "bigint(100)",  remarks: "版本")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}