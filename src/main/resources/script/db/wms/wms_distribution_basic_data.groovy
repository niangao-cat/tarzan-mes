package script.db

databaseChangeLog(logicalFilePath: 'script/db/wms_distribution_basic_data.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2020-07-21-wms_distribution_basic_data") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'wms_distribution_basic_data_s', startValue:"1")
        }
        createTable(tableName: "wms_distribution_basic_data", remarks: "配送基础数据表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "header_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料ID")
            column(name: "material_version", type: "varchar(" + 20 * weight + ")",  remarks: "物料版本")
            column(name: "material_group_id", type: "varchar(" + 30 * weight + ")",  remarks: "物料组ID")   
            column(name: "distribution_type", type: "varchar(" + 30 * weight + ")",  remarks: "策略类型")   
            column(name: "proportion", type: "decimal(10,0)",  remarks: "比例")   
            column(name: "inventory_level", type: "decimal(10,0)",  remarks: "库存水位")   
            column(name: "inventory_level_uom", type: "varchar(" + 30 * weight + ")",  remarks: "单位")   
            column(name: "every_qty", type: "decimal(36,6)",  remarks: "安全库存配送量")   
            column(name: "one_qty", type: "decimal(36,6)",  remarks: "单次配送量")   
            column(name: "one_time", type: "datetime",  remarks: "单次配送时间")   
            column(name: "one_uom", type: "varchar(" + 100 * weight + ")",  remarks: "单次配送单位")   
            column(name: "minimum_package_qty", type: "decimal(36,6)",  remarks: "最小包装量")   
            column(name: "enable_flag", type: "varchar(" + 1 * weight + ")",  remarks: "是否有效")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute5", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute6", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute7", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute8", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute9", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute10", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}