package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_data_collect_line.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2020-07-17-hme_data_collect_line") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_data_collect_line_s', startValue:"1")
        }
        createTable(tableName: "hme_data_collect_line", remarks: "生产数据采集行表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "collect_line_id", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产数据采集行表主键")  {constraints(primaryKey: true)} 
            column(name: "collect_header_id", type: "varchar(" + 100 * weight + ")",  remarks: "生产数据采集行表主键")  {constraints(nullable:"false")}  
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位ID")  {constraints(nullable:"false")}  
            column(name: "shift_id", type: "varchar(" + 100 * weight + ")",  remarks: "班次ID")   
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "采集行物料ID")   
            column(name: "tag_group_id", type: "varchar(" + 100 * weight + ")",  remarks: "数据收集组ID")  {constraints(nullable:"false")}  
            column(name: "tag_id", type: "varchar(" + 100 * weight + ")",  remarks: "数据项ID")  {constraints(nullable:"false")}  
            column(name: "true_value", type: "varchar(" + 255 * weight + ")",  remarks: "符合值")   
            column(name: "false_value", type: "varchar(" + 255 * weight + ")",  remarks: "不符合值")   
            column(name: "minimum_value", type: "decimal(36,6)",  remarks: "最小值")   
            column(name: "maximal_value", type: "decimal(36,6)",  remarks: "最大值")   
            column(name: "standard", type: "varchar(" + 255 * weight + ")",  remarks: "标准值")   
            column(name: "group_purpose", type: "varchar(" + 30 * weight + ")",  remarks: "采集组用途")  {constraints(nullable:"false")}  
            column(name: "result", type: "varchar(" + 30 * weight + ")",  remarks: "采集结果")   
            column(name: "reference_point", type: "varchar(" + 255 * weight + ")",  remarks: "参考点")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute5", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}