package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_equipment_wkc_rel.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_equipment_wkc_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_equipment_wkc_rel_s', startValue:"1")
        }
        createTable(tableName: "hme_equipment_wkc_rel", remarks: "设备工位关系表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID（企业ID）")  {constraints(nullable:"false")}  
            column(name: "EQUIPMENT_WKC_REL_ID", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(primaryKey: true)} 
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "站点id")  {constraints(nullable:"false")}  
            column(name: "equipment_id", type: "varchar(" + 100 * weight + ")",  remarks: "设备ID")  {constraints(nullable:"false")}  
            column(name: "business_id", type: "varchar(" + 100 * weight + ")",  remarks: "部门id")   
            column(name: "work_shop_id", type: "varchar(" + 100 * weight + ")",  remarks: "车间id")   
            column(name: "prod_line_id", type: "varchar(" + 100 * weight + ")",  remarks: "产线id")   
            column(name: "line_id", type: "varchar(" + 100 * weight + ")",  remarks: "工段id")   
            column(name: "process_id", type: "varchar(" + 100 * weight + ")",  remarks: "工序id")   
            column(name: "station_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位id")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 20 * weight + ")",  remarks: "等级编码有效性")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE1", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}