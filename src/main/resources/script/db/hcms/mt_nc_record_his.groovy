package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_nc_record_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_nc_record_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_nc_record_his_s', startValue:"1")
        }
        createTable(tableName: "mt_nc_record_his", remarks: "不良代码记录历史") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "NC_RECORD_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "表ID，主键，供其他表做外键")  {constraints(primaryKey: true)} 
            column(name: "NC_RECORD_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "不良记录")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点")  {constraints(nullable:"false")}  
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO")  {constraints(nullable:"false")}  
            column(name: "PARENT_NC_RECORD_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "父不良记录")   
            column(name: "USER_ID", type: "bigint(20)",  remarks: "记录人")   
            column(name: "SEQUENCE", type: "bigint(20)",  remarks: "顺序")  {constraints(nullable:"false")}  
            column(name: "NC_INCIDENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "不良事故ID")  {constraints(nullable:"false")}  
            column(name: "DATE_TIME", type: "datetime",  remarks: "NC记录时间")  {constraints(nullable:"false")}  
            column(name: "QTY", type: "decimal(20,6)",   defaultValue:"0.000000",   remarks: "数量")   
            column(name: "DEFECT_COUNT", type: "decimal(20,6)",   defaultValue:"0.000000",   remarks: "缺陷数量")   
            column(name: "COMMENTS", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "备注")   
            column(name: "NC_CODE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "不良代码ID")  {constraints(nullable:"false")}  
            column(name: "NC_TYPE", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "不良代码分类，缺陷/瑕疵/修复")  {constraints(nullable:"false")}  
            column(name: "COMPONENT_MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "NC记录的组件")   
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批")   
            column(name: "REFERENCE_POINT", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "组件装配参考点")   
            column(name: "EO_STEP_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "步骤")   
            column(name: "ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线")   
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺")   
            column(name: "ROOT_CAUSE_OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "产生问题的源工艺")   
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工作单元")   
            column(name: "ROOT_CAUSE_WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "产生问题的源工作单元")   
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "发生不良的装配件")  {constraints(nullable:"false")}  
            column(name: "NC_STATUS", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "NC状态，打开/关闭/取消")  {constraints(nullable:"false")}  
            column(name: "CONFIRMED_STATUS", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "NC复核状态，已复核/未复核")  {constraints(nullable:"false")}  
            column(name: "CONFIRMED_DATE_TIME", type: "datetime",  remarks: "复核时间")   
            column(name: "CLOSURE_REQUIRED_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "Y/N,是否必须被关闭")  {constraints(nullable:"false")}  
            column(name: "CLOSED_DATE_TIME", type: "datetime",  remarks: "事故发生日期")   
            column(name: "CLOSED_USER_ID", type: "bigint(20)",  remarks: "事故发生日期")   
            column(name: "DISPOSITION_DONE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "NC是否被处置，默认N")  {constraints(nullable:"false")}  
            column(name: "DISPOSITION_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "处置组:NC被记录的处置功能")   
            column(name: "DISPOSITION_ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "处置工艺路线:NC被记录的处置路线")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件唯一标识")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"NC_RECORD_ID,EVENT_ID,TENANT_ID",tableName:"mt_nc_record_his",constraintName: "MT_NC_RECORD_HIS_U1")
    }
}