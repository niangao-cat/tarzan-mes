package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_bom_component.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_bom_component") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_bom_component_s', startValue:"1")
        }
        createTable(tableName: "mt_bom_component", remarks: "装配清单行") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "BOM_COMPONENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料清单行ID")  {constraints(primaryKey: true)} 
            column(name: "BOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料清单头ID")  {constraints(nullable:"false")}  
            column(name: "LINE_NUMBER", type: "bigint(100)",  remarks: "序号")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "组件物料ID")  {constraints(nullable:"false")}  
            column(name: "BOM_COMPONENT_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "组件类型，如装配、拆卸、联产品等")  {constraints(nullable:"false")}  
            column(name: "DATE_FROM", type: "datetime",  remarks: "生效时间")  {constraints(nullable:"false")}  
            column(name: "DATE_TO", type: "datetime",  remarks: "失效生产")   
            column(name: "QTY", type: "decimal(36,6)",  remarks: "数量，六位小数")  {constraints(nullable:"false")}  
            column(name: "KEY_MATERIAL_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "关键物料标识")   
            column(name: "ASSEMBLE_METHOD", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "装配方式(投料/上料位反冲/库存反冲)")   
            column(name: "ASSEMBLE_AS_REQ_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "是否按需求数量装配")   
            column(name: "ATTRITION_POLICY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "损耗策略，1按固定值，2按百分比，3固定值+百分比")   
            column(name: "ATTRITION_CHANCE", type: "decimal(36,6)",  remarks: "损耗百分比，两位小数")   
            column(name: "ATTRITION_QTY", type: "decimal(36,6)",  remarks: "固定损耗值，六位小数")   
            column(name: "COPIED_FROM_COMPONENT_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "复制的来源装配清单行ID")   
            column(name: "ISSUED_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "发料库位ID")   
            column(name: "LATEST_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "最新一次新增历史表的主键")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_bom_component", indexName: "MT_BOM_COMPONENT_N1") {
            column(name: "KEY_MATERIAL_FLAG")
        }
   createIndex(tableName: "mt_bom_component", indexName: "MT_BOM_COMPONENT_N3") {
            column(name: "ASSEMBLE_METHOD")
        }
   createIndex(tableName: "mt_bom_component", indexName: "MT_BOM_COMPONENT_N4") {
            column(name: "ASSEMBLE_AS_REQ_FLAG")
        }

        addUniqueConstraint(columnNames:"TENANT_ID,BOM_ID,MATERIAL_ID,LINE_NUMBER,BOM_COMPONENT_TYPE",tableName:"mt_bom_component",constraintName: "MT_BOM_COMPONENT_U1")
    }
}