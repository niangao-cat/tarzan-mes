package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_instruction_detail.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_instruction_detail") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_instruction_detail_s', startValue:"1")
        }
        createTable(tableName: "mt_instruction_detail", remarks: "指令明细行") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "INSTRUCTION_DETAIL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "指令明细ID,唯一标识")  {constraints(primaryKey: true)} 
            column(name: "INSTRUCTION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "指令ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"INSTRUCTION_ID,MATERIAL_LOT_ID,TENANT_ID",tableName:"mt_instruction_detail",constraintName: "MT_INSTRUCTION_DETAIL_U1")
    }
}