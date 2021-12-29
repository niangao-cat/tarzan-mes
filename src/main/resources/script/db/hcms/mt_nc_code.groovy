package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_nc_code.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_nc_code") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_nc_code_s', startValue:"1")
        }
        createTable(tableName: "mt_nc_code", remarks: "不良代码数据") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "NC_CODE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "表ID，主键，供其他表做外键")  {constraints(primaryKey: true)} 
            column(name: "NC_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "不良代码组关联字段")   
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点为“生产类型”")  {constraints(nullable:"false")}  
            column(name: "NC_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "不良代码编码，前台通常显示此值")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "描述")   
            column(name: "NC_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "值集已维护在表GEN_TYPE中，对应字段TYPE_GROUP为“NC”：1.[FAILURE]缺陷 2.[DEFECT]瑕疵 3.[REPAIR]修复")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否可供车间使用（Y/N）")  {constraints(nullable:"false")}  
            column(name: "PRIORITY", type: "bigint(20)",  remarks: "优先级")   
            column(name: "CLOSURE_REQUIRED", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否需要被关闭")   
            column(name: "CONFIRM_REQUIRED", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否需要复核")   
            column(name: "AUTO_CLOSE_INCIDENT", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "自动关闭事故")   
            column(name: "AUTO_CLOSE_PRIMARY", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "自动关闭主代码")   
            column(name: "CAN_BE_PRIMARY_CODE", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "可以是主代码")   
            column(name: "VALID_AT_ALL_OPERATIONS", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "对所有工艺有效")   
            column(name: "ALLOW_NO_DISPOSITION", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "允许无处置")   
            column(name: "COMPONENT_REQUIRED", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否需要记录组件")   
            column(name: "DISPOSITION_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "处置组")   
            column(name: "MAX_NC_LIMIT", type: "bigint(20)",  remarks: "最大限制值")   
            column(name: "SECONDARY_CODE_SP_INSTR", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "次级代码特殊指令")   
            column(name: "SECONDARY_REQD_FOR_CLOSE", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "需要次级代码才能关闭")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"SITE_ID,NC_CODE,TENANT_ID",tableName:"mt_nc_code",constraintName: "MT_NC_CODE_U1")
    }
}