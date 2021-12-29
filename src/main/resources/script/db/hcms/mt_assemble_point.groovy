package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_assemble_point.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_assemble_point") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_assemble_point_s', startValue:"1")
        }
        createTable(tableName: "mt_assemble_point", remarks: "装配点，标识具体装配组下具体的装配位置") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_POINT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID,表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "ASSEMBLE_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配组")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_POINT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "装配点")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "装配点描述")   
            column(name: "UNIQUE_MATERIAL_LOT_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "是否限制唯一物料批")   
            column(name: "SEQUENCE", type: "bigint(100)",  remarks: "装配点在装配组内的顺序")  {constraints(nullable:"false")}  
            column(name: "MAX_QTY", type: "decimal(36,6)",  remarks: "最大装载量，如果为空则不限制")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"Y",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ASSEMBLE_GROUP_ID,ASSEMBLE_POINT_CODE,TENANT_ID",tableName:"mt_assemble_point",constraintName: "mt_assemble_point_u1")
        addUniqueConstraint(columnNames:"ASSEMBLE_GROUP_ID,SEQUENCE,TENANT_ID",tableName:"mt_assemble_point",constraintName: "mt_assemble_point_u2")
    }
}