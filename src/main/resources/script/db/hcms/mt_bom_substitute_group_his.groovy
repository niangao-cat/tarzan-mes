package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_bom_substitute_group_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_bom_substitute_group_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_bom_substitute_group_his_s', startValue:"1")
        }
        createTable(tableName: "mt_bom_substitute_group_his", remarks: "装配清单行替代组历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "BOM_SUBSTITUTE_GROUP_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "唯一性索引")  {constraints(primaryKey: true)} 
            column(name: "BOM_SUBSTITUTE_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配清单行替代组ID")  {constraints(nullable:"false")}  
            column(name: "BOM_COMPONENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "关联的装配清单行ID")  {constraints(nullable:"false")}  
            column(name: "SUBSTITUTE_GROUP", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "替代组编码")  {constraints(nullable:"false")}  
            column(name: "SUBSTITUTE_POLICY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "替代策略编码，1表示百分比，2表示优先级")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "替代物料有效标识")  {constraints(nullable:"false")}  
            column(name: "COPIED_FROM_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "复制的来源替代组属性ID")   
            column(name: "EVENT_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"BOM_SUBSTITUTE_GROUP_ID,EVENT_ID,TENANT_ID",tableName:"mt_bom_substitute_group_his",constraintName: "MT_BOM_SUBSTITUTE_GROUP_HIS_U1")
    }
}