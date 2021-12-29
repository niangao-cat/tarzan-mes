package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_tag_daq_attr.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2020-07-21-hme_tag_daq_attr") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_tag_daq_attr_s', startValue:"1")
        }
        createTable(tableName: "hme_tag_daq_attr", remarks: "数据项数据采集扩展属性表") {
            column(name: "tag_daq_attr_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID")  {constraints(primaryKey: true)} 
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "tag_id", type: "varchar(" + 100 * weight + ")",  remarks: "数据项ID")  {constraints(nullable:"false")}  
            column(name: "equipment_category", type: "varchar(" + 100 * weight + ")",  remarks: "设备类别")  {constraints(nullable:"false")}  
            column(name: "value_field", type: "varchar(" + 100 * weight + ")",  remarks: "取值字段")  {constraints(nullable:"false")}  
            column(name: "limit_cond1", type: "varchar(" + 100 * weight + ")",  remarks: "限制条件1")   
            column(name: "cond1_value", type: "varchar(" + 100 * weight + ")",  remarks: "条件1限制值")   
            column(name: "limit_cond2", type: "varchar(" + 100 * weight + ")",   defaultValue:"1",   remarks: "限制条件2")   
            column(name: "cond2_value", type: "varchar(" + 100 * weight + ")",  remarks: "条件2限制值")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute5", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute6", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute7", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute8", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute9", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute10", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}