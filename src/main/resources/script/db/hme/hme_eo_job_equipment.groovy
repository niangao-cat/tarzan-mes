package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_eo_job_equipment.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_eo_job_equipment") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_eo_job_equipment_s', startValue:"1")
        }
        createTable(tableName: "hme_eo_job_equipment", remarks: "SN进出站设备状态记录表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户Id")  {constraints(nullable:"false")}  
            column(name: "job_equipment_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "job_id", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "equipment_id", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "equipment_status", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE1", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   

        }

    }
}