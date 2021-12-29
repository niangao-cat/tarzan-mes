package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_uom.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_uom") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_uom_s', startValue:"1")
        }
        createTable(tableName: "mt_uom", remarks: "单位") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID,标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "UOM_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "单位类型")  {constraints(nullable:"false")}  
            column(name: "UOM_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"YYYY",   remarks: "单位编码")  {constraints(nullable:"false")}  
            column(name: "UOM_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "单位描述")  {constraints(nullable:"false")}  
            column(name: "PRIMARY_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "同类别主单位标识")   
            column(name: "CONVERSION_VALUE", type: "decimal(36,6)",  remarks: "与主单位换算关系，主单位/单位")  {constraints(nullable:"false")}  
            column(name: "DECIMAL_NUMBER", type: "bigint(100)",  remarks: "小数位数")  {constraints(nullable:"false")}  
            column(name: "PROCESS_MODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "尾数处理方式，包括进一法、四舍五入、去尾法")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_uom", indexName: "MT_UOM_N1") {
            column(name: "ENABLE_FLAG")
        }
   createIndex(tableName: "mt_uom", indexName: "MT_UOM_N2") {
            column(name: "UOM_TYPE")
        }
   createIndex(tableName: "mt_uom", indexName: "MT_UOM_N3") {
            column(name: "PRIMARY_FLAG")
        }

        addUniqueConstraint(columnNames:"UOM_CODE,TENANT_ID",tableName:"mt_uom",constraintName: "MT_UOM_U1")
    }
}