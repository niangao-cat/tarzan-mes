package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_intercept_object.groovy') {
changeSet(author: "wengang.qiang@hand-china.com", id: "2021-09-11-hme_intercept_object") {
    def weight = 1
    if(helper.isSqlServer()){
        weight = 2
    } else if(helper.isOracle()){
        weight = 3
    }
    if(helper.dbType().isSupportSequence()){
        createSequence(sequenceName: 'hme_intercept_object_s', startValue:"1")
    }
    createTable(tableName: "hme_intercept_object", remarks: "拦截对象表") {
        column(name: "tenant_id", type: "bigint",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
        column(name: "intercept_object_id", type: "varchar(" + 100 * weight + ")",  remarks: "对象表主键ID")  {constraints(primaryKey: true)} 
        column(name: "intercept_id", type: "varchar(" + 100 * weight + ")",  remarks: "信息表主键ID")  {constraints(nullable:"false")}  
        column(name: "intercept_object", type: "varchar(" + 100 * weight + ")",  remarks: "拦截对象")  {constraints(nullable:"false")}  
        column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料ID")   
        column(name: "status", type: "varchar(" + 20 * weight + ")",  remarks: "状态")  {constraints(nullable:"false")}  
        column(name: "release_by", type: "bigint",  remarks: "放行人")   
        column(name: "release_date", type: "datetime",  remarks: "放行时间")   
        column(name: "cid", type: "bigint",  remarks: "CID")  {constraints(nullable:"false")}  
        column(name: "object_version_number", type: "bigint",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
        column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
        column(name: "created_by", type: "bigint",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
        column(name: "last_updated_by", type: "bigint",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
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

        
            addUniqueConstraint(columnNames:"intercept_id,intercept_object,material_id,tenant_id",tableName:"hme_intercept_object",constraintName: "hme_intercept_object_u1")
            }
}