package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_eo_job_sn.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_eo_job_sn") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_eo_job_sn_s', startValue:"1")
        }
        createTable(tableName: "hme_eo_job_sn", remarks: "工序作业-eoSN作业记录表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "job_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "site_in_date", type: "datetime",  remarks: "进站日期")  {constraints(nullable:"false")}  
            column(name: "site_out_date", type: "datetime",  remarks: "出站日期")   
            column(name: "shift_id", type: "varchar(" + 100 * weight + ")",  remarks: "班次ID")   
            column(name: "site_in_by", type: "bigint(20)",  remarks: "操作员ID")  {constraints(nullable:"false")}  
            column(name: "site_out_by", type: "bigint(20)",  remarks: "出站人ID")   
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位ID")  {constraints(nullable:"false")}  
            column(name: "work_order_id", type: "varchar(" + 100 * weight + ")",  remarks: "工单ID")   
            column(name: "operation_id", type: "varchar(" + 100 * weight + ")",  remarks: "工艺ID")   
            column(name: "sn_material_id", type: "varchar(" + 100 * weight + ")",  remarks: "SN在制品ID")  {constraints(nullable:"false")}  
            column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "条码ID")   
            column(name: "eo_id", type: "varchar(" + 100 * weight + ")",  remarks: "EO")   
            column(name: "eo_step_id", type: "varchar(" + 100 * weight + ")",  remarks: "工艺步骤ID")   
            column(name: "eo_step_num", type: "int(9)",  remarks: "工艺步骤加工次数")   
            column(name: "job_container_id", type: "varchar(" + 100 * weight + ")",  remarks: "作业容器ID")   
            column(name: "rework_flag", type: "char(1)",  remarks: "是否返修标识")   
            column(name: "job_type", type: "varchar(" + 30 * weight + ")",  remarks: "作业平台类型")
            column(name: "source_container_id", type: "varchar(" + 100 * weight + ")",  remarks: "来源容器ID")
            column(name: "cid", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "hme_eo_job_sn", indexName: "hme_eo_job_sn_n1") {
            column(name: "workcell_id")
        }

    }
}