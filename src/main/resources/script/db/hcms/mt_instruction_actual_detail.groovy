package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_instruction_actual_detail.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_instruction_actual_detail") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_instruction_actual_detail_s', startValue:"1")
        }
        createTable(tableName: "mt_instruction_actual_detail", remarks: "指令实绩明细表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ACTUAL_DETAIL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "实绩id")  {constraints(primaryKey: true)} 
            column(name: "ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "汇总id")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批ID")  {constraints(nullable:"false")}  
            column(name: "UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "单位")  {constraints(nullable:"false")}  
            column(name: "ACTUAL_QTY", type: "decimal(36,6)",  remarks: "实绩数量")  {constraints(nullable:"false")}  
            column(name: "CONTAINER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "器具id")   
            column(name: "FROM_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "来源库位id ")   
            column(name: "TO_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "目标库位id")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ACTUAL_ID,MATERIAL_LOT_ID,TENANT_ID",tableName:"mt_instruction_actual_detail",constraintName: "MT_INSTRUCTION_ACTUAL_DTL_U1")
    }
}