package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_iqc_details_his.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2020-08-06-qms_iqc_details_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_iqc_details_his_s', startValue:"1")
        }
        createTable(tableName: "qms_iqc_details_his", remarks: "质检单明细历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "IQC_DETAILS_HIS_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单明细历史主键")  {constraints(primaryKey: true)} 
            column(name: "IQC_DETAILS_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单明细主键ID")   
            column(name: "IQC_HEADER_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单头主键")   
            column(name: "IQC_LINE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单行主键")   
            column(name: "NUMBER", type: "bigint(20)",  remarks: "序号")  {constraints(nullable:"false")}  
            column(name: "RESULT", type: "varchar(" + 50 * weight + ")",  remarks: "结果值")  {constraints(nullable:"false")}  
            column(name: "REMARK", type: "varchar(" + 500 * weight + ")",  remarks: "备注")  {constraints(nullable:"false")}  
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",  remarks: "事件id")  {constraints(nullable:"false")}  
            column(name: "cid", type: "bigint(100)",  remarks: "cid")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,IQC_DETAILS_HIS_ID,EVENT_ID",tableName:"qms_iqc_details_his",constraintName: "qms_iqc_details_his_u1")
    }
}