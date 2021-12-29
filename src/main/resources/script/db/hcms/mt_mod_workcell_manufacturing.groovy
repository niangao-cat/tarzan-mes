package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_workcell_manufacturing.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_workcell_manufacturing") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_workcell_manufacturing_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_workcell_manufacturing", remarks: "工作单元生产属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_MANUFACTURING_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工作单元ID，标识唯一工作单元")  {constraints(nullable:"false")}  
            column(name: "FORWARD_SHIFT_NUMBER", type: "bigint(100)",  remarks: "可向前操作的班次数")   
            column(name: "BACKWARD_SHIFT_NUMBER", type: "bigint(100)",  remarks: "可向后操作的班次数")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"WORKCELL_ID,TENANT_ID",tableName:"mt_mod_workcell_manufacturing",constraintName: "MT_MOD_WORKCELL_MANU_U1")
    }
}