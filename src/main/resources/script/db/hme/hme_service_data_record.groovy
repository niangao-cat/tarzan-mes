package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_service_data_record.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2020-09-03-hme_service_data_record") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_service_data_record_s', startValue:"1")
        }
        createTable(tableName: "hme_service_data_record", remarks: "售后返品信息采集确认表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "service_data_record_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "service_receive_id", type: "varchar(" + 100 * weight + ")",  remarks: "售后接收信息主键")  {constraints(nullable:"false")}  
            column(name: "sn_num", type: "varchar(" + 100 * weight + ")",  remarks: "售后接收机器SN")  {constraints(nullable:"false")}  
            column(name: "logistics_number", type: "varchar(" + 100 * weight + ")",  remarks: "物流单号")  {constraints(nullable:"false")}  
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位ID")  {constraints(nullable:"false")}  
            column(name: "wkc_shift_id", type: "varchar(" + 100 * weight + ")",  remarks: "班次ID")   
            column(name: "operation_id", type: "varchar(" + 100 * weight + ")",  remarks: "工艺ID")   
            column(name: "tag_group_id", type: "varchar(" + 100 * weight + ")",  remarks: "收集组ID")  {constraints(nullable:"false")}  
            column(name: "business_type", type: "varchar(" + 100 * weight + ")",  remarks: "收集组类型")  {constraints(nullable:"false")}  
            column(name: "result", type: "varchar(" + 100 * weight + ")",  remarks: "采集结果")
            column(name: "remark", type: "varchar(" + 1000 * weight + ")",  remarks: "备注")   
            column(name: "attachment_uuid", type: "varchar(" + 100 * weight + ")",  remarks: "附件组ID")
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

        addUniqueConstraint(columnNames:"tenant_id,service_data_record_id,tag_group_id",tableName:"hme_service_data_record",constraintName: "hme_service_data_record_u1")
    }
}