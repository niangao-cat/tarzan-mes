package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_assemble_confirm_actual_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_assemble_confirm_actual_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_assemble_confirm_actual_his_s', startValue:"1")
        }
        createTable(tableName: "mt_assemble_confirm_actual_his", remarks: "装配确认实绩历史，指示执行作业组件材料的装配和确认历史记录情况") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_CONFIRM_ACTUAL_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配确认实绩历史ID")  {constraints(primaryKey: true)} 
            column(name: "ASSEMBLE_CONFIRM_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配确认实绩")  {constraints(nullable:"false")}  
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO主键ID，标识实绩对应的唯一执行作业")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "实际装配物料ID")  {constraints(nullable:"false")}  
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "实际装配OPERATION，实绩装配工艺与需求步骤工艺不一致时也判断为强制装配")   
            column(name: "COMPONENT_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "实际装配组件类型，如装配、拆卸、联产品等")  {constraints(nullable:"false")}  
            column(name: "BOM_COMPONENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "非强制装配时物料对应装配清单行ID")   
            column(name: "BOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配时执行作业引用的装配清单")   
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "非强制装配时物料对应组件装配步骤需求")   
            column(name: "ASSEMBLE_EXCESS_FLAG", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "强制装配标识，“Y”代表该记录为强制装配，不属于原装配清单部分")   
            column(name: "ASSEMBLE_ROUTER_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "包括NC和特殊工艺路线装配，均属于强制装配")   
            column(name: "SUBSTITUTE_FLAG", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "替代装配标识，“Y”代表装配的是替代件，如果是替代")   
            column(name: "BYPASS_FLAG", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配遗留标识，“Y”代表用户主动遗留")   
            column(name: "BYPASS_BY", type: "varchar(" + 20 * weight + ")",   defaultValue:"",   remarks: "遗留人")   
            column(name: "CONFIRM_FLAG", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配确认标识，“Y”代表该行记录以被确认")   
            column(name: "CONFIRMED_BY", type: "varchar(" + 20 * weight + ")",   defaultValue:"",   remarks: "确认人")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ASSEMBLE_CONFIRM_ACTUAL_ID,EVENT_ID,TENANT_ID",tableName:"mt_assemble_confirm_actual_his",constraintName: "MT_ASS_CON_ACT_HIS_U1")
    }
}