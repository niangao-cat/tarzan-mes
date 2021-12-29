package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_assemble_group_control.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_assemble_group_control") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_assemble_group_control_s', startValue:"1")
        }
        createTable(tableName: "mt_assemble_group_control", remarks: "装配组控制，标识具体装配控制下对装配组的控制，装配组可安装的工作单元") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_GROUP_CONTROL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID,表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "ASSEMBLE_CONTROL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配控制ID")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配组ID")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "WKC")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"Y",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ASSEMBLE_CONTROL_ID,ASSEMBLE_GROUP_ID,TENANT_ID",tableName:"mt_assemble_group_control",constraintName: "mt_assemble_group_control_u1")
    }
}