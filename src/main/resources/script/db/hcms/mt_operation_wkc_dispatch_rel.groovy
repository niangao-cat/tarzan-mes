package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_operation_wkc_dispatch_rel.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_operation_wkc_dispatch_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_operation_wkc_dispatch_rel_s', startValue:"1")
        }
        createTable(tableName: "mt_operation_wkc_dispatch_rel", remarks: "工艺和工作单元调度关系表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "OPERATION_WKC_DISPATCH_REL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "关系ID，标识唯一一条数据")  {constraints(primaryKey: true)} 
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺ID，标识唯一工艺，表示关系所属工艺")  {constraints(nullable:"false")}  
            column(name: "STEP_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "步骤名称， 用于在工艺路线中多次出现同一标准工艺时区分唯一工艺")   
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工作单元ID，标识在当前工艺和步骤下允许调度分派时选择的WKC")  {constraints(nullable:"false")}  
            column(name: "PRIORITY", type: "bigint(10)",  remarks: "优先级，标识当前工艺步骤存在多个可选WKC时，推荐的使用顺序")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"OPERATION_ID,STEP_NAME,WORKCELL_ID,TENANT_ID",tableName:"mt_operation_wkc_dispatch_rel",constraintName: "MT_OPER_WKC_DISPATCH_REL_U1")
    }
}