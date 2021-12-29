package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_assemble_process_actual.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_assemble_process_actual") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_assemble_process_actual_s', startValue:"1")
        }
        createTable(tableName: "mt_assemble_process_actual", remarks: "装配过程实绩，记录每一次执行作业的材料明细装配记录") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_PROCESS_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配过程实绩")  {constraints(primaryKey: true)} 
            column(name: "ASSEMBLE_CONFIRM_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配确认实绩")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_QTY", type: "decimal(36,6)",  remarks: "本次装配数量，六位小数")  {constraints(nullable:"false")}  
            column(name: "SCRAP_QTY", type: "decimal(36,6)",  remarks: "本次报废数量，六位小数")  {constraints(nullable:"false")}  
            column(name: "ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "实际装配所在工艺路线")   
            column(name: "SUBSTEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "子步骤")   
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批")   
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工作单元")   
            column(name: "ASSEMBLE_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配组")   
            column(name: "ASSEMBLE_POINT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配点")   
            column(name: "REFERENCE_AREA", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "参考区域")   
            column(name: "REFERENCE_POINT", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "参考点")   
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配库位")   
            column(name: "ASSEMBLE_METHOD", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "装配方式(投料/上料位反冲/库存反冲)")   
            column(name: "OPERATE_BY", type: "bigint(20)",  remarks: "操作人")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_LOT_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批历史ID")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "EVENT_TIME", type: "datetime",  remarks: "事件时间")  {constraints(nullable:"false")}  
            column(name: "EVENT_BY", type: "bigint(20)",  remarks: "事件人")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}