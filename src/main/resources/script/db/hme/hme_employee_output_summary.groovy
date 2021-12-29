package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_employee_output_summary.groovy') {
    changeSet(author: "penglin.sui@hand-china.com", id: "2021-07-28-hme_employee_output_summary") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_employee_output_summary_s', startValue:"1")
        }
        createTable(tableName: "hme_employee_output_summary", remarks: "员工产量汇总表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "output_summary_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID")  {constraints(primaryKey: true)} 
            column(name: "job_time", type: "datetime",  remarks: "时间")  {constraints(nullable:"false")}  
            column(name: "user_id", type: "bigint(20)",  remarks: "用户ID")  {constraints(nullable:"false")}  
            column(name: "real_name", type: "varchar(" + 128 * weight + ")",  remarks: "姓名")  {constraints(nullable:"false")}  
            column(name: "login_name", type: "varchar(" + 128 * weight + ")",  remarks: "工号")  {constraints(nullable:"false")}  
            column(name: "prod_line_id", type: "varchar(" + 100 * weight + ")",  remarks: "产线ID")  {constraints(nullable:"false")}  
            column(name: "prod_line_code", type: "varchar(" + 255 * weight + ")",  remarks: "产线编码")  {constraints(nullable:"false")}  
            column(name: "prod_line_name", type: "varchar(" + 255 * weight + ")",  remarks: "产线名称")  {constraints(nullable:"false")}  
            column(name: "line_id", type: "varchar(" + 100 * weight + ")",  remarks: "工段ID")  {constraints(nullable:"false")}  
            column(name: "line_code", type: "varchar(" + 255 * weight + ")",  remarks: "工段编码")  {constraints(nullable:"false")}  
            column(name: "line_name", type: "varchar(" + 255 * weight + ")",  remarks: "工段描述")  {constraints(nullable:"false")}  
            column(name: "process_id", type: "varchar(" + 100 * weight + ")",  remarks: "工序ID")  {constraints(nullable:"false")}  
            column(name: "process_code", type: "varchar(" + 255 * weight + ")",  remarks: "工序编码")  {constraints(nullable:"false")}  
            column(name: "process_name", type: "varchar(" + 255 * weight + ")",  remarks: "工序描述")  {constraints(nullable:"false")}  
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料ID")  {constraints(nullable:"false")}  
            column(name: "material_code", type: "varchar(" + 255 * weight + ")",  remarks: "物料编码")  {constraints(nullable:"false")}  
            column(name: "material_name", type: "varchar(" + 4000 * weight + ")",  remarks: "物料描述")  {constraints(nullable:"false")}  
            column(name: "production_version", type: "varchar(" + 100 * weight + ")",  remarks: "生产版本")  {constraints(nullable:"false")}  
            column(name: "actual_output_qty", type: "decimal(36,6)",  remarks: "实际产出")  {constraints(nullable:"false")}  
            column(name: "output_qty", type: "decimal(36,6)",  remarks: "产量")  {constraints(nullable:"false")}  
            column(name: "nc_qty", type: "decimal(36,6)",  remarks: "不良数")  {constraints(nullable:"false")}  
            column(name: "rework_qty", type: "decimal(36,6)",  remarks: "返修数")  {constraints(nullable:"false")}  
            column(name: "total_duration", type: "decimal(36,6)",  remarks: "总时长")  {constraints(nullable:"false")}  
            column(name: "cid", type: "bigint(100)",  remarks: "cid")  {constraints(nullable:"false")}  
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
   createIndex(tableName: "hme_employee_output_summary", indexName: "hme_employee_output_summary_n1") {
            column(name: "job_time")
            column(name: "tenant_id")
        }
   createIndex(tableName: "hme_employee_output_summary", indexName: "hme_employee_output_summary_n2") {
            column(name: "prod_line_id")
            column(name: "tenant_id")
        }
   createIndex(tableName: "hme_employee_output_summary", indexName: "hme_employee_output_summary_n3") {
            column(name: "line_id")
            column(name: "tenant_id")
        }
   createIndex(tableName: "hme_employee_output_summary", indexName: "hme_employee_output_summary_n4") {
            column(name: "process_id")
            column(name: "tenant_id")
        }
   createIndex(tableName: "hme_employee_output_summary", indexName: "hme_employee_output_summary_n5") {
            column(name: "user_id")
            column(name: "tenant_id")
        }
   createIndex(tableName: "hme_employee_output_summary", indexName: "hme_employee_output_summary_n6") {
            column(name: "material_id")
            column(name: "tenant_id")
        }

    }
}