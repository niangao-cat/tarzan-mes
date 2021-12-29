package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_exception.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_exception") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_exception_s', startValue:"1")
        }
        createTable(tableName: "hme_exception", remarks: "异常维护基础数据头表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")   
            column(name: "exception_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(nullable:"false")}  
            column(name: "exception_type", type: "varchar(" + 30 * weight + ")",  remarks: "异常类型")   
            column(name: "exception_code", type: "varchar(" + 30 * weight + ")",  remarks: "异常编码")   
            column(name: "exception_name", type: "varchar(" + 255 * weight + ")",  remarks: "异常描述")   
            column(name: "enable_flag", type: "varchar(" + 1 * weight + ")",  remarks: "是否有效")   
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

        addUniqueConstraint(columnNames:"tenant_id,exception_code",tableName:"hme_exception",constraintName: "hme_exception_u1")
    }
}