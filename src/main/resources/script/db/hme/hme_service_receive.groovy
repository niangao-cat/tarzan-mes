package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_service_receive.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2020-09-01-hme_service_receive") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_service_receive_s', startValue:"1")
        }
        createTable(tableName: "hme_service_receive", remarks: "营销服务部接收拆箱登记表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "service_receive_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "logistics_info_id", type: "varchar(" + 100 * weight + ")",  remarks: "物流信息表ID")  {constraints(nullable:"false")}  
            column(name: "area_code", type: "varchar(" + 100 * weight + ")",  remarks: "区域编码（制造部）")   
            column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料批ID")   
            column(name: "sn_num", type: "varchar(" + 100 * weight + ")",  remarks: "sn")  {constraints(nullable:"false")}  
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料ID")   
            column(name: "production_version", type: "varchar(" + 100 * weight + ")",  remarks: "物料版本")   
            column(name: "receive_by", type: "bigint(20)",  remarks: "接收人")   
            column(name: "receive_date", type: "datetime",  remarks: "接收时间")   
            column(name: "remark", type: "varchar(" + 1000 * weight + ")",  remarks: "备注")   
            column(name: "receive_status", type: "varchar(" + 100 * weight + ")",  remarks: "接收状态")  {constraints(nullable:"false")}  
            column(name: "enable_flag", type: "varchar(" + 1 * weight + ")",   defaultValue:"Y",   remarks: "有效标志")   
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

        addUniqueConstraint(columnNames:"tenant_id,logistics_info_id,sn_num",tableName:"hme_service_receive",constraintName: "hme_service_receive_u1")
    }
}