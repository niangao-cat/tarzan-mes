package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_eo_actual_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_eo_actual_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_eo_actual_his_s', startValue:"1")
        }
        createTable(tableName: "mt_eo_actual_his", remarks: "执行作业实绩历史") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EO_ACTUAL_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，唯一性标识")  {constraints(primaryKey: true)} 
            column(name: "EO_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，标识唯一一条执行作业实绩记录")  {constraints(nullable:"false")}  
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO主键ID，标识实绩对应的唯一执行作业")  {constraints(nullable:"false")}  
            column(name: "ACTUAL_START_TIME", type: "datetime",  remarks: "实际开始时间")   
            column(name: "ACTUAL_END_TIME", type: "datetime",  remarks: "实际完成时间")   
            column(name: "COMPLETED_QTY", type: "decimal(20,6)",  remarks: "累计完工数量")  {constraints(nullable:"false")}  
            column(name: "SCRAPPED_QTY", type: "decimal(20,6)",  remarks: "累计报废数量")  {constraints(nullable:"false")}  
            column(name: "HOLD_QTY", type: "decimal(20,6)",  remarks: "累计保留数量")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "EVENT_ID", type: "bigint(100)",  remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "TRX_COMPLETED_QTY", type: "decimal(20,6)",  remarks: "事务数量-完工数量")  {constraints(nullable:"false")}  
            column(name: "TRX_SCRAPPED_QTY", type: "decimal(20,6)",  remarks: "事务数量-报废数量")  {constraints(nullable:"false")}  
            column(name: "TRX_HOLD_QTY", type: "decimal(20,6)",  remarks: "事务数量-保留数量")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,EVENT_ID,EO_ACTUAL_ID",tableName:"mt_eo_actual_his",constraintName: "MT_EO_ACTUAL_HIS_U1")
    }
}