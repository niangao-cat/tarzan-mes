package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_assemble_point_control.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_assemble_point_control") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_assemble_point_control_s', startValue:"1")
        }
        createTable(tableName: "mt_assemble_point_control", remarks: "装配点控制，指示具体装配控制下装配点可装载的物料") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_POINT_CONTROL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID,表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "ASSEMBLE_CONTROL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配控制ID")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_POINT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配点ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配物料")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配物料的物料批")   
            column(name: "UNIT_QTY", type: "decimal(36,6)",  remarks: "装配单位用量，需求量=单位用量*ASSEMBLE_BATCH_QTY")  {constraints(nullable:"false")}  
            column(name: "REFERENCE_POINT", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "装配参考点")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"Y",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ASSEMBLE_CONTROL_ID,ASSEMBLE_POINT_ID,TENANT_ID",tableName:"mt_assemble_point_control",constraintName: "mt_assemble_point_control_u1")
    }
}