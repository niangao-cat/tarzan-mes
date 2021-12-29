package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_data_record_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_data_record_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_data_record_his_s', startValue:"1")
        }
        createTable(tableName: "mt_data_record_his", remarks: "数据收集实绩历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "DATA_RECORD_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "DATA_RECORD_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "基表主键")  {constraints(nullable:"false")}  
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "执行作业")   
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料ID")   
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺ID")   
            column(name: "EO_STEP_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "步骤实绩唯一标识")   
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线步骤ID")   
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工作单元ID")   
            column(name: "COMPONENT_MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "组件物料")   
            column(name: "ASSEMBLE_CONFIRM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配实绩唯一标识")   
            column(name: "NC_CODE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "不良代码")   
            column(name: "TAG_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据组")  {constraints(nullable:"false")}  
            column(name: "TAG_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据项")  {constraints(nullable:"false")}  
            column(name: "TAG_VALUE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "收集值（文件时为路径）")  {constraints(nullable:"false")}  
            column(name: "TAG_CALCULATE_RESULT", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "判定结果")   
            column(name: "RECORD_REMARK", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "备注")   
            column(name: "RECORD_DATE", type: "datetime",  remarks: "时间")  {constraints(nullable:"false")}  
            column(name: "USER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "记录人ID")  {constraints(nullable:"false")}  
            column(name: "TAG_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据项编号")   
            column(name: "TAG_DESCRIPTION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "描述")   
            column(name: "REMARK", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "备注")   
            column(name: "TAG_ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "状态")   
            column(name: "VALUE_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据类型")   
            column(name: "TRUE_VALUE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "符合值")   
            column(name: "FALSE_VALUE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "不符合值")   
            column(name: "COLLECTION_METHOD", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据收集方式")   
            column(name: "VALUE_ALLOW_MISSING", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "允许缺失值")   
            column(name: "MINIMUM_VALUE", type: "decimal(36,6)",  remarks: "最小值")   
            column(name: "MAXIMAL_VALUE", type: "decimal(36,6)",  remarks: "最大值")   
            column(name: "UNIT", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "计量单位")   
            column(name: "MANDATORY_NUM", type: "bigint(20)",  remarks: "必需的数据条数")   
            column(name: "OPTIONAL_NUM", type: "bigint(20)",  remarks: "可选的数据条数")   
            column(name: "API_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "转化API_ID")   
            column(name: "TAG_GROUP_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据收集组编码")   
            column(name: "TAG_GROUP_DESCRIPTION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据收集组描述")   
            column(name: "TAG_GROUP_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "收集组类型")   
            column(name: "SOURCE_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "源数据收集组ID")   
            column(name: "BUSINESS_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "业务类型")   
            column(name: "TAG_GROUP_STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "状态")   
            column(name: "COLLECTION_TIME_CONTROL", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据收集时点")   
            column(name: "USER_VERIFICATION", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "需要用户验证")   
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批ID")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"DATA_RECORD_ID,EVENT_ID,TENANT_ID",tableName:"mt_data_record_his",constraintName: "mt_data_record_his_u1")
    }
}