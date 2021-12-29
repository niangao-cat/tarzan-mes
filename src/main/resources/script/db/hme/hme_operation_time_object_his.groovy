package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_operation_time_object_his.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2020-08-11-hme_operation_time_object_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_operation_time_object_his_s', startValue:"1")
        }
        createTable(tableName: "hme_operation_time_object_his", remarks: "时效要求关联对象历史表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "operation_time_object_his_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "operation_time_object_id", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "operation_time_id", type: "varchar(" + 100 * weight + ")",  remarks: "工艺时效要求主键")  {constraints(nullable:"false")}  
            column(name: "object_type", type: "varchar(" + 255 * weight + ")",  remarks: "对象类型")  {constraints(nullable:"false")}  
            column(name: "object_id", type: "varchar(" + 255 * weight + ")",  remarks: "对象ID")  {constraints(nullable:"false")}  
            column(name: "standard_reqd_time_in_process", type: "decimal(36,0)",  remarks: "时效要求时长")  {constraints(nullable:"false")}  
            column(name: "enable_flag", type: "varchar(" + 1 * weight + ")",  remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "event_id", type: "varchar(" + 100 * weight + ")",  remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "cid", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute5", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}