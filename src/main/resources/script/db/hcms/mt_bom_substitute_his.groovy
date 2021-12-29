package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_bom_substitute_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_bom_substitute_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_bom_substitute_his_s', startValue:"1")
        }
        createTable(tableName: "mt_bom_substitute_his", remarks: "装配清单行替代项历史") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "BOM_SUBSTITUTE_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "唯一性索引")  {constraints(primaryKey: true)} 
            column(name: "BOM_SUBSTITUTE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配清单行替代项ID")  {constraints(nullable:"false")}  
            column(name: "BOM_SUBSTITUTE_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "关联的装配清单行替代组ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "替代物料ID")  {constraints(nullable:"false")}  
            column(name: "DATE_FROM", type: "datetime",  remarks: "生效时间")  {constraints(nullable:"false")}  
            column(name: "DATE_TO", type: "datetime",  remarks: "失效生产")   
            column(name: "SUBSTITUTE_VALUE", type: "decimal(36,6)",  remarks: "替代值（可能是百分比可能是优先级）")  {constraints(nullable:"false")}  
            column(name: "SUBSTITUTE_USAGE", type: "decimal(36,6)",  remarks: "替代用量")  {constraints(nullable:"false")}  
            column(name: "COPIED_FROM_SUBSTITUTE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "复制的来源替代项属性ID")   
            column(name: "EVENT_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"BOM_SUBSTITUTE_ID,EVENT_ID,TENANT_ID",tableName:"mt_bom_substitute_his",constraintName: "MT_BOM_SUBSTITUTE_HIS_U1")
    }
}