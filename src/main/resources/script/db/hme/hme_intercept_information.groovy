package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_intercept_information.groovy') {
changeSet(author: "wengang.qiang@hand-china.com", id: "2021-09-11-hme_intercept_information") {
    def weight = 1
    if(helper.isSqlServer()){
        weight = 2
    } else if(helper.isOracle()){
        weight = 3
    }
    if(helper.dbType().isSupportSequence()){
        createSequence(sequenceName: 'hme_intercept_information_s', startValue:"1")
    }
    createTable(tableName: "hme_intercept_information", remarks: "拦截单信息表") {
        column(name: "tenant_id", type: "bigint",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
        column(name: "intercept_id", type: "varchar(" + 100 * weight + ")",  remarks: "信息表主键ID")  {constraints(primaryKey: true)} 
        column(name: "intercept_num", type: "varchar(" + 100 * weight + ")",  remarks: "拦截单号")  {constraints(nullable:"false")}  
        column(name: "dimension", type: "varchar(" + 100 * weight + ")",  remarks: "拦截维度")  {constraints(nullable:"false")}  
        column(name: "status", type: "varchar(" + 20 * weight + ")",  remarks: "状态")  {constraints(nullable:"false")}  
        column(name: "remark", type: "varchar(" + 255 * weight + ")",  remarks: "拦截消息")  {constraints(nullable:"false")}  
        column(name: "intercept_by", type: "bigint",  remarks: "拦截人")  {constraints(nullable:"false")}  
        column(name: "intercept_date", type: "datetime",  remarks: "拦截时间")  {constraints(nullable:"false")}  
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

        
                addUniqueConstraint(columnNames:"intercept_num,tenant_id",tableName:"hme_intercept_information",constraintName: "hme_intercept_information_u1")
        }
}