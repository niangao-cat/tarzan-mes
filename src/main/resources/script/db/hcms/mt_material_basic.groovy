package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_material_basic.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_material_basic") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_material_basic_s', startValue:"1")
        }
        createTable(tableName: "mt_material_basic", remarks: "物料业务属性表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料站点 ID ")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料 ID ")  {constraints(nullable:"false")}  
            column(name: "OLD_ITEM_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "旧物料号")   
            column(name: "LONG_DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "物料长描述")   
            column(name: "ITEM_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "物料类型")   
            column(name: "MAKE_BUY_CODE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "制造或采购")   
            column(name: "LOT_CONTROL_CODE", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "批次控制")   
            column(name: "QC_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "检验标志")   
            column(name: "RECEIVING_ROUTING_ID", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "接收方式")   
            column(name: "WIP_SUPPLY_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: " wip发料类型 ")   
            column(name: "VMI_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "vmi寄售标识")   
            column(name: "ITEM_GROUP", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料组")   
            column(name: "PRODUCT_GROUP", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "产品组")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}