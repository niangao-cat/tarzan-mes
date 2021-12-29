package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_assemble_pro_act_attr.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_assemble_pro_act_attr") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_assemble_pro_act_attr_s', startValue:"1")
        }
        createTable(tableName: "mt_assemble_pro_act_attr", remarks: "装配过程实绩扩展表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ATTR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "扩展表主键ID")  {constraints(primaryKey: true)} 
            column(name: "ASSEMBLE_PROCESS_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主表ID")  {constraints(nullable:"false")}  
            column(name: "ATTR_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "扩展属性名")  {constraints(nullable:"false")}  
            column(name: "ATTR_VALUE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "扩展属性值")   
            column(name: "LANG", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "语言类型")   
            column(name: "CID", type: "bigint(20)",  remarks: "CID")   
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")   
            column(name: "REQUEST_ID", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "PROGRAM_ID", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")   
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")   
            column(name: "LAST_UPDATE_LOGIN", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "ATTRIBUTE_CATEGORY", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "")   

        }

        addUniqueConstraint(columnNames:"ASSEMBLE_PROCESS_ACTUAL_ID,ATTR_NAME,LANG,TENANT_ID",tableName:"mt_assemble_pro_act_attr",constraintName: "MT_BOM_COMPONENT_ATTR_U1")
    }
}