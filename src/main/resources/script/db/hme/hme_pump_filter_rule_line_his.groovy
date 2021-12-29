package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_pump_filter_rule_line_his.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2021-08-20-hme_pump_filter_rule_line_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_pump_filter_rule_line_his_s', startValue:"1")
        }
        createTable(tableName: "hme_pump_filter_rule_line_his", remarks: "泵浦源筛选规则行表历史表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "rule_line_his_id", type: "varchar(" + 100 * weight + ")",  remarks: "行表历史主键ID")  {constraints(primaryKey: true)} 
            column(name: "rule_head_his_id", type: "varchar(" + 100 * weight + ")",  remarks: "头表历史主键ID")  {constraints(nullable:"false")}
            column(name: "rule_line_id", type: "varchar(" + 100 * weight + ")",  remarks: "行表主键")  {constraints(nullable:"false")}  
            column(name: "rule_head_id", type: "varchar(" + 100 * weight + ")",  remarks: "头表主键")  {constraints(nullable:"false")}  
            column(name: "parameter_code", type: "varchar(" + 100 * weight + ")",  remarks: "参数代码")  {constraints(nullable:"false")}  
            column(name: "tag_id", type: "varchar(" + 100 * weight + ")",  remarks: "数据项ID")  {constraints(nullable:"false")}  
            column(name: "calculate_type", type: "varchar(" + 100 * weight + ")",  remarks: "计算类型")  {constraints(nullable:"false")}  
            column(name: "min_value", type: "decimal(36,6)",  remarks: "最小值")  {constraints(nullable:"false")}  
            column(name: "max_value", type: "decimal(36,6)",  remarks: "最大值")  {constraints(nullable:"false")}  
            column(name: "enable_flag", type: "varchar(" + 1 * weight + ")",  remarks: "启用标识。Y启用，N未启用")  {constraints(nullable:"false")}  
            column(name: "formula", type: "varchar(" + 255 * weight + ")",  remarks: "公式")
            column(name: "sequence", type: "bigint(20)",  remarks: "排序")
            column(name: "priority", type: "varchar(" + 100 * weight + ")",  remarks: "优先消耗")
            column(name: "cid", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
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
            column(name: "ATTRIBUTE11", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE12", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE13", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE14", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE15", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }
   createIndex(tableName: "hme_pump_filter_rule_line_his", indexName: "hme_pump_filter_rule_line_his_n1") {
            column(name: "rule_head_his_id")
            column(name: "tenant_id")
        }

    }
}