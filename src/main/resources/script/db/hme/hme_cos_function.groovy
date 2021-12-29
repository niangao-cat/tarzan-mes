package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_cos_function.groovy') {
    changeSet(author: "wenzhang.yu@hand-china.com", id: "2020-08-18-hme_cos_function") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_cos_function_s', startValue:"1")
        }
        createTable(tableName: "hme_cos_function", remarks: "芯片性能表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")   
            column(name: "cos_function_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "load_sequence", type: "bigint(100)",  remarks: "芯片序列号")  {constraints(nullable:"false")}  
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "组织id")  {constraints(nullable:"false")}  
            column(name: "current", type: "varchar(" + 255 * weight + ")",  remarks: "电流")  {constraints(nullable:"false")}  
            column(name: "wavelength", type: "decimal(20,2)",  remarks: "波长(单点)")   
            column(name: "Spectral_width", type: "decimal(20,2)",  remarks: "光谱宽度(单点)")   
            column(name: "Wavelength_difference", type: "decimal(20,2)",  remarks: "波长差(两点差)")   
            column(name: "Voltage", type: "decimal(20,2)",  remarks: "电压(单点)")   
            column(name: "power_A", type: "decimal(20,2)",  remarks: "功率(单点)-A类")   
            column(name: "power_B", type: "decimal(20,2)",  remarks: "功率(单点)-B类")   
            column(name: "power_Abnormal", type: "decimal(20,2)",  remarks: "功率(单点)-功率异常")   
            column(name: "power_Low", type: "decimal(20,2)",  remarks: "功率(单点)-功率偏低")   
            column(name: "power_light_spot", type: "decimal(20,2)",  remarks: "功率(单点)-加强光电")   
            column(name: "Power_off", type: "decimal(20,2)",  remarks: "功率掉(两点差)")   
            column(name: "Wavelength_grade_A", type: "decimal(20,2)",  remarks: "波长等级(单点)-A级")   
            column(name: "Wavelength_grade_B", type: "decimal(20,2)",  remarks: "波长等级(单点)-B级")   
            column(name: "Wavelength_grade_C", type: "decimal(20,2)",  remarks: "波长等级(单点)-C级")   
            column(name: "Wavelength_grade_D", type: "decimal(20,2)",  remarks: "波长等级(单点)-D级")   
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
            column(name: "ATTRIBUTE6", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }

        addUniqueConstraint(columnNames:"site_id,current,load_sequence",tableName:"hme_cos_function",constraintName: "hme_cos_function_U1")
    }
}