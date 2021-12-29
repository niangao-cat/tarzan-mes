package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_inspection_levels_record.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-qms_inspection_levels_record") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_inspection_levels_record_s', startValue:"1")
        }
        createTable(tableName: "qms_inspection_levels_record", remarks: "供应商物料检验水平记录表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "record_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "组织")  {constraints(nullable:"false")}  
            column(name: "supplier_id", type: "varchar(" + 100 * weight + ")",  remarks: "供应商")  {constraints(nullable:"false")}  
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料")  {constraints(nullable:"false")}  
            column(name: "inspection_levels", type: "varchar(" + 50 * weight + ")",  remarks: "检验水平")  {constraints(nullable:"false")}  
            column(name: "inspection_method", type: "varchar(" + 50 * weight + ")",  remarks: "检验方式")  {constraints(nullable:"false")}  
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
   createIndex(tableName: "qms_inspection_levels_record", indexName: "UNI_LEVELS_RECORD") {
            column(name: "supplier_id")
            column(name: "material_id")
        }

    }
}