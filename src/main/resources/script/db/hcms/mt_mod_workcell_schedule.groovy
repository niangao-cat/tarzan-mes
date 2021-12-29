package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_workcell_schedule.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_workcell_schedule") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_workcell_schedule_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_workcell_schedule", remarks: "工作单元计划属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_SCHEDULE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工作单元ID，标识唯一工作单元")  {constraints(nullable:"false")}  
            column(name: "RATE_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "速率类型")   
            column(name: "RATE", type: "decimal(36,6)",  remarks: "默认速率")   
            column(name: "ACTIVITY", type: "decimal(36,6)",  remarks: "开动率")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"WORKCELL_ID,TENANT_ID",tableName:"mt_mod_workcell_schedule",constraintName: "MT_MOD_WORKCELL_SCHEDULE_U1")
    }
}