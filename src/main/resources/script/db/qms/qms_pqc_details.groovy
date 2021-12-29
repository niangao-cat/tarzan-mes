package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_pqc_details.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2020-08-17-qms_pqc_details") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_pqc_details_s', startValue:"1")
        }
        createTable(tableName: "qms_pqc_details", remarks: "巡检单明细表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "PQC_DETAILS_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单明细主键ID")  {constraints(primaryKey: true)} 
            column(name: "PQC_HEADER_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单头表ID")  {constraints(nullable:"false")}  
            column(name: "PQC_LINE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单行主键ID")  {constraints(nullable:"false")}  
            column(name: "NUMBER", type: "decimal(20,0)",  remarks: "序号")  {constraints(nullable:"false")}  
            column(name: "RESULT", type: "varchar(" + 50 * weight + ")",  remarks: "结果值")  {constraints(nullable:"false")}  
            column(name: "REMARK", type: "varchar(" + 500 * weight + ")",  remarks: "备注")   
            column(name: "cid", type: "bigint(100)",  remarks: "cid")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
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
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,PQC_HEADER_ID,PQC_LINE_ID,NUMBER",tableName:"qms_pqc_details",constraintName: "qms_pqc_details_u1")
    }
}