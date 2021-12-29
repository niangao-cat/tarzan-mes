package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_pfep_distribution_category.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_pfep_distribution_category") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_pfep_distribution_category_s', startValue:"1")
        }
        createTable(tableName: "mt_pfep_distribution_category", remarks: "物料类别配送属性（一个WKC的物料类别和物料不能有从属关系）") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "PFEP_DISTRIBUTION_CATEGORY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，唯一标识")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_CATEGORY_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料类别站点主键，标识唯一物料类别站点对应关系，限定为生产站点")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "限定类型为工作单元")   
            column(name: "ORGANIZATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工作单元ID")   
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "线边库位")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_CAPACITY", type: "decimal(36,6)",  remarks: "物料在线边库位的容量")   
            column(name: "FROM_SCHEDULE_RATE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否采用装配件生产速率（按WO补货模式和按顺序补货模式可以采取装配件街拍，其它模式只能采用组件节拍）")   
            column(name: "MATERIAL_CONSUME_RATE_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料消耗的速率单位，统一用per/h单位，计算总数时，要将时间换算成h再进行计算")   
            column(name: "MATERIAL_CONSUME_RATE", type: "decimal(36,6)",  remarks: "物料消耗的速率@")   
            column(name: "BUFFER_INVENTORY", type: "decimal(36,6)",  remarks: "安全库存a")   
            column(name: "BUFFER_PERIOD", type: "decimal(36,6)",  remarks: "缓冲时间A,单位小时（h）")   
            column(name: "MIN_INVENTORY", type: "decimal(36,6)",  remarks: "最小库存")   
            column(name: "MAX_INVENTORY", type: "decimal(36,6)",  remarks: "最大库存")   
            column(name: "PACK_QTY", type: "decimal(36,6)",  remarks: "配送包装数")   
            column(name: "MULTIPLES_OF_PACK_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "配送数量是否为包装数的倍数")   
            column(name: "AREA_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "关联配送库位")   
            column(name: "AREA_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "关联配送路线，必输")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "有效性")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"MATERIAL_CATEGORY_SITE_ID,ORGANIZATION_ID,ORGANIZATION_TYPE,LOCATOR_ID,TENANT_ID",tableName:"mt_pfep_distribution_category",constraintName: "MT_PFEP_DISTRIBUTION_CATEGORY_U1")
    }
}