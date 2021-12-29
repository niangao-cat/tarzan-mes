package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_work_order_actual_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_work_order_actual_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_work_order_actual_his_s', startValue:"1")
        }
        createTable(tableName: "mt_work_order_actual_his", remarks: "生产指令实绩历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "WORK_ORDER_ACTUAL_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，标识唯一一条实绩历史记录")  {constraints(primaryKey: true)} 
            column(name: "WORK_ORDER_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，标识唯一一条实绩记录")  {constraints(nullable:"false")}  
            column(name: "WORK_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工作订单ID")  {constraints(nullable:"false")}  
            column(name: "RELEASED_QTY", type: "decimal(20,6)",  remarks: "已下达数量")  {constraints(nullable:"false")}  
            column(name: "COMPLETED_QTY", type: "decimal(20,6)",  remarks: "已完成数量")  {constraints(nullable:"false")}  
            column(name: "SCRAPPED_QTY", type: "decimal(20,6)",  remarks: "已报废数量")  {constraints(nullable:"false")}  
            column(name: "HOLD_QTY", type: "decimal(20,6)",  remarks: "已保留数量")  {constraints(nullable:"false")}  
            column(name: "ACTUAL_START_DATE", type: "datetime",  remarks: "实际开始时间（第一个EO置于排队的时间）")   
            column(name: "ACTUAL_END_DATE", type: "datetime",  remarks: "实际完成时间（最后一个EO完成的时间）")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "TRX_RELEASED_QTY", type: "decimal(20,6)",  remarks: "事务数量-已下达数量")  {constraints(nullable:"false")}  
            column(name: "TRX_COMPLETED_QTY", type: "decimal(20,6)",  remarks: "事务数量-已完成数量")  {constraints(nullable:"false")}  
            column(name: "TRX_SCRAPPED_QTY", type: "decimal(20,6)",  remarks: "事务数量-已报废数量")  {constraints(nullable:"false")}  
            column(name: "TRX_HOLD_QTY", type: "decimal(20,6)",  remarks: "事务数量-已保留数量")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"WORK_ORDER_ID,EVENT_ID,TENANT_ID",tableName:"mt_work_order_actual_his",constraintName: "MT_WORK_ORDER_ACTUAL_HIS_U1")
    }
}