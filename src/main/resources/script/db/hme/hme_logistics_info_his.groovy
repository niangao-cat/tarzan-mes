package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_logistics_info_his.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2020-09-02-hme_logistics_info_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_logistics_info_his_s', startValue:"1")
        }
        createTable(tableName: "hme_logistics_info_his", remarks: "物流信息历史表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "logistics_info_his_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "logistics_info_id", type: "varchar(" + 100 * weight + ")",  remarks: "物流信息ID")  {constraints(nullable:"false")}  
            column(name: "logistics_number", type: "varchar(" + 255 * weight + ")",  remarks: "物流单号")  {constraints(nullable:"false")}  
            column(name: "logistics_company", type: "varchar(" + 100 * weight + ")",  remarks: "物流公司")  {constraints(nullable:"false")}  
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "站点")  {constraints(nullable:"false")}  
            column(name: "enable_flag", type: "varchar(" + 1 * weight + ")",   defaultValue:"Y",   remarks: "有效标志")  {constraints(nullable:"false")}  
            column(name: "batch_number", type: "varchar(" + 100 * weight + ")",  remarks: "批次")  {constraints(nullable:"false")}  
            column(name: "remark", type: "varchar(" + 1000 * weight + ")",  remarks: "备注")   
            column(name: "event_id", type: "varchar(" + 100 * weight + ")",  remarks: "事件ID")  {constraints(nullable:"false")}  
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

        }

    }
}