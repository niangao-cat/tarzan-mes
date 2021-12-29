package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_qualification.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_qualification") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_qualification_s', startValue:"1")
        }
        createTable(tableName: "hme_qualification", remarks: "资质基础信息表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "quality_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "quality_type", type: "varchar(" + 10 * weight + ")",  remarks: "资质类型")  {constraints(nullable:"false")}  
            column(name: "quality_code", type: "varchar(" + 255 * weight + ")",  remarks: "资质编码")  {constraints(nullable:"false")}  
            column(name: "quality_name", type: "varchar(" + 255 * weight + ")",  remarks: "资质名称")  {constraints(nullable:"false")}  
            column(name: "remark", type: "varchar(" + 1000 * weight + ")",  remarks: "资质备注")   
            column(name: "enable_flag", type: "varchar(" + 1 * weight + ")",  remarks: "有效性")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE_CATEGORY", type: "varchar(" + 30 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }

        addUniqueConstraint(columnNames:"tenant_id,quality_code",tableName:"hme_qualification",constraintName: "HME_QUALIFICATION_U1")
        addUniqueConstraint(columnNames:"tenant_id,quality_name",tableName:"hme_qualification",constraintName: "HME_QUALIFICATION_U2")
    }
}