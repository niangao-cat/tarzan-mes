package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_nameplate_print_rel_line_his.groovy') {
changeSet(author: "wengang.qiang@hand-china", id: "2021-10-13-hme_nameplate_print_rel_line_his") {
    def weight = 1
    if(helper.isSqlServer()){
        weight = 2
    } else if(helper.isOracle()){
        weight = 3
    }
    if(helper.dbType().isSupportSequence()){
        createSequence(sequenceName: 'hme_nameplate_print_rel_line_his_s', startValue:"1")
    }
    createTable(tableName: "hme_nameplate_print_rel_line_his", remarks: "铭牌打印内部识别码对应关系行历史表") {
        column(name: "tenant_id", type: "bigint",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
        column(name: "nameplate_line_his_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID")  {constraints(primaryKey: true)} 
        column(name: "nameplate_header_his_id", type: "varchar(" + 100 * weight + ")",  remarks: "头历史表主键ID")  {constraints(nullable:"false")}  
        column(name: "nameplate_line_id", type: "varchar(" + 100 * weight + ")",  remarks: "头表主键ID")  {constraints(nullable:"false")}  
        column(name: "nameplate_header_id", type: "varchar(" + 100 * weight + ")",  remarks: "头表主键ID")  {constraints(nullable:"false")}  
        column(name: "code", type: "varchar(" + 100 * weight + ")",  remarks: "编码")  {constraints(nullable:"false")}  
        column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料ID")  {constraints(nullable:"false")}  
        column(name: "qty", type: "decimal(36,6)",  remarks: "数量")  {constraints(nullable:"false")}  
        column(name: "enable_flag", type: "varchar(" + 1 * weight + ")",  remarks: "有效性")  {constraints(nullable:"false")}  
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

        createIndex(tableName: "hme_nameplate_print_rel_line_his", indexName: "hme_nameplate_print_rel_line_his_n1") {
                column(name: "nameplate_line_id")
                column(name: "tenant_id")
            }
            createIndex(tableName: "hme_nameplate_print_rel_line_his", indexName: "hme_nameplate_print_rel_line_his_n2") {
                column(name: "nameplate_header_his_id")
                column(name: "tenant_id")
            }
        
                }
}