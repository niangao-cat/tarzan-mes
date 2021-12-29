package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_employee_assign.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_employee_assign") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_employee_assign_s', startValue:"1")
        }
        createTable(tableName: "hme_employee_assign", remarks: "人员与资质关系表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "employee_assign_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "employee_id", type: "varchar(" + 100 * weight + ")",  remarks: "员工主键")  {constraints(nullable:"false")}  
            column(name: "quality_id", type: "varchar(" + 100 * weight + ")",  remarks: "资质主键")  {constraints(nullable:"false")}  
            column(name: "proficiency", type: "varchar(" + 10 * weight + ")",  remarks: "资质熟练度")  {constraints(nullable:"false")}  
            column(name: "date_from", type: "datetime",  remarks: "有效期起")   
            column(name: "date_to", type: "datetime",  remarks: "有效期止")   
            column(name: "enable_flag", type: "varchar(" + 1 * weight + ")",  remarks: "有效性")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE_CATEGORY", type: "varchar(" + 30 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }

        addUniqueConstraint(columnNames:"tenant_id,employee_id,quality_id",tableName:"hme_employee_assign",constraintName: "HME_EMPLOYEE_ASSIGN_UI")
    }
}