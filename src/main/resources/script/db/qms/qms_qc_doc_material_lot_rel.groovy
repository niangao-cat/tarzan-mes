package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_qc_doc_material_lot_rel.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2020-08-24-qms_qc_doc_material_lot_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_qc_doc_material_lot_rel_s', startValue:"1")
        }
        createTable(tableName: "qms_qc_doc_material_lot_rel", remarks: "二次送检条码") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "qc_doc_material_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "instruction_doc_id", type: "varchar(" + 100 * weight + ")",  remarks: "送货单头ID")  {constraints(nullable:"false")}  
            column(name: "instruction_id", type: "varchar(" + 100 * weight + ")",  remarks: "送货单行ID")  {constraints(nullable:"false")}  
            column(name: "iqc_header_id", type: "varchar(" + 100 * weight + ")",  remarks: "质检单头ID")   
            column(name: "doc_status", type: "varchar(" + 30 * weight + ")",  remarks: "状态")  {constraints(nullable:"false")}  
            column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "条码")  {constraints(nullable:"false")}  
            column(name: "quantity", type: "decimal(36,6)",  remarks: "条码数量")  {constraints(nullable:"false")}  
            column(name: "cid", type: "bigint(100)",  remarks: "cid")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE_CATEGORY", type: "varchar(" + 30 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE6", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }

    }
}