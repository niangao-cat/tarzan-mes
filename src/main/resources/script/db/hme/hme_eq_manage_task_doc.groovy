package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_eq_manage_task_doc.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_eq_manage_task_doc") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_eq_manage_task_doc_s', startValue:"1")
        }
        createTable(tableName: "hme_eq_manage_task_doc", remarks: "设备管理任务单表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "task_doc_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "doc_num", type: "varchar(" + 100 * weight + ")",  remarks: "单据号")  {constraints(nullable:"false")}  
            column(name: "doc_type", type: "varchar(" + 100 * weight + ")",  remarks: "单据类型")  {constraints(nullable:"false")}  
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "组织id")  {constraints(nullable:"false")}  
            column(name: "equipment_id", type: "varchar(" + 100 * weight + ")",  remarks: "设备id")  {constraints(nullable:"false")}  
            column(name: "doc_status", type: "varchar(" + 100 * weight + ")",  remarks: "单据状态")  {constraints(nullable:"false")}  
            column(name: "task_cycle", type: "varchar(" + 255 * weight + ")",  remarks: "任务周期")   
            column(name: "check_result", type: "varchar(" + 255 * weight + ")",  remarks: "检验结果")   
            column(name: "shift_date", type: "date",  remarks: "班次日期")   
            column(name: "shift_code", type: "varchar(" + 100 * weight + ")",  remarks: "班次")   
            column(name: "check_date", type: "datetime",  remarks: "检验日期")   
            column(name: "wkc_id", type: "varchar(" + 100 * weight + ")",  remarks: "点检工位")   
            column(name: "check_by", type: "bigint(20)",  remarks: "检验人")   
            column(name: "confirm_by", type: "bigint(20)",  remarks: "确认人")   
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

        }

        addUniqueConstraint(columnNames:"doc_num,tenant_id",tableName:"hme_eq_manage_task_doc",constraintName: "hme_eq_manage_task_doc_u1")
    }
}