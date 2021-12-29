package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_assemble_group.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_assemble_group") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_assemble_group_s', startValue:"1")
        }
        createTable(tableName: "mt_assemble_group", remarks: "装配组，标识一个装载设备或一类装配关系") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产站点")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_GROUP_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "装配组代码")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "装配组描述")   
            column(name: "AUTO_INSTALL_POINT_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "每次上料自动安装装配点，如果启用则ASSEMBLE_CONTROL_FLAG和ASSEMBLE_SEQUENCE_FLAG无效")   
            column(name: "ASSEMBLE_CONTROL_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "装配限制标识，如果启用则ASSEMBLE_SEQUENCE_FLAG无效，如果装配控制明细为空，则不校验")   
            column(name: "ASSEMBLE_SEQUENCE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "如果为是，则严格按照装配点在装配组的顺序装载物料")   
            column(name: "ASSEMBLE_GROUP_STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "状态，包括新建NEW、下达RELEASED、运行WORKING、保留HOLD、关闭CLOSED")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ASSEMBLE_GROUP_CODE,TENANT_ID",tableName:"mt_assemble_group",constraintName: "mt_assemble_group_u1")
    }
}