package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_shift.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_shift") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_shift_s', startValue:"1")
        }
        createTable(tableName: "mt_shift", remarks: "班次信息") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "SHIFT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "作为班次唯一标识，用于其他数据结构引用该班次。")  {constraints(primaryKey: true)} 
            column(name: "SHIFT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "可以作为班次标识。")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(100)",  remarks: "顺序")  {constraints(nullable:"false")}  
            column(name: "SHIFT_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "指一天之内班次划分的不同方式TYPE_CODE:SHIFT_TYPE")  {constraints(nullable:"false")}  
            column(name: "SHIFT_START_TIME", type: "varchar(" + 255 * weight + ")",  remarks: "班次开始时间")  {constraints(nullable:"false")}  
            column(name: "SHIFT_END_TIME", type: "varchar(" + 255 * weight + ")",  remarks: "班次结束时间")  {constraints(nullable:"false")}  
            column(name: "REST_TIME", type: "decimal(36,6)",  remarks: "班次内休息时间 小时")   
            column(name: "UTILIZATION_RATE", type: "decimal(36,6)",  remarks: "开动率 %")   
            column(name: "BORROWING_ABILITY", type: "decimal(36,6)",  remarks: "借用能力 小时")   
            column(name: "CAPACITY_UNIT", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "能力单位")   
            column(name: "STANDARD_CAPACITY", type: "decimal(36,6)",  remarks: "标准产量")   
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "班次描述")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "该班次是否有效。默认为“N”")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_shift", indexName: "MT_SHIFT_U2") {
            column(name: "SEQUENCE")
            column(name: "SHIFT_TYPE")
            column(name: "TENANT_ID")
        }

        addUniqueConstraint(columnNames:"SHIFT_TYPE,SHIFT_CODE,TENANT_ID",tableName:"mt_shift",constraintName: "MT_SHIFT_U1")
    }
}