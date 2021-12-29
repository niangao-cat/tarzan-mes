package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_sample_size_code_letter.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-qms_sample_size_code_letter") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_sample_size_code_letter_s', startValue:"1")
        }
        createTable(tableName: "qms_sample_size_code_letter", remarks: "样本量字码表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "letter_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "sample_standard_type", type: "varchar(" + 100 * weight + ")",  remarks: "抽样标准类型")  {constraints(nullable:"false")}  
            column(name: "lot_size_from", type: "bigint(20)",  remarks: "批次范围从(大于等于)")  {constraints(nullable:"false")}  
            column(name: "lot_size_to", type: "bigint(20)",  remarks: "批次范围至(小于等于)")  {constraints(nullable:"false")}  
            column(name: "size_code_letter1", type: "varchar(" + 10 * weight + ")",  remarks: "样本量字码1(特殊检验水平S-1)")  {constraints(nullable:"false")}  
            column(name: "size_code_letter2", type: "varchar(" + 10 * weight + ")",  remarks: "样本量字码2(特殊检验水平S-2)")  {constraints(nullable:"false")}  
            column(name: "size_code_letter3", type: "varchar(" + 10 * weight + ")",  remarks: "样本量字码3(特殊检验水平S-3)")  {constraints(nullable:"false")}  
            column(name: "size_code_letter4", type: "varchar(" + 10 * weight + ")",  remarks: "样本量字码4(特殊检验水平S-4)")  {constraints(nullable:"false")}  
            column(name: "size_code_letter5", type: "varchar(" + 10 * weight + ")",  remarks: "样本量字码5(一般检验水平I)")  {constraints(nullable:"false")}  
            column(name: "size_code_letter6", type: "varchar(" + 10 * weight + ")",  remarks: "样本量字码6(一般检验水平II)")  {constraints(nullable:"false")}  
            column(name: "size_code_letter7", type: "varchar(" + 10 * weight + ")",  remarks: "样本量字码7(一般检验水平III)")  {constraints(nullable:"false")}  
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

    }
}