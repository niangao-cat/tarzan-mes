package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_pqc_inspection_scheme.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2020-08-12-qms_pqc_inspection_scheme") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_pqc_inspection_scheme_s', startValue:"1")
        }
        createTable(tableName: "qms_pqc_inspection_scheme", remarks: "巡检检验计划") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "INSPECTION_SCHEME_ID", type: "varchar(" + 100 * weight + ")",  remarks: "主键id")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "组织")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_CATEGORY_ID", type: "varchar(" + 100 * weight + ")",  remarks: "物料类别")   
            column(name: "MATERIAL_ID", type: "varchar(" + 20 * weight + ")",  remarks: "物料")   
            column(name: "MATERIAL_VERSION", type: "varchar(" + 10 * weight + ")",  remarks: "物料版本")  {constraints(nullable:"false")}  
            column(name: "INSPECTION_TYPE", type: "varchar(" + 100 * weight + ")",  remarks: "物料版本")  {constraints(nullable:"false")}  
            column(name: "STATUS", type: "varchar(" + 10 * weight + ")",  remarks: "状态")  {constraints(nullable:"false")}  
            column(name: "INSPECTION_FILE", type: "varchar(" + 100 * weight + ")",  remarks: "检验文件号")  {constraints(nullable:"false")}  
            column(name: "FILE_VERSION", type: "varchar(" + 10 * weight + ")",  remarks: "文件版本号")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 10 * weight + ")",  remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "PUBLISH_FLAG", type: "varchar(" + 10 * weight + ")",  remarks: "发布标识")  {constraints(nullable:"false")}  
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")",  remarks: "扩展字段1")   
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")",  remarks: "扩展字段2")   
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")",  remarks: "扩展字段3")   
            column(name: "cid", type: "bigint(100)",  remarks: "cid")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,SITE_ID,MATERIAL_ID,MATERIAL_CATEGORY_ID,MATERIAL_VERSION,INSPECTION_TYPE",tableName:"qms_pqc_inspection_scheme",constraintName: "qms_pqc_inspection_scheme_u1")
    }
}