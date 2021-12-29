package script.db

databaseChangeLog(logicalFilePath: 'script/db/wms_put_in_storage_task.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-wms_put_in_storage_task") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'wms_put_in_storage_task_s', startValue:"1")
        }
        createTable(tableName: "wms_put_in_storage_task", remarks: "入库上架任务表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")   
            column(name: "task_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "task_type", type: "varchar(" + 30 * weight + ")",  remarks: "任务类型")  {constraints(nullable:"false")}  
            column(name: "instruction_doc_id", type: "varchar(" + 100 * weight + ")",  remarks: "入库上架关联单据ID")  {constraints(nullable:"false")}  
            column(name: "instruction_id", type: "varchar(" + 100 * weight + ")",  remarks: "入库上架关联单据行ID")  {constraints(nullable:"false")}  
            column(name: "task_status", type: "varchar(" + 30 * weight + ")",  remarks: "入库上架任务状态")  {constraints(nullable:"false")}  
            column(name: "instruction_doc_type", type: "varchar(" + 30 * weight + ")",  remarks: "关联单据类型")  {constraints(nullable:"false")}  
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料ID")  {constraints(nullable:"false")}  
            column(name: "task_qty", type: "decimal(20,6)",  remarks: "任务数量")  {constraints(nullable:"false")}  
            column(name: "execute_qty", type: "decimal(20,6)",  remarks: "执行数量")   
            column(name: "cid", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "wms_put_in_storage_task", indexName: "wms_put_in_storage_task_n1") {
            column(name: "instruction_id")
        }

    }
}