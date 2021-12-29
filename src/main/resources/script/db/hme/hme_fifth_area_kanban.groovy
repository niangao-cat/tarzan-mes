package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_fifth_area_kanban.groovy') {
    changeSet(author: "penglin.sui@hand-china.com", id: "2021-06-24-hme_fifth_area_kanban") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_fifth_area_kanban_s', startValue:"1")
        }
        createTable(tableName: "hme_fifth_area_kanban", remarks: "五部看板") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "FIFTH_AREA_KANBAN_ID", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID")  {constraints(primaryKey: true)} 
            column(name: "JOB_ID", type: "varchar(" + 100 * weight + ")",  remarks: "工序作业ID")  {constraints(nullable:"false")}  
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",  remarks: "EOID")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",  remarks: "工位ID")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_NAME", type: "varchar(" + 255 * weight + ")",  remarks: "工位")   
            column(name: "PROD_LINE_NAME", type: "varchar(" + 255 * weight + ")",  remarks: "产线名称")   
            column(name: "OPERATION_NAME", type: "varchar(" + 255 * weight + ")",  remarks: "工艺名称")   
            column(name: "SN", type: "varchar(" + 255 * weight + ")",  remarks: "SN")  {constraints(nullable:"false")}  
            column(name: "CHIP_TYPE", type: "varchar(" + 1 * weight + ")",  remarks: "芯片类型")  {constraints(nullable:"false")}  
            column(name: "WO_MATERIAL_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "工单物料编码")   
            column(name: "SN_MATERIAL_CODE", type: "varchar(" + 256 * weight + ")",  remarks: "SN物料编码")   
            column(name: "LAB_CODE", type: "varchar(" + 50 * weight + ")",  remarks: "实验代码")   
            column(name: "SITE_OUT_DATE", type: "datetime",  remarks: "出站时间")   
            column(name: "REWORK_FLAG", type: "varchar(" + 1 * weight + ")",  remarks: "返修标识")   
            column(name: "NC_PROCESS_METHOD", type: "varchar(" + 120 * weight + ")",  remarks: "不良处理方式")   
            column(name: "REAL_NAME", type: "varchar(" + 128 * weight + ")",  remarks: "出站人员")   
            column(name: "LOGIN_NAME", type: "varchar(" + 128 * weight + ")",  remarks: "工号")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
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
   createIndex(tableName: "hme_fifth_area_kanban", indexName: "hme_fifth_area_kanban_n1") {
            column(name: "OPERATION_NAME")
            column(name: "SITE_OUT_DATE")
            column(name: "TENANT_ID")
        }
   createIndex(tableName: "hme_fifth_area_kanban", indexName: "hme_fifth_area_kanban_n2") {
            column(name: "OPERATION_NAME")
            column(name: "SITE_OUT_DATE")
            column(name: "PROD_LINE_NAME")
            column(name: "TENANT_ID")
        }
   createIndex(tableName: "hme_fifth_area_kanban", indexName: "hme_fifth_area_kanban_n3") {
            column(name: "OPERATION_NAME")
            column(name: "SITE_OUT_DATE")
            column(name: "WORKCELL_NAME")
            column(name: "TENANT_ID")
        }
   createIndex(tableName: "hme_fifth_area_kanban", indexName: "hme_fifth_area_kanban_n4") {
            column(name: "OPERATION_NAME")
            column(name: "SITE_OUT_DATE")
            column(name: "WO_MATERIAL_CODE")
            column(name: "TENANT_ID")
        }
   createIndex(tableName: "hme_fifth_area_kanban", indexName: "hme_fifth_area_kanban_n5") {
            column(name: "OPERATION_NAME")
            column(name: "SITE_OUT_DATE")
            column(name: "REWORK_FLAG")
            column(name: "TENANT_ID")
        }
   createIndex(tableName: "hme_fifth_area_kanban", indexName: "hme_fifth_area_kanban_n6") {
            column(name: "OPERATION_NAME")
            column(name: "SITE_OUT_DATE")
            column(name: "CHIP_TYPE")
            column(name: "TENANT_ID")
        }
   createIndex(tableName: "hme_fifth_area_kanban", indexName: "hme_fifth_area_kanban_n7") {
            column(name: "SITE_OUT_DATE")
            column(name: "TENANT_ID")
        }

    }
}