package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_eo_job_sn_lot_material.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_eo_job_sn_lot_material") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_eo_job_sn_lot_material_s', startValue:"1")
        }
        createTable(tableName: "hme_eo_job_sn_lot_material", remarks: "工序作业-eo批次物料投料记录表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "表ID，主键")  {constraints(nullable:"false")}  
            column(name: "job_material_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "lot_material_id", type: "varchar(" + 100 * weight + ")",  remarks: "批次物料ID")   
            column(name: "time_material_id", type: "varchar(" + 100 * weight + ")",  remarks: "时效物料ID")   
            column(name: "material_type", type: "varchar(" + 30 * weight + ")",  remarks: "物料类型")   
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位ID")  {constraints(nullable:"false")}  
            column(name: "job_id", type: "varchar(" + 100 * weight + ")",  remarks: "工序作业ID")
            column(name: "eo_id", type: "varchar(" + 100 * weight + ")",  remarks: "EO")
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "组件物料ID")  {constraints(nullable:"false")}  
            column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "组件条码")  {constraints(nullable:"false")}  
            column(name: "release_qty", type: "decimal(36,6)",  remarks: "条码投料量")   
            column(name: "is_released", type: "tinyint(1)",  remarks: "是否已投")   
            column(name: "cid", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "hme_eo_job_sn_lot_material", indexName: "hme_eo_job_sn_lot_material_n1") {
            column(name: "workcell_id")
        }

    }
}