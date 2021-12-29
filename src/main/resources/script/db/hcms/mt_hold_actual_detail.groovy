package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_hold_actual_detail.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_hold_actual_detail") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_hold_actual_detail_s', startValue:"1")
        }
        createTable(tableName: "mt_hold_actual_detail", remarks: "保留实绩明细") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "HOLD_DETAIL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "保留实绩明细ID，主键，供其他表做外键")  {constraints(primaryKey: true)} 
            column(name: "HOLD_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "保留实绩ID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "保留对象类型，包括装配清单、工艺路线、WO、EO、装配组")  {constraints(nullable:"false")}  
            column(name: "OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "保留对象值")  {constraints(nullable:"false")}  
            column(name: "EO_STEP_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "保留所在的EO步骤实绩ID")   
            column(name: "ORIGINAL_STATUS", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "保留前的初始状态")  {constraints(nullable:"false")}  
            column(name: "FUTURE_HOLD_ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "将来保留的工艺路线步骤ID")   
            column(name: "FUTURE_HOLD_STATUS", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "将来保留发生所在的步骤状态")   
            column(name: "HOLD_EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "保留事件ID")   
            column(name: "RELEASE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "保留是否释放的标识")   
            column(name: "RELEASE_COMMENT", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "保留释放的备注")   
            column(name: "RELEASE_TIME", type: "datetime",  remarks: "保留备注")   
            column(name: "RELEASE_BY", type: "bigint(20)",  remarks: "保留释放的人")   
            column(name: "RELEASE_REASON_CODE", type: "varchar(" + 240 * weight + ")",   defaultValue:"",   remarks: "保留释放的原因代码")   
            column(name: "RELEASE_EVENT_ID", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "保留释放事件ID")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"HOLD_ID,OBJECT_TYPE,OBJECT_ID,TENANT_ID",tableName:"mt_hold_actual_detail",constraintName: "MT_HOLD_ACTUAL_DETAIL_U1")
    }
}