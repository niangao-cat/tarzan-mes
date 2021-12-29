package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_pfep_manufacturing.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_pfep_manufacturing") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_pfep_manufacturing_s', startValue:"1")
        }
        createTable(tableName: "mt_pfep_manufacturing", remarks: "物料生产属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "PFEP_MANUFACTURING_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料站点主键，标识唯一物料站点对应关系，限定为生产站点")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "组织类型，可选计划站点下区域、生产线、工作单元等类型")   
            column(name: "ORGANIZATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元")   
            column(name: "DEFAULT_BOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "默认装配清单")   
            column(name: "DEFAULT_ROUTING_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "默认工艺路线")   
            column(name: "ISSUE_CONTROL_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "投料限制类型，如数量限制、百分比限制等")   
            column(name: "ISSUE_CONTROL_QTY", type: "decimal(36,6)",  remarks: "投料限制值")   
            column(name: "COMPLETE_CONTROL_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "完工限制类型")   
            column(name: "COMPLETE_CONTROL_QTY", type: "decimal(36,6)",  remarks: "完工限制值")   
            column(name: "ATTRITION_CONTROL_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "损耗类型，如数量限制、百分比限制等")   
            column(name: "ATTRITION_CONTROL_QTY", type: "decimal(36,6)",  remarks: "损耗值")   
            column(name: "OPERATION_ASSEMBLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否工序装配")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_pfep_manufacturing", indexName: "MT_PFEP_MANUFACTURING_N1") {
            column(name: "ENABLE_FLAG")
        }
   createIndex(tableName: "mt_pfep_manufacturing", indexName: "MT_PFEP_MANUFACTURING_N2") {
            column(name: "DEFAULT_BOM_ID")
        }
   createIndex(tableName: "mt_pfep_manufacturing", indexName: "MT_PFEP_MANUFACTURING_N3") {
            column(name: "DEFAULT_ROUTING_ID")
        }
   createIndex(tableName: "mt_pfep_manufacturing", indexName: "MT_PFEP_MANUFACTURING_N4") {
            column(name: "ISSUE_CONTROL_TYPE")
        }
   createIndex(tableName: "mt_pfep_manufacturing", indexName: "MT_PFEP_MANUFACTURING_N5") {
            column(name: "COMPLETE_CONTROL_TYPE")
        }
   createIndex(tableName: "mt_pfep_manufacturing", indexName: "MT_PFEP_MANUFACTURING_N6") {
            column(name: "ATTRITION_CONTROL_TYPE")
        }

        addUniqueConstraint(columnNames:"MATERIAL_SITE_ID,ORGANIZATION_TYPE,ORGANIZATION_ID,TENANT_ID",tableName:"mt_pfep_manufacturing",constraintName: "MT_PFEP_MANUFACTURING_U1")
    }
}