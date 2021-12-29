package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_iqc_details.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-qms_iqc_details") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_iqc_details_s', startValue:"1")
        }
        createTable(tableName: "qms_iqc_details", remarks: "") {
            column(name: "TENANT_ID", type: "bigint(20)",  remarks: "")   
            column(name: "IQC_DETAILS_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单明细主键ID")  {constraints(primaryKey: true)} 
            column(name: "IQC_HEADER_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单头主键")  {constraints(nullable:"false")}  
            column(name: "IQC_LINE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单行主键")  {constraints(nullable:"false")}  
            column(name: "NUMBER", type: "bigint(20)",  remarks: "序号")  {constraints(nullable:"false")}  
            column(name: "RESULT", type: "varchar(" + 50 * weight + ")",  remarks: "结果值")  {constraints(nullable:"false")}  
            column(name: "REMARK", type: "varchar(" + 500 * weight + ")",  remarks: "备注")   
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

        addUniqueConstraint(columnNames:"IQC_HEADER_ID,IQC_LINE_ID,NUMBER",tableName:"qms_iqc_details",constraintName: "UNI1_QMS_IQC_DETAILS")
        addUniqueConstraint(columnNames:"IQC_DETAILS_ID",tableName:"qms_iqc_details",constraintName: "UNI2_QMS_IQC_DETAILS")
    }
}