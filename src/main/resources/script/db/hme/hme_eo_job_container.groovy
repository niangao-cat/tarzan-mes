package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_eo_job_container.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_eo_job_container") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_eo_job_container_s', startValue:"1")
        }
        createTable(tableName: "hme_eo_job_container", remarks: "工序作业平台-容器") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "job_container_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位ID")  {constraints(nullable:"false")}  
            column(name: "container_id", type: "varchar(" + 100 * weight + ")",  remarks: "容器ID")   
            column(name: "container_code", type: "varchar(" + 255 * weight + ")",  remarks: "容器条码")   
            column(name: "site_in_date", type: "datetime",  remarks: "进站日期")  {constraints(nullable:"false")}  
            column(name: "site_out_date", type: "datetime",  remarks: "出站日期")   
            column(name: "site_in_by", type: "bigint(20)",  remarks: "进站操作人ID")  {constraints(nullable:"false")}  
            column(name: "site_out_by", type: "bigint(20)",  remarks: "出操作人ID")   
            column(name: "cid", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "hme_eo_job_container", indexName: "hme_eo_job_container_n1") {
            column(name: "workcell_id")
        }

    }
}