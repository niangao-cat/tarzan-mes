package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_iqc_header_his.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2020-08-06-qms_iqc_header_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_iqc_header_his_s', startValue:"1")
        }
        createTable(tableName: "qms_iqc_header_his", remarks: "质检单头历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")   
            column(name: "IQC_HEADER_HIS_ID", type: "varchar(" + 100 * weight + ")",  remarks: "质检单头历史表主键")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "组织")  {constraints(nullable:"false")}  
            column(name: "IQC_HEADER_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单头表id")  {constraints(nullable:"false")}  
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
            column(name: "INSPECTION_TIME", type: "decimal(20,0)",  remarks: "检验时长（单位：小时）")   
            column(name: "INSPECTION_RESULT", type: "varchar(" + 100 * weight + ")",  remarks: "检验结果")   
            column(name: "QC_BY", type: "varchar(" + 20 * weight + ")",  remarks: "检验员")   
            column(name: "OK_QTY", type: "bigint(20)",  remarks: "合格项数")   
            column(name: "NG_QTY", type: "bigint(20)",  remarks: "不合格项数")   
            column(name: "REMARK", type: "varchar(" + 500 * weight + ")",  remarks: "备注")   
            column(name: "FINAL_DECISION", type: "varchar(" + 100 * weight + ")",  remarks: "审核结果")   
            column(name: "AUDIT_OPINION", type: "varchar(" + 500 * weight + ")",  remarks: "审核意见")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",  remarks: "事件id")  {constraints(nullable:"false")}  
            column(name: "cid", type: "bigint(100)",  remarks: "cid")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,EVENT_ID,IQC_HEADER_HIS_ID",tableName:"qms_iqc_header_his",constraintName: "qms_iqc_header_his_u1")
    }
}