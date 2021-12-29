package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_cos_rule_type.groovy') {
    changeSet(author: "wenzhang.yu@hand-china.com", id: "2020-08-18-hme_cos_rule_type") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_cos_rule_type_s', startValue:"1")
        }
        createTable(tableName: "hme_cos_rule_type", remarks: "芯片规则类型") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")   
            column(name: "cos_rule_type_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "cos_rule_id", type: "varchar(" + 100 * weight + ")",  remarks: "头表id")  {constraints(nullable:"false")}  
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "组织Id")  {constraints(nullable:"false")}  
            column(name: "cos_number", type: "bigint(100)",  remarks: "芯片路数")  {constraints(nullable:"false")}  
            column(name: "material_Id", type: "varchar(" + 255 * weight + ")",  remarks: "芯片料号")  {constraints(nullable:"false")}  
            column(name: "cos_type", type: "varchar(" + 255 * weight + ")",  remarks: "芯片类型")  {constraints(nullable:"false")}  
            column(name: "power_single_point", type: "varchar(" + 255 * weight + ")",  remarks: "功率/W（单点）")  {constraints(nullable:"false")}  
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
            column(name: "ATTRIBUTE6", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }

        addUniqueConstraint(columnNames:"site_id,cos_rule_id,cos_number",tableName:"hme_cos_rule_type",constraintName: "hme_cos_container_u1")
    }
}