package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_work_order_component_actual.groovy') {
    changeSet(author: "liyuan.lv@hand-china.com", id: "2020-05-20-mt_work_order_component_actual") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_work_order_component_actual_s', startValue:"1")
        }
        createTable(tableName: "mt_work_order_component_actual", remarks: "生产订单组件装配实绩，记录生产订单物料和组件实际装配情况") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "WORK_ORDER_COMPONENT_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "执行作业组件实绩ID")  {constraints(primaryKey: true)} 
            column(name: "WORK_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO主键ID，标识实绩对应的唯一执行作业")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "实际装配物料ID")  {constraints(nullable:"false")}  
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "实际装配OPERATION")   
            column(name: "ASSEMBLE_QTY", type: "decimal(20,6)",  remarks: "实际装配数量")  {constraints(nullable:"false")}  
            column(name: "SCRAPPED_QTY", type: "decimal(20,6)",  remarks: "实际报废数量")  {constraints(nullable:"false")}  
            column(name: "COMPONENT_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "组件类型，如装配、拆卸、联产品等")  {constraints(nullable:"false")}  
            column(name: "BOM_COMPONENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "非强制装配时物料清单行ID")   
            column(name: "BOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配时引用的装配清单")   
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "非强制装配时组件所属装配步骤")   
            column(name: "ASSEMBLE_EXCESS_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "强制装配标识，“Y”代表该记录为强制装配，不属于原装配清单部分")   
            column(name: "ASSEMBLE_ROUTER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "是否为NC或特殊工艺路线装配，均属于强制装配")   
            column(name: "SUBSTITUTE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "替代装配标识，“Y”代表装配的是替代件，如果是替代，则不属于强制装配")   
            column(name: "ACTUAL_FIRST_TIME", type: "datetime",  remarks: "第一次装配时间")   
            column(name: "ACTUAL_LAST_TIME", type: "datetime",  remarks: "最近一次装配时间，最终体现确认时间")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"WORK_ORDER_ID,MATERIAL_ID,COMPONENT_TYPE,BOM_ID,OPERATION_ID,ROUTER_STEP_ID,TENANT_ID,BOM_COMPONENT_ID",tableName:"mt_work_order_component_actual",constraintName: "MT_WORK_ORDER_COMP_ACTUAL_U1")
    }
}