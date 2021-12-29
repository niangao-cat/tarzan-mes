package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_pfep_inventory_category.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_pfep_inventory_category") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_pfep_inventory_category_s', startValue:"1")
        }
        createTable(tableName: "mt_pfep_inventory_category", remarks: "物料类别存储属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "PFEP_INVENTORY_CATEGORY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_CATEGORY_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料类别站点主键，标识唯一物料类别站点分配数据，限定为生产站点")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "组织类型，可选计划站点下区域、生产线、工作单元等类型")   
            column(name: "ORGANIZATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元")   
            column(name: "IDENTIFY_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "存储标识类型，如无标识、容器标识、物料批标识等")   
            column(name: "IDENTIFY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "标识模板，关联物料默认使用的标识模板")   
            column(name: "STOCK_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "默认存储库位")   
            column(name: "PACKAGE_LENGTH", type: "decimal(36,6)",  remarks: "存储包装长")   
            column(name: "PACKAGE_WIDTH", type: "decimal(36,6)",  remarks: "存储包装宽")   
            column(name: "PACKAGE_HEIGHT", type: "decimal(36,6)",  remarks: "存储包装高")   
            column(name: "PACKAGE_SIZE_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "包装尺寸单位，如MMCMM等，与单位维护保持一致")   
            column(name: "PACKAGE_WEIGHT", type: "decimal(36,6)",  remarks: "存储包装重量")   
            column(name: "WEIGHT_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "重量单位，如KG/G/T等")   
            column(name: "MAX_STOCK_QTY", type: "decimal(36,6)",  remarks: "最大存储库存")   
            column(name: "MIN_STOCK_QTY", type: "decimal(36,6)",  remarks: "最小存储库存")   
            column(name: "ISSUED_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "默认发料库位")   
            column(name: "COMPLETION_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "默认完工库位")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_pfep_inventory_category", indexName: "MT_PFEP_INVENTORY_CATEGORY_N1") {
            column(name: "ENABLE_FLAG")
        }
   createIndex(tableName: "mt_pfep_inventory_category", indexName: "MT_PFEP_INVENTORY_CATEGORY_N2") {
            column(name: "IDENTIFY_ID")
        }
   createIndex(tableName: "mt_pfep_inventory_category", indexName: "MT_PFEP_INVENTORY_CATEGORY_N3") {
            column(name: "IDENTIFY_TYPE")
        }
   createIndex(tableName: "mt_pfep_inventory_category", indexName: "MT_PFEP_INVENTORY_CATEGORY_N4") {
            column(name: "STOCK_LOCATOR_ID")
        }
   createIndex(tableName: "mt_pfep_inventory_category", indexName: "MT_PFEP_INVENTORY_CATEGORY_N5") {
            column(name: "PACKAGE_SIZE_UOM_ID")
        }
   createIndex(tableName: "mt_pfep_inventory_category", indexName: "MT_PFEP_INVENTORY_CATEGORY_N6") {
            column(name: "WEIGHT_UOM_ID")
        }
   createIndex(tableName: "mt_pfep_inventory_category", indexName: "MT_PFEP_INVENTORY_CATEGORY_N7") {
            column(name: "ISSUED_LOCATOR_ID")
        }
   createIndex(tableName: "mt_pfep_inventory_category", indexName: "MT_PFEP_INVENTORY_CATEGORY_N8") {
            column(name: "COMPLETION_LOCATOR_ID")
        }

        addUniqueConstraint(columnNames:"MATERIAL_CATEGORY_SITE_ID,ORGANIZATION_TYPE,ORGANIZATION_ID,TENANT_ID",tableName:"mt_pfep_inventory_category",constraintName: "MT_PFEP_INVENTORY_CATEGORY_U1")
    }
}