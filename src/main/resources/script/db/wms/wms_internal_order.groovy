package script.db

databaseChangeLog(logicalFilePath: 'script/db/wms_internal_order.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-wms_internal_order") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'wms_internal_order_s', startValue:"1")
        }
        createTable(tableName: "wms_internal_order", remarks: "内部订单表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "INTERNAL_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "内部订单id")  {constraints(primaryKey: true)} 
            column(name: "INTERNAL_ORDER", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "内部订单")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "MOVE_TYPE", type: "varchar(" + 100 * weight + ")",  remarks: "移动类型")   
            column(name: "MOVE_REASON", type: "varchar(" + 100 * weight + ")",  remarks: "移动原因")   
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "内部订单描述")  {constraints(nullable:"false")}  
            column(name: "DATE_FROM_TO", type: "datetime",  remarks: "生效时间")  {constraints(nullable:"false")}  
            column(name: "DATE_END_TO", type: "datetime",  remarks: "失效时间")   
            column(name: "SOURCE_IDENTIFICATION_ID", type: "decimal(36,6)",  remarks: "来源标识ID（Oracle同步写入账户别名ID）")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",  remarks: "有效性")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,INTERNAL_ORDER,SITE_ID",tableName:"wms_internal_order",constraintName: "WMS_INTERNAL_ORDER_U1")
    }
}