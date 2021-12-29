package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_eq_manage_tag.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_eq_manage_tag") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_eq_manage_tag_s', startValue:"1")
        }
        createTable(tableName: "hme_eq_manage_tag", remarks: "设备管理项目表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID（企业ID）")  {constraints(nullable:"false")}  
            column(name: "manage_tag_id", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(primaryKey: true)} 
            column(name: "manage_tag_group_id", type: "varchar(" + 100 * weight + ")",  remarks: "设备管理项目组ID")  {constraints(nullable:"false")}  
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "组织id")   
            column(name: "serial_number", type: "bigint(10)",  remarks: "排序码")   
            column(name: "tag_group_id", type: "varchar(" + 100 * weight + ")",  remarks: "项目组Id")   
            column(name: "tag_id", type: "varchar(" + 100 * weight + ")",  remarks: "项目ID")   
            column(name: "tag_code", type: "varchar(" + 255 * weight + ")",  remarks: "项目编码")  {constraints(nullable:"false")}  
            column(name: "tag_descriptions", type: "varchar(" + 255 * weight + ")",  remarks: "项目描述")  {constraints(nullable:"false")}  
            column(name: "value_type", type: "varchar(" + 255 * weight + ")",  remarks: "数据类型")  {constraints(nullable:"false")}  
            column(name: "collection_method", type: "varchar(" + 255 * weight + ")",  remarks: "收集方式")   
            column(name: "check_flag", type: "varchar(" + 1 * weight + ")",  remarks: "点检标识")   
            column(name: "maintain_flag", type: "varchar(" + 1 * weight + ")",  remarks: "保养标识")   
            column(name: "check_cycle", type: "varchar(" + 100 * weight + ")",  remarks: "点检周期")   
            column(name: "maintain_cycle", type: "varchar(" + 100 * weight + ")",  remarks: "保养周期")   
            column(name: "accuracy", type: "decimal(20,4)",  remarks: "精度")   
            column(name: "minimum_value", type: "decimal(20,4)",  remarks: "最小值")   
            column(name: "maximal_value", type: "decimal(20,4)",  remarks: "最大值")   
            column(name: "standard_value", type: "decimal(20,4)",  remarks: "标准值")   
            column(name: "uom_id", type: "varchar(" + 100 * weight + ")",  remarks: "计量单位")   
            column(name: "maintain_leadtime", type: "varchar(" + 100 * weight + ")",  remarks: "保养提前期")   
            column(name: "tool", type: "varchar(" + 100 * weight + ")",  remarks: "工具")   
            column(name: "responsible", type: "varchar(" + 100 * weight + ")",  remarks: "责任人")   
            column(name: "remark", type: "varchar(" + 1000 * weight + ")",  remarks: "备注")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 20 * weight + ")",  remarks: "有效性")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE1", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE6", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   

        }

    }
}