package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_data_collect_header.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2020-07-16-hme_data_collect_header") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_data_collect_header_s', startValue:"1")
        }
        createTable(tableName: "hme_data_collect_header", remarks: "生产数据采集头表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "collect_header_id", type: "varchar(" + 100 * weight + ")",  remarks: "生产数据采集头表主键")  {constraints(primaryKey: true)} 
            column(name: "shift_id", type: "varchar(" + 10 * weight + ")",  remarks: "班次ID")   
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "采集物料ID")  {constraints(nullable:"false")}  
            column(name: "qty", type: "decimal(36,6)",  remarks: "采集物料数量")  {constraints(nullable:"false")}  
            column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料批ID")   
            column(name: "data_record_code", type: "varchar(" + 100 * weight + ")",  remarks: "采集条码号")  {constraints(nullable:"false")}  
            column(name: "operation_id", type: "varchar(" + 100 * weight + ")",  remarks: "工艺ID")   
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位ID")  {constraints(nullable:"false")}  
            column(name: "site_in_by", type: "bigint(20)",  remarks: "进站人ID")  {constraints(nullable:"false")}  
            column(name: "site_out_by", type: "bigint(20)",  remarks: "出站人ID")   
            column(name: "site_in_date", type: "datetime",  remarks: "进站时间")  {constraints(nullable:"false")}  
            column(name: "site_out_date", type: "datetime",  remarks: "出站时间")   
            column(name: "data_collect_type", type: "varchar(" + 30 * weight + ")",  remarks: "数据采集类型")   
            column(name: "remark", type: "varchar(" + 1000 * weight + ")",  remarks: "备注")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
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

        addUniqueConstraint(columnNames:"data_record_code,workcell_id,tenant_id",tableName:"hme_data_collect_header",constraintName: "hme_data_collect_header_u1")
    }
}