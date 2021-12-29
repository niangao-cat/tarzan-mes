package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_sample_scheme.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-qms_sample_scheme") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_sample_scheme_s', startValue:"1")
        }
        createTable(tableName: "qms_sample_scheme", remarks: "抽样方案表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")   
            column(name: "scheme_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "sample_plan_type", type: "varchar(" + 100 * weight + ")",  remarks: "抽样计划类型")  {constraints(nullable:"false")}  
            column(name: "sample_standard_type", type: "varchar(" + 100 * weight + ")",  remarks: "抽样标准类型")  {constraints(nullable:"false")}  
            column(name: "sample_size_code_letter", type: "varchar(" + 100 * weight + ")",  remarks: "样本量字码")   
            column(name: "lot_upper_limit", type: "bigint(20)",  remarks: "批量上限（零抽样方案用,非负整数）")   
            column(name: "lot_lower_limit", type: "bigint(20)",  remarks: "批量下限（零抽样方案用,非负整数）")   
            column(name: "acceptance_quantity_limit", type: "varchar(" + 100 * weight + ")",  remarks: "aql值")  {constraints(nullable:"false")}  
            column(name: "sample_size", type: "bigint(20)",  remarks: "抽样数量（非负整数，除-1，-1代表全检）")   
            column(name: "ac", type: "bigint(20)",  remarks: "ac值（非负整数）")   
            column(name: "re", type: "bigint(20)",  remarks: "re值（非负整数）")   
            column(name: "enable_flag", type: "varchar(" + 1 * weight + ")",  remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "cid", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",  remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
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

        addUniqueConstraint(columnNames:"sample_plan_type,sample_standard_type,sample_size_code_letter,acceptance_quantity_limit,lot_upper_limit,lot_lower_limit,tenant_id",tableName:"qms_sample_scheme",constraintName: "qms_sample_scheme_u1")
    }
}