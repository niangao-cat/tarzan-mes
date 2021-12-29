package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_pull_dispatch_snapshot.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_pull_dispatch_snapshot") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_pull_dispatch_snapshot_s', startValue:"1")
        }
        createTable(tableName: "mt_pull_dispatch_snapshot", remarks: "拉动调度结果快照") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "DISPATCH_SNAPSHOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，唯一标识")  {constraints(primaryKey: true)} 
            column(name: "SHIFT_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "拉动日期")  {constraints(nullable:"false")}  
            column(name: "SHIFT_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "班次拉动")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "WKC_ID")  {constraints(nullable:"false")}  
            column(name: "AREA_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "配送路线")  {constraints(nullable:"false")}  
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO_ID")   
            column(name: "WORK_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "WO_ID")   
            column(name: "DISPATCH_QTY", type: "decimal(36,6)",  remarks: "调度数量")   
            column(name: "PRIORITY", type: "decimal(36,6)",  remarks: "优先级")   
            column(name: "REVISION", type: "decimal(36,6)",  remarks: "调度版本")   
            column(name: "SNAPSHOT_REVISION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "快照版本：配送路线+MMDDHHMMSS")  {constraints(nullable:"false")}  
            column(name: "LATEST_REVISION_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "标志")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"WORKCELL_ID,SNAPSHOT_REVISION,EO_ID,WORK_ORDER_ID,TENANT_ID",tableName:"mt_pull_dispatch_snapshot",constraintName: "MT_DISPATCH_STRATEGY_ORG_REL_U1")
    }
}