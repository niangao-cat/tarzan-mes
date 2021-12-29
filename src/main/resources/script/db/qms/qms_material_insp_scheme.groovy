package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_material_insp_scheme.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-qms_material_insp_scheme") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_material_insp_scheme_s', startValue:"1")
        }
        createTable(tableName: "qms_material_insp_scheme", remarks: "物料检验计划") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")   
            column(name: "inspection_scheme_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键id，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "site_id", type: "varchar(" + 50 * weight + ")",  remarks: "组织")  {constraints(nullable:"false")}  
            column(name: "material_category_id", type: "varchar(" + 50 * weight + ")",  remarks: "物料类别")   
            column(name: "material_id", type: "varchar(" + 50 * weight + ")",  remarks: "物料id")   
            column(name: "material_version", type: "varchar(" + 10 * weight + ")",  remarks: "物料版本")   
            column(name: "inspection_type", type: "varchar(" + 50 * weight + ")",  remarks: "检验类型")  {constraints(nullable:"false")}  
            column(name: "status", type: "varchar(" + 10 * weight + ")",  remarks: "状态")  {constraints(nullable:"false")}  
            column(name: "inspection_file", type: "varchar(" + 100 * weight + ")",  remarks: "检验文件号")  {constraints(nullable:"false")}  
            column(name: "file_version", type: "varchar(" + 10 * weight + ")",  remarks: "文件版本号")  {constraints(nullable:"false")}  
            column(name: "enable_flag", type: "varchar(" + 10 * weight + ")",  remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "publish_flag", type: "varchar(" + 10 * weight + ")",  remarks: "发布标识")  {constraints(nullable:"false")}  
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

        addUniqueConstraint(columnNames:"site_id,material_category_id,material_id,material_version,inspection_type",tableName:"qms_material_insp_scheme",constraintName: "qms_material_inspection_scheme_uk")
    }
}