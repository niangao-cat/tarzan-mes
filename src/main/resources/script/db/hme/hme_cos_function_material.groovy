package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_cos_function_material.groovy') {
    changeSet(author: "penglin.sui@hand-china.com", id: "2021-06-24-hme_cos_function_material") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_cos_function_material_s', startValue:"1")
        }
        createTable(tableName: "hme_cos_function_material", remarks: "COS投料性能表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "COS_FUNCTION_MATERIAL_ID", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID")  {constraints(primaryKey: true)} 
            column(name: "LOAD_SEQUENCE", type: "varchar(" + 100 * weight + ")",  remarks: "芯片序列号")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "组织id")   
            column(name: "CURRENT", type: "varchar(" + 255 * weight + ")",  remarks: "电流")  {constraints(nullable:"false")}  
            column(name: "A01", type: "varchar(" + 255 * weight + ")",  remarks: "功率等级")  {constraints(nullable:"false")}  
            column(name: "A02", type: "decimal(20,3)",  remarks: "功率/w")  {constraints(nullable:"false")}  
            column(name: "A03", type: "varchar(" + 20 * weight + ")",  remarks: "波长等级")   
            column(name: "A04", type: "decimal(20,3)",  remarks: "波长/nm")   
            column(name: "A05", type: "decimal(20,3)",  remarks: "波长差/nm")   
            column(name: "A06", type: "decimal(20,3)",  remarks: "电压/V")   
            column(name: "A07", type: "decimal(20,3)",  remarks: "光谱宽度(单点)")   
            column(name: "A08", type: "varchar(" + 100 * weight + ")",  remarks: "设备资产编码")   
            column(name: "A09", type: "varchar(" + 100 * weight + ")",  remarks: "测试模式")   
            column(name: "A010", type: "decimal(20,3)",  remarks: "阈值电流")   
            column(name: "A011", type: "decimal(20,3)",  remarks: "阈值电压")   
            column(name: "A012", type: "decimal(20,3)",  remarks: "SE")   
            column(name: "A013", type: "decimal(20,3)",  remarks: "线宽")   
            column(name: "A014", type: "decimal(20,3)",  remarks: "光电转换效率")   
            column(name: "A15", type: "decimal(20,3)",  remarks: "偏振度数")   
            column(name: "A16", type: "decimal(20,3)",  remarks: "X半宽高")   
            column(name: "A17", type: "decimal(20,3)",  remarks: "Y半宽高")   
            column(name: "A18", type: "decimal(20,3)",  remarks: "X86能量宽度")   
            column(name: "A19", type: "decimal(20,3)",  remarks: "Y86能量宽度")   
            column(name: "A20", type: "decimal(20,3)",  remarks: "X95能量宽度")   
            column(name: "A21", type: "decimal(20,3)",  remarks: "Y95能量宽度")   
            column(name: "A22", type: "decimal(20,3)",  remarks: "透镜功率")   
            column(name: "A23", type: "decimal(20,3)",  remarks: "PBS功率")   
            column(name: "A24", type: "varchar(" + 100 * weight + ")",  remarks: "不良代码")   
            column(name: "A25", type: "varchar(" + 100 * weight + ")",  remarks: "操作者")   
            column(name: "A26", type: "varchar(" + 100 * weight + ")",  remarks: "备注")   
            column(name: "A27", type: "varchar(" + 100 * weight + ")",  remarks: "电压等级")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
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
   createIndex(tableName: "hme_cos_function_material", indexName: "hme_cos_function_material_n1") {
            column(name: "LOAD_SEQUENCE")
            column(name: "TENANT_ID")
        }

        addUniqueConstraint(columnNames:"SITE_ID,CURRENT,LOAD_SEQUENCE",tableName:"hme_cos_function_material",constraintName: "hme_cos_function_material_u1")
    }
}