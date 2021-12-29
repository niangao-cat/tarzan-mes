package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_eo_step_workcell_actual.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_eo_step_workcell_actual") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_eo_step_workcell_actual_s', startValue:"1")
        }
        createTable(tableName: "mt_eo_step_workcell_actual", remarks: "执行作业-工艺路线步骤执行明细") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EO_STEP_WORKCELL_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "EO_STEP_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO步骤实绩主键，表示唯一EO工艺路线步骤执行实绩")  {constraints(nullable:"false")}  
            column(name: "QUEUE_QTY", type: "decimal(20,6)",  remarks: "排队数量")  {constraints(nullable:"false")}  
            column(name: "WORKING_QTY", type: "decimal(20,6)",  remarks: "正在加工的数量")  {constraints(nullable:"false")}  
            column(name: "COMPLETE_PENDING_QTY", type: "decimal(20,6)",  remarks: "完成暂存数量")  {constraints(nullable:"false")}  
            column(name: "COMPLETED_QTY", type: "decimal(20,6)",  remarks: "完成的数量")  {constraints(nullable:"false")}  
            column(name: "SCRAPPED_QTY", type: "decimal(20,6)",  remarks: "报废的数量")  {constraints(nullable:"false")}  
            column(name: "QUEUE_DATE", type: "datetime",  remarks: "EO最近一次置于排队的时间")   
            column(name: "WORKING_DATE", type: "datetime",  remarks: "EO最近一次置于运行的时间")   
            column(name: "COMPLETED_DATE", type: "datetime",  remarks: "EO最近一次置于完成的时间")   
            column(name: "COMPLETE_PENDING_DATE", type: "datetime",  remarks: "EO最近一次置于完成暂存的时间")   
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO在此步骤的工作单元")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"EO_STEP_ACTUAL_ID,WORKCELL_ID,TENANT_ID",tableName:"mt_eo_step_workcell_actual",constraintName: "MT_EO_STEP_WORKCELL_ACTUAL_U1")
    }
}