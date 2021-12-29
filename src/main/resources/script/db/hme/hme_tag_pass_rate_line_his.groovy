package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_tag_pass_rate_line_his.groovy') {
changeSet(author: "wengang.qiang@hand-china.com", id: "2021-09-22-hme_tag_pass_rate_line_his") {
    def weight = 1
    if(helper.isSqlServer()){
        weight = 2
    } else if(helper.isOracle()){
        weight = 3
    }
    if(helper.dbType().isSupportSequence()){
        createSequence(sequenceName: 'hme_tag_pass_rate_line_his_s', startValue:"1")
    }
    createTable(tableName: "hme_tag_pass_rate_line_his", remarks: "偏振度和发散角良率维护行历史表") {
        column(name: "tenant_id", type: "bigint",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
        column(name: "line_his_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
        column(name: "header_his_id", type: "varchar(" + 100 * weight + ")",  remarks: "头表历史主键")  {constraints(nullable:"false")}  
        column(name: "header_id", type: "varchar(" + 100 * weight + ")",  remarks: "偏振度和发散角良率维护头表ID")  {constraints(nullable:"false")}  
        column(name: "line_id", type: "varchar(" + 100 * weight + ")",  remarks: "偏振度和发散角良率维护行表ID")  {constraints(nullable:"false")}  
        column(name: "add_pass_rate", type: "decimal(10,0)",  remarks: "加测目标良率")   
        column(name: "test_sum_qty", type: "bigint",  remarks: "测试总量")  {constraints(nullable:"false")}  
        column(name: "priority", type: "bigint",  remarks: "优先级")  {constraints(nullable:"false")}  
        column(name: "remark", type: "varchar(" + 255 * weight + ")",  remarks: "备注")   
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

        createIndex(tableName: "hme_tag_pass_rate_line_his", indexName: "hme_tag_pass_rate_line_his_n1") {
                column(name: "line_id")
                column(name: "tenant_id")
            }
        
            }
}