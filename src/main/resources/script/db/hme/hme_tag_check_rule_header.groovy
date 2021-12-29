package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_tag_check_rule_header.groovy') {
changeSet(author: "wengang.qiang@hand-china.com", id: "2021-08-26-hme_tag_check_rule_header") {
    def weight = 1
    if(helper.isSqlServer()){
        weight = 2
    } else if(helper.isOracle()){
        weight = 3
    }
    if(helper.dbType().isSupportSequence()){
        createSequence(sequenceName: 'hme_tag_check_rule_header_s', startValue:"1")
    }
    createTable(tableName: "hme_tag_check_rule_header", remarks: "数据项展示规则维护头表") {
        column(name: "tenant_id", type: "bigint",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
        column(name: "header_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
        column(name: "business_id", type: "varchar(" + 100 * weight + ")",  remarks: "应用事业部")  {constraints(nullable:"false")}  
        column(name: "item_group_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料组ID")   
        column(name: "rule_code", type: "varchar(" + 100 * weight + ")",  remarks: "规则编码")  {constraints(nullable:"false")}  
        column(name: "rule_description", type: "varchar(" + 255 * weight + ")",  remarks: "规则描述")  {constraints(nullable:"false")}  
        column(name: "type", type: "varchar(" + 100 * weight + ")",  remarks: "类型")  {constraints(nullable:"false")}  
        column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "当前工序")   
        column(name: "enable_flag", type: "varchar(" + 1 * weight + ")",  remarks: "是否有效")  {constraints(nullable:"false")}  
        column(name: "cid", type: "bigint",  remarks: "CID")  {constraints(nullable:"false")}  
        column(name: "object_version_number", type: "bigint",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
        column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
        column(name: "created_by", type: "bigint",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
        column(name: "last_updated_by", type: "bigint",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
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

        
            addUniqueConstraint(columnNames:"business_id,rule_code,type,item_group_id,workcell_id,tenant_id",tableName:"hme_tag_check_rule_header",constraintName: "hme_tag_check_rule_header_u1")
            }
}