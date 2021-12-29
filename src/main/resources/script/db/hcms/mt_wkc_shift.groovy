package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_wkc_shift.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_wkc_shift") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_wkc_shift_s', startValue:"1")
        }
        createTable(tableName: "mt_wkc_shift", remarks: "开班实绩数据表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "WKC_SHIFT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "作为班次日历分配唯一标识，用于其他数据结构引用工作日历。")  {constraints(primaryKey: true)} 
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工作单元ID")  {constraints(nullable:"false")}  
            column(name: "SHIFT_DATE", type: "date",  remarks: "班次日期")  {constraints(nullable:"false")}  
            column(name: "SHIFT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "用于标识该日期下班次")  {constraints(nullable:"false")}  
            column(name: "SHIFT_START_TIME", type: "datetime",  remarks: "班次开始时间")  {constraints(nullable:"false")}  
            column(name: "SHIFT_END_TIME", type: "datetime",  remarks: "班次结束时间")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"WORKCELL_ID,SHIFT_DATE,SHIFT_CODE,TENANT_ID",tableName:"mt_wkc_shift",constraintName: "MT_WKC_SHIFT_U1")
    }
}