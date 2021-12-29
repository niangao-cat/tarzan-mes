package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_iqc_line.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-qms_iqc_line") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_iqc_line_s', startValue:"1")
        }
        createTable(tableName: "qms_iqc_line", remarks: "") {
            column(name: "TENANT_ID", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "IQC_HEADER_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单头表ID")  {constraints(nullable:"false")}  
            column(name: "IQC_LINE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单行主键ID")  {constraints(primaryKey: true)} 
            column(name: "NUMBER", type: "bigint(20)",  remarks: "检验项序号")  {constraints(nullable:"false")}  
            column(name: "INSPECTION_TYPE", type: "varchar(" + 50 * weight + ")",  remarks: "检验项类别")  {constraints(nullable:"false")}  
            column(name: "INSPECTION", type: "varchar(" + 100 * weight + ")",  remarks: "检验项目")  {constraints(nullable:"false")}  
            column(name: "INSPECTION_DESC", type: "varchar(" + 500 * weight + ")",  remarks: "检验项描述")  {constraints(nullable:"false")}  
            column(name: "SAMPLE_TYPE", type: "varchar(" + 50 * weight + ")",  remarks: "抽样方案类型")  {constraints(nullable:"false")}  
            column(name: "INSPECTION_LEVELS", type: "varchar(" + 50 * weight + ")",  remarks: "检验水平")   
            column(name: "DEFECT_LEVELS", type: "varchar(" + 50 * weight + ")",  remarks: "缺陷等级")  {constraints(nullable:"false")}  
            column(name: "ACCEPTANCE_QUANTITY_LIMIT", type: "decimal(20,4)",  remarks: "AQL值")   
            column(name: "SAMPLE_SIZE", type: "decimal(20,0)",  remarks: "抽样数量")  {constraints(nullable:"false")}  
            column(name: "AC", type: "decimal(20,0)",  remarks: "AC值")  {constraints(nullable:"false")}  
            column(name: "RE", type: "decimal(20,0)",  remarks: "RE值")  {constraints(nullable:"false")}  
            column(name: "STANDARD_TEXT", type: "varchar(" + 500 * weight + ")",  remarks: "文本规格值")   
            column(name: "STANDARD_FROM", type: "decimal(20,0)",  remarks: "规格值从")   
            column(name: "STANDARD_TO", type: "decimal(20,0)",  remarks: "规格值至")   
            column(name: "STANDARD_UOM", type: "varchar(" + 20 * weight + ")",  remarks: "规格单位")   
            column(name: "STANDARD_TYPE", type: "varchar(" + 20 * weight + ")",  remarks: "规格类型")  {constraints(nullable:"false")}  
            column(name: "INSPECTION_TOOL", type: "varchar(" + 50 * weight + ")",  remarks: "检验工具")  {constraints(nullable:"false")}  
            column(name: "NG_QTY", type: "decimal(20,0)",  remarks: "不良数")   
            column(name: "OK_QTY", type: "decimal(20,0)",  remarks: "合格数")   
            column(name: "INSPECTION_RESULT", type: "varchar(" + 50 * weight + ")",  remarks: "结论")   
            column(name: "ATTACHMENT_UUID", type: "varchar(" + 100 * weight + ")",  remarks: "附件ID")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 20 * weight + ")",  remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "ADDED_FLAG", type: "varchar(" + 20 * weight + ")",  remarks: "新增标识")   
            column(name: "cid", type: "bigint(100)",  remarks: "cid")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "attribute_category", type: "varchar(" + 30 * weight + ")",  remarks: "")   
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute5", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute6", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute7", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute8", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute9", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute10", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }

        addUniqueConstraint(columnNames:"IQC_LINE_ID",tableName:"qms_iqc_line",constraintName: "UNI1_HQM_SAMPLE_SCHEME")
        addUniqueConstraint(columnNames:"IQC_HEADER_ID,NUMBER",tableName:"qms_iqc_line",constraintName: "UNI2_HQM_SAMPLE_SCHEME")
    }
}