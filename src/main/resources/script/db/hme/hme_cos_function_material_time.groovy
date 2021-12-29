package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_cos_function_material_time.groovy') {
    changeSet(author: "penglin.sui@hand-china.com", id: "2021-06-24-hme_cos_function_material_time") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_cos_function_material_time_s', startValue:"1")
        }
        createTable(tableName: "hme_cos_function_material_time", remarks: "COS投料性能时间记录表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "COS_FUNCTION_MATERIAL_TIME_ID", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID")  {constraints(primaryKey: true)} 
            column(name: "START_TIME", type: "datetime",  remarks: "开始时间")  {constraints(nullable:"false")}  
            column(name: "END_TIME", type: "datetime",  remarks: "结束时间")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
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