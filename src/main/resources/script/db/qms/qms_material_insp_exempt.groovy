package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_material_insp_exempt.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-qms_material_insp_exempt") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_material_insp_exempt_s', startValue:"1")
        }
        createTable(tableName: "qms_material_insp_exempt", remarks: "物料免检表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EXEMPTION_ID", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "组织ID")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_ID", type: "varchar(" + 100 * weight + ")",  remarks: "供应商ID")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_SITE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "供应商地址ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",  remarks: "物料ID")  {constraints(nullable:"false")}  
            column(name: "TYPE", type: "varchar(" + 100 * weight + ")",  remarks: "免检类型")   
            column(name: "EXEMPTION_FLAG", type: "varchar(" + 1 * weight + ")",  remarks: "免检标识")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",  remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",  remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
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

        addUniqueConstraint(columnNames:"SITE_ID,SUPPLIER_ID,MATERIAL_ID,TENANT_ID",tableName:"qms_material_insp_exempt",constraintName: "qms_material_insp_exempt_u1")
    }
}