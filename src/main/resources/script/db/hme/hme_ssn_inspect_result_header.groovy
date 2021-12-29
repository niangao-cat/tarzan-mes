package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_ssn_inspect_result_header.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china", id: "2021-02-04-hme_ssn_inspect_result_header") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_ssn_inspect_result_header_s', startValue:"1")
        }
        createTable(tableName: "hme_ssn_inspect_result_header", remarks: "标准件检验结果头") {
            column(name: "ssn_inspect_result_header_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "standard_sn_code", type: "varchar(" + 100 * weight + ")",  remarks: "标准件编码")  {constraints(nullable:"false")}  
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料id")   
            column(name: "cos_type", type: "varchar(" + 100 * weight + ")",  remarks: "芯片类型")   
            column(name: "work_way", type: "varchar(" + 100 * weight + ")",  remarks: "工作方式")   
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位id")   
            column(name: "result", type: "varchar(" + 30 * weight + ")",  remarks: "检验结果")   
            column(name: "shift_date", type: "datetime",  remarks: "日期")   
            column(name: "shift_code", type: "varchar(" + 100 * weight + ")",  remarks: "班次")   
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "cid", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
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
            column(name: "ATTRIBUTE11", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE12", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE13", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE14", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE15", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }
   createIndex(tableName: "hme_ssn_inspect_result_header", indexName: "hme_ssn_inspect_result_header_n1") {
            column(name: "shift_date")
            column(name: "shift_code")
        }
   createIndex(tableName: "hme_ssn_inspect_result_header", indexName: "hme_ssn_inspect_result_header_n2") {
            column(name: "material_id")
        }
   createIndex(tableName: "hme_ssn_inspect_result_header", indexName: "hme_ssn_inspect_result_header_n3") {
            column(name: "workcell_id")
        }

    }
}