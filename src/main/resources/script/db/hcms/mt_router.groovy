package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_router.groovy') {
    changeSet(author: "liyuan.lv@hand-china.com", id: "2020-05-20-mt_router") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_router_s', startValue:"1")
        }
        createTable(tableName: "mt_router", remarks: "工艺路线") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线唯一标识")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点标识")  {constraints(nullable:"false")}  
            column(name: "ROUTER_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工艺路线简称")  {constraints(nullable:"false")}  
            column(name: "ROUTER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工艺路线类型")  {constraints(nullable:"false")}  
            column(name: "REVISION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工艺路线版本")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "物料清单描述")   
            column(name: "CURRENT_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "是否为当前版本")   
            column(name: "ROUTER_STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工艺路线状态")  {constraints(nullable:"false")}  
            column(name: "ORIGINAL_STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "来源状态")   
            column(name: "DATE_FROM", type: "datetime",  remarks: "生效时间")  {constraints(nullable:"false")}  
            column(name: "DATE_TO", type: "datetime",  remarks: "失效时间")   
            column(name: "BOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配清单ID")   
            column(name: "TEMPORARY_ROUTER_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "是否为临时工艺路线")   
            column(name: "RELAXED_FLOW_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "是否为松散工艺路线")   
            column(name: "HAS_BEEN_RELEASED_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "是否已经应用于EO")   
            column(name: "COPIED_FROM_ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "复制来源工艺路线标识")   
            column(name: "DISPOSITION_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "处置组")   
            column(name: "AUTO_REVISION_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "自动升版本标识")   
            column(name: "HOLD_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "当执行HOLD时，具体HOLD明细")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_router", indexName: "MT_ROUTER_N1") {
            column(name: "CURRENT_FLAG")
        }
   createIndex(tableName: "mt_router", indexName: "MT_ROUTER_N2") {
            column(name: "COPIED_FROM_ROUTER_ID")
        }
   createIndex(tableName: "mt_router", indexName: "MT_ROUTER_N3") {
            column(name: "DISPOSITION_GROUP_ID")
        }
   createIndex(tableName: "mt_router", indexName: "MT_ROUTER_N4") {
            column(name: "ROUTER_STATUS")
        }
   createIndex(tableName: "mt_router", indexName: "MT_ROUTER_N5") {
            column(name: "BOM_ID")
        }

        addUniqueConstraint(columnNames:"SITE_ID,ROUTER_NAME,ROUTER_TYPE,REVISION,TENANT_ID",tableName:"mt_router",constraintName: "MT_ROUTER_U1")
    }
}