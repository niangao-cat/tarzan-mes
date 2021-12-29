package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_sample_type.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-qms_sample_type") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_sample_type_s', startValue:"1")
        }
        createTable(tableName: "qms_sample_type", remarks: "抽样类型管理") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")   
            column(name: "sample_type_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键id，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "sample_type_code", type: "varchar(" + 50 * weight + ")",  remarks: "抽样方式编码")  {constraints(nullable:"false")}  
            column(name: "sample_type_desc", type: "varchar(" + 100 * weight + ")",  remarks: "抽样方式描述")  {constraints(nullable:"false")}  
            column(name: "sample_type", type: "varchar(" + 50 * weight + ")",  remarks: "抽样类型")  {constraints(nullable:"false")}  
            column(name: "parameters", type: "bigint(20)",  remarks: "参数值")   
            column(name: "sample_standard", type: "varchar(" + 50 * weight + ")",  remarks: "抽样标准")   
            column(name: "acceptance_quantity_limit", type: "decimal(20,4)",  remarks: "aql值")   
            column(name: "inspection_levels", type: "varchar(" + 50 * weight + ")",  remarks: "检验水平")   
            column(name: "enable_flag", type: "varchar(" + 10 * weight + ")",  remarks: "是否有效")  {constraints(nullable:"false")}  
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

        addUniqueConstraint(columnNames:"sample_type_code,inspection_levels",tableName:"qms_sample_type",constraintName: "SAMPLE_UK")
    }
}