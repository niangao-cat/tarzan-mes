package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_wo_dispatch_recode.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_wo_dispatch_recode") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_wo_dispatch_recode_s', startValue:"1")
        }
        createTable(tableName: "hme_wo_dispatch_recode", remarks: "工单派工记录表") {
            column(name: "TENANT_ID", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "WO_DISPATCH_ID", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "WORK_ORDER_ID", type: "varchar(" + 100 * weight + ")",  remarks: "生产指令ID")  {constraints(nullable:"false")}  
            column(name: "PROD_LINE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "生产线ID")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",  remarks: "工段（工作单元）ID")  {constraints(nullable:"false")}  
            column(name: "CALENDAR_SHIFT_ID", type: "varchar(" + 100 * weight + ")",  remarks: "班次ID")  {constraints(nullable:"false")}  
            column(name: "SHIFT_COMPLETED_QTY", type: "decimal(36,6)",  remarks: "班次完工数量")   
            column(name: "DISPATCH_QTY", type: "decimal(36,6)",  remarks: "派工数量")   
            column(name: "CID", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"WORK_ORDER_ID,PROD_LINE_ID,WORKCELL_ID,CALENDAR_SHIFT_ID,TENANT_ID",tableName:"hme_wo_dispatch_recode",constraintName: "hme_wo_dispatch_recode_u1")
    }
}