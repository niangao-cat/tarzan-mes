package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_iqc_header.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-qms_iqc_header") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_iqc_header_s', startValue:"1")
        }
        createTable(tableName: "qms_iqc_header", remarks: "") {
            column(name: "TENANT_ID", type: "bigint(20)",  remarks: "租户id")   
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "组织")  {constraints(nullable:"false")}  
            column(name: "IQC_HEADER_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单头表主键")  {constraints(primaryKey: true)} 
            column(name: "IQC_NUMBER", type: "varchar(" + 50 * weight + ")",  remarks: "检验单号")  {constraints(nullable:"false")}  
            column(name: "RECEIPT_LOT", type: "varchar(" + 50 * weight + ")",  remarks: "接收批次")  {constraints(nullable:"false")}  
            column(name: "RECEIPT_BY", type: "varchar(" + 100 * weight + ")",  remarks: "接收人")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_ID", type: "varchar(" + 100 * weight + ")",  remarks: "供应商ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",  remarks: "物料ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_VERSION", type: "varchar(" + 100 * weight + ")",  remarks: "物料版本")   
            column(name: "INSPECTION_TYPE", type: "varchar(" + 50 * weight + ")",  remarks: "检验类型")  {constraints(nullable:"false")}  
            column(name: "UAI_FLAG", type: "varchar(" + 50 * weight + ")",  remarks: "特采标识")   
            column(name: "INSPECTION_STATUS", type: "varchar(" + 50 * weight + ")",  remarks: "检验状态")  {constraints(nullable:"false")}  
            column(name: "DOC_TYPE", type: "varchar(" + 50 * weight + ")",  remarks: "检验来源")  {constraints(nullable:"false")}  
            column(name: "DOC_HEADER_ID", type: "varchar(" + 100 * weight + ")",  remarks: "来源单号")  {constraints(nullable:"false")}  
            column(name: "DOC_LINE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "来源单行号")   
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",  remarks: "货位")  {constraints(nullable:"false")}  
            column(name: "CREATED_DATE", type: "datetime",  remarks: "到货日期")  {constraints(nullable:"false")}  
            column(name: "QUANTITY", type: "decimal(20,0)",  remarks: "物料数量")  {constraints(nullable:"false")}  
            column(name: "UOM_ID", type: "varchar(" + 100 * weight + ")",  remarks: "单位")  {constraints(nullable:"false")}  
            column(name: "INSPECTION_METHOD", type: "varchar(" + 50 * weight + ")",  remarks: "检验方式")  {constraints(nullable:"false")}  
            column(name: "INSPECTION_START_DATE", type: "datetime",  remarks: "检验开始时间")   
            column(name: "IDENTIFICATION", type: "varchar(" + 50 * weight + ")",  remarks: "检验单标识（如加急）")   
            column(name: "INSPECTION_FINISH_DATE", type: "datetime",  remarks: "完成时间")   
            column(name: "INSPECTION_TIME", type: "decimal(20,2)",  remarks: "检验时长（单位：小时）")   
            column(name: "INSPECTION_RESULT", type: "varchar(" + 100 * weight + ")",  remarks: "检验结果")   
            column(name: "QC_BY", type: "bigint(20)",  remarks: "检验员")   
            column(name: "OK_QTY", type: "bigint(20)",  remarks: "合格项数")   
            column(name: "NG_QTY", type: "bigint(20)",  remarks: "不合格项数")   
            column(name: "REMARK", type: "varchar(" + 500 * weight + ")",  remarks: "备注")   
            column(name: "FINAL_DECISION", type: "varchar(" + 100 * weight + ")",  remarks: "审核结果")   
            column(name: "AUDIT_OPINION", type: "varchar(" + 500 * weight + ")",  remarks: "审核意见")   
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

        addUniqueConstraint(columnNames:"IQC_HEADER_ID",tableName:"qms_iqc_header",constraintName: "UNI1_QMS_IQC_HEADER")
        addUniqueConstraint(columnNames:"IQC_NUMBER",tableName:"qms_iqc_header",constraintName: "UNI2_QMS_IQC_HEADER_")
        addUniqueConstraint(columnNames:"DOC_HEADER_ID,DOC_LINE_ID",tableName:"qms_iqc_header",constraintName: "UNI3_QMS_IQC_HEADER_")
    }
}