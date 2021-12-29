package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_pqc_header_his.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2020-08-17-qms_pqc_header_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_pqc_header_his_s', startValue:"1")
        }
        createTable(tableName: "qms_pqc_header_his", remarks: "巡检单头历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "站点ID")   
            column(name: "PQC_HEADER_HIS_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单头历史表主键")  {constraints(primaryKey: true)} 
            column(name: "PQC_HEADER_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验单头表主键")  {constraints(nullable:"false")}  
            column(name: "PQC_NUMBER", type: "varchar(" + 50 * weight + ")",  remarks: "检验单号")  {constraints(nullable:"false")}  
            column(name: "WO_ID", type: "varchar(" + 100 * weight + ")",  remarks: "工单")  {constraints(nullable:"false")}  
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",  remarks: "EO_ID")   
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",  remarks: "物料批ID")   
            column(name: "QC_BY", type: "bigint(20)",  remarks: "检验员")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",  remarks: "物料ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_VERSION", type: "varchar(" + 20 * weight + ")",  remarks: "物料版本")   
            column(name: "INSPECTION_STATUS", type: "varchar(" + 50 * weight + ")",  remarks: "检验状态")  {constraints(nullable:"false")}  
            column(name: "PROD_LINE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "产线ID")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",  remarks: "工位ID")   
            column(name: "SHIFT_CODE", type: "varchar(" + 50 * weight + ")",  remarks: "班次")   
            column(name: "CREATED_DATE", type: "date",  remarks: "建单日期")  {constraints(nullable:"false")}  
            column(name: "INSPECTION_START_DATE", type: "date",  remarks: "检验开始时间")   
            column(name: "INSPECTION_FINISH_DATE", type: "date",  remarks: "完成时间")   
            column(name: "INSPECTION_TIME", type: "decimal(10,0)",  remarks: "检验时长（单位：小时）")   
            column(name: "INSPECTION_RESULT", type: "varchar(" + 50 * weight + ")",  remarks: "检验结果")   
            column(name: "REMARK", type: "varchar(" + 500 * weight + ")",  remarks: "备注")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",  remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "cid", type: "bigint(100)",  remarks: "cid")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
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