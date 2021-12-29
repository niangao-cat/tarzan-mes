package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_pqc_inspection_content.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2020-08-12-qms_pqc_inspection_content") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_pqc_inspection_content_s', startValue:"1")
        }
        createTable(tableName: "qms_pqc_inspection_content", remarks: "巡检检验项目表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "PQC_INSPECTION_CONTENT_ID", type: "varchar(" + 100 * weight + ")",  remarks: "主键id")  {constraints(primaryKey: true)} 
            column(name: "SCHEME_ID", type: "varchar(" + 100 * weight + ")",  remarks: "物料检验计划ID")  {constraints(nullable:"false")}  
            column(name: "TAG_GROUP_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验组ID")  {constraints(nullable:"false")}  
            column(name: "TAG_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验项ID")  {constraints(nullable:"false")}  
            column(name: "ORDER_KEY", type: "bigint(100)",  remarks: "排序码")  {constraints(nullable:"false")}  
            column(name: "INSPECTION", type: "varchar(" + 200 * weight + ")",  remarks: "检验项目")  {constraints(nullable:"false")}  
            column(name: "INSPECTION_DESC", type: "varchar(" + 500 * weight + ")",   defaultValue:"1",   remarks: "检验项目描述")   
            column(name: "INSPECTION_TYPE", type: "varchar(" + 200 * weight + ")",  remarks: "检验项类型")  {constraints(nullable:"false")}  
            column(name: "FREQUENCY", type: "varchar(" + 100 * weight + ")",  remarks: "检验频率")   
            column(name: "STANDARD_TYPE", type: "varchar(" + 100 * weight + ")",  remarks: "规格类型")  {constraints(nullable:"false")}  
            column(name: "ACCURACY", type: "decimal(20,3)",  remarks: "精度")   
            column(name: "STANDARD_FROM", type: "decimal(20,4)",  remarks: "规格值从")   
            column(name: "STANDARD_TO", type: "decimal(20,4)",  remarks: "规格值至")   
            column(name: "STANDARD_UOM", type: "varchar(" + 20 * weight + ")",  remarks: "规格单位")   
            column(name: "STANDARD_TEXT", type: "varchar(" + 500 * weight + ")",  remarks: "文本规格值")   
            column(name: "INSPECTIOM_TOOL", type: "varchar(" + 100 * weight + ")",  remarks: "检验工具")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 10 * weight + ")",  remarks: "有效性")  {constraints(nullable:"false")}  
            column(name: "REMARK", type: "varchar(" + 500 * weight + ")",  remarks: "备注")   
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")",  remarks: "扩展字段1")   
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")",  remarks: "扩展字段2")   
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")",  remarks: "扩展字段3")   
            column(name: "cid", type: "bigint(100)",  remarks: "cid")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}