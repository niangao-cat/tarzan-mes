package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_locator.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_locator") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_locator_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_locator", remarks: "库位") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "LOCATOR_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "库位编码")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "库位名称")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_LOCATION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "货位位置")   
            column(name: "LOCATOR_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "库位类型")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "所属库位组ID")   
            column(name: "LENGTH", type: "decimal(36,6)",  remarks: "长")   
            column(name: "WIDTH", type: "decimal(36,6)",  remarks: "宽")   
            column(name: "HEIGHT", type: "decimal(36,6)",  remarks: "高")   
            column(name: "SIZE_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "货位的尺寸单位")   
            column(name: "MAX_WEIGHT", type: "decimal(36,6)",  remarks: "最大载重重量")   
            column(name: "WEIGHT_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "重量单位")   
            column(name: "MAX_CAPACITY", type: "decimal(36,6)",  remarks: "容量")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "PARENT_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "父层库位ID")   
            column(name: "LOCATOR_CATEGORY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "库位类别，区域/库存/地点类别")  {constraints(nullable:"false")}  
            column(name: "NEGATIVE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "是否允许负库存，标识库位库存是否允许为负值")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_mod_locator", indexName: "MT_MOD_LOCATOR_N1") {
            column(name: "LOCATOR_TYPE")
        }
   createIndex(tableName: "mt_mod_locator", indexName: "MT_MOD_LOCATOR_N2") {
            column(name: "SIZE_UOM_ID")
        }
   createIndex(tableName: "mt_mod_locator", indexName: "MT_MOD_LOCATOR_N3") {
            column(name: "WEIGHT_UOM_ID")
        }
   createIndex(tableName: "mt_mod_locator", indexName: "MT_MOD_LOCATOR_N4") {
            column(name: "ENABLE_FLAG")
        }
   createIndex(tableName: "mt_mod_locator", indexName: "MT_MOD_LOCATOR_N6") {
            column(name: "LOCATOR_GROUP_ID")
        }

        addUniqueConstraint(columnNames:"LOCATOR_CODE,TENANT_ID",tableName:"mt_mod_locator",constraintName: "MT_MOD_LOCATOR_U1")
    }
}