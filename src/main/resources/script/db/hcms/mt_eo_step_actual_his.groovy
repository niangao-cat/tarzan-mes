package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_eo_step_actual_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_eo_step_actual_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_eo_step_actual_his_s', startValue:"1")
        }
        createTable(tableName: "mt_eo_step_actual_his", remarks: "执行作业-工艺路线步骤执行实绩") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EO_STEP_ACTUAL_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "EO_STEP_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主表主键ID")  {constraints(nullable:"false")}  
            column(name: "EO_ROUTER_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO工艺路线主键，表示唯一EO路径")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(10)",  remarks: "EO步骤实际执行顺序，生成时递增")  {constraints(nullable:"false")}  
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "步骤ID（对于特殊操作步骤ID就是操作ID）")  {constraints(nullable:"false")}  
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "标准作业（工艺）主键，表示唯一标准作业（工艺）")  {constraints(nullable:"false")}  
            column(name: "STEP_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "EO步骤名称")  {constraints(nullable:"false")}  
            column(name: "QUEUE_QTY", type: "decimal(20,6)",  remarks: "排队数量")  {constraints(nullable:"false")}  
            column(name: "WORKING_QTY", type: "decimal(20,6)",  remarks: "正在加工的数量")  {constraints(nullable:"false")}  
            column(name: "COMPLETE_PENDING_QTY", type: "decimal(20,6)",  remarks: "完成暂存数量")  {constraints(nullable:"false")}  
            column(name: "COMPLETED_QTY", type: "decimal(20,6)",  remarks: "完成的数量")  {constraints(nullable:"false")}  
            column(name: "SCRAPPED_QTY", type: "decimal(20,6)",  remarks: "报废数量")  {constraints(nullable:"false")}  
            column(name: "HOLD_QTY", type: "decimal(20,6)",  remarks: "保留数量")  {constraints(nullable:"false")}  
            column(name: "BYPASSED_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "步骤是否被跳过标识")   
            column(name: "REWORK_STEP_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否用于返工")   
            column(name: "LOCAL_REWORK_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否为此路径的返工步骤")   
            column(name: "MAX_PROCESS_TIMES", type: "bigint(100)",  remarks: "步骤最大允许加工次数（0意味着没有限制）")   
            column(name: "TIMES_PROCESSED", type: "bigint(100)",  remarks: "已加工次数（EO已通过该步骤的次数）")   
            column(name: "PREVIOUS_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "前道完工步骤（用于控制步骤间的移动，步骤排队数量的来源）")   
            column(name: "QUEUE_DATE", type: "datetime",  remarks: "EO最近一次置于排队的时间")   
            column(name: "WORKING_DATE", type: "datetime",  remarks: "EO最近一次置于运行的时间")   
            column(name: "COMPLETED_DATE", type: "datetime",  remarks: "EO最近一次置于完成的时间")   
            column(name: "COMPLETE_PENDING_DATE", type: "datetime",  remarks: "EO最近一次置于完成暂存的时间")   
            column(name: "STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "执行作业在工艺路线步骤上的状态，包括：")   
            column(name: "SPECIAL_INSTRUCTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "特殊操作说明")   
            column(name: "HOLD_COUNT", type: "bigint(10)",  remarks: "步骤预留次数，每次步骤增加预留时增加")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "TRX_QUEUE_QTY", type: "decimal(20,6)",  remarks: "事务数量-排队数量")   
            column(name: "TRX_WORKING_QTY", type: "decimal(20,6)",  remarks: "事务数量-正在加工的数量")   
            column(name: "TRX_COMPLETED_QTY", type: "decimal(20,6)",  remarks: "事务数量-完成的数量")   
            column(name: "TRX_SCRAPPED_QTY", type: "decimal(20,6)",  remarks: "事务数量-报废数量")   
            column(name: "TRX_HOLD_QTY", type: "decimal(20,6)",  remarks: "事务数量-保留数量")   
            column(name: "TRX_COMPLETE_PENDING_QTY", type: "decimal(20,6)",  remarks: "事务数量-完成暂存数量")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"EO_ROUTER_ACTUAL_ID,ROUTER_STEP_ID,EVENT_ID,TENANT_ID",tableName:"mt_eo_step_actual_his",constraintName: "MT_EO_STEP_ACTUAL_HIS_U1")
    }
}