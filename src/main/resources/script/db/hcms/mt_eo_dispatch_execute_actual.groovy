package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_eo_dispatch_execute_actual.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_eo_dispatch_execute_actual") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_eo_dispatch_execute_actual_s', startValue:"1")
        }
        createTable(tableName: "mt_eo_dispatch_execute_actual", remarks: "调度结果执行表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EO_DISPATCH_EXECUTE_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(nullable:"false")}  
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "执行作业主键")  {constraints(primaryKey: true)} 
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "步骤主键")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "步骤执行工作单元，工作单元主键，标识唯一工作单元")  {constraints(nullable:"false")}  
            column(name: "SHIFT_DATE", type: "date",  remarks: "日期")  {constraints(nullable:"false")}  
            column(name: "SHIFT_CODE", type: "varchar(" + 20 * weight + ")",   defaultValue:"",   remarks: "班次")  {constraints(nullable:"false")}  
            column(name: "PRIORITY", type: "bigint(20)",  remarks: "优先级")   
            column(name: "ASSIGN_QTY", type: "decimal(36,6)",  remarks: "调度数量")   
            column(name: "QUEUE_QTY", type: "decimal(36,6)",  remarks: "排队数量")   
            column(name: "COMPLETED_QTY", type: "decimal(36,6)",  remarks: "完成数量")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}