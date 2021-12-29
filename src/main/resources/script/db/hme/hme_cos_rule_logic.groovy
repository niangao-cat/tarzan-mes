package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_cos_rule_logic.groovy') {
    changeSet(author: "wenzhang.yu@hand-china.com", id: "2020-08-18-hme_cos_rule_logic") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_cos_rule_logic_s', startValue:"1")
        }
        createTable(tableName: "hme_cos_rule_logic", remarks: "芯片规则逻辑") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")   
            column(name: "cos_rule_logic_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "cos_rule_id", type: "varchar(" + 100 * weight + ")",  remarks: "头表id")  {constraints(nullable:"false")}  
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "rule_number", type: "varchar(" + 255 * weight + ")",  remarks: "规则")  {constraints(nullable:"false")}  
            column(name: "current", type: "varchar(" + 255 * weight + ")",  remarks: "电流")   
            column(name: "Collection_item", type: "varchar(" + 255 * weight + ")",  remarks: "采集项")  {constraints(nullable:"false")}  
            column(name: "count_type", type: "varchar(" + 255 * weight + ")",  remarks: "计算类型")  {constraints(nullable:"false")}  
            column(name: "Range_type", type: "varchar(" + 100 * weight + ")",  remarks: "范围类型")  {constraints(nullable:"false")}  
            column(name: "rule_value", type: "varchar(" + 100 * weight + ")",  remarks: "值")  {constraints(nullable:"false")}  
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

        addUniqueConstraint(columnNames:"site_id,rule_number,cos_rule_id",tableName:"hme_cos_rule_logic",constraintName: "hme_cos_rule_logic_u1")
    }
}