package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_area_day_plan_reach_rate.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2021-07-02-hme_area_day_plan_reach_rate") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_area_day_plan_reach_rate_s', startValue:"1")
        }
        createTable(tableName: "hme_area_day_plan_reach_rate", remarks: "制造部日计划达成率看板") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "day_plan_reach_rate_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "站点")  {constraints(nullable:"false")}  
            column(name: "area_id", type: "varchar(" + 100 * weight + ")",  remarks: "制造部主键")  {constraints(nullable:"false")}  
            column(name: "chart_date", type: "date",  remarks: "日期")  {constraints(nullable:"false")}  
            column(name: "material_code", type: "varchar(" + 255 * weight + ")",  remarks: "物料编码")  {constraints(nullable:"false")}  
            column(name: "material_name", type: "varchar(" + 255 * weight + ")",  remarks: "物料描述")  {constraints(nullable:"false")}  
            column(name: "dispatch_qty", type: "decimal(36,6)",  remarks: "派工数量")   
            column(name: "actual_deliver_qty", type: "decimal(36,6)",  remarks: "实际交付数量")   
            column(name: "plan_reach_rate", type: "varchar(" + 20 * weight + ")",  remarks: "达成率")   
            column(name: "cid", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
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
   createIndex(tableName: "hme_area_day_plan_reach_rate", indexName: "hme_area_day_plan_reach_rate_n1") {
            column(name: "area_id")
            column(name: "chart_date")
            column(name: "tenant_id")
        }

    }
}