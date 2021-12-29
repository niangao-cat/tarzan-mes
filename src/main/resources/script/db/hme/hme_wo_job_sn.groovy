package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_wo_job_sn.groovy') {
    changeSet(author: "wenzhang.yu@hand-china.com", id: "2020-08-12-hme_wo_job_sn") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_wo_job_sn_s', startValue:"1")
        }
        createTable(tableName: "hme_wo_job_sn", remarks: "wo工艺作业记录表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "wo_job_sn_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "工厂ID")  {constraints(nullable:"false")}  
            column(name: "work_order_id", type: "varchar(" + 100 * weight + ")",  remarks: "工单id")  {constraints(nullable:"false")}  
            column(name: "operation_id", type: "varchar(" + 100 * weight + ")",  remarks: "工艺id")  {constraints(nullable:"false")}  
            column(name: "site_in_num", type: "bigint(20)",  defaultValue:"0",   remarks: "进站总数")  {constraints(nullable:"false")}
            column(name: "unqualified_num", type: "bigint(20)",   defaultValue:"0",   remarks: "不合格数")   
            column(name: "processed_num", type: "bigint(20)",  defaultValue:"0",   remarks: "已加工数")
            column(name: "cid", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
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
            column(name: "ATTRIBUTE11", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE12", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE13", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE14", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE15", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }

        addUniqueConstraint(columnNames:"tenant_id,site_id,work_order_id,operation_id",tableName:"hme_wo_job_sn",constraintName: "hme_wo_job_sn_u1")
    }
}