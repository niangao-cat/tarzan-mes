package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_material_tag_group_rel.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-qms_material_tag_group_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_material_tag_group_rel_s', startValue:"1")
        }
        createTable(tableName: "qms_material_tag_group_rel", remarks: "物料与检验组关系表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")   
            column(name: "tag_group_rel_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键id，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "scheme_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料检验计划主键id")  {constraints(nullable:"false")}  
            column(name: "tag_group_id", type: "varchar(" + 100 * weight + ")",  remarks: "检验组id")  {constraints(nullable:"false")}  
            column(name: "remark", type: "varchar(" + 500 * weight + ")",  remarks: "备注")   
            column(name: "cid", type: "bigint(100)",  remarks: "cid")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "attribute_category", type: "varchar(" + 30 * weight + ")",  remarks: "")   
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

        }

    }
}