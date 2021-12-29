package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_pfep_shipping_category.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_pfep_shipping_category") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_pfep_shipping_category_s', startValue:"1")
        }
        createTable(tableName: "mt_pfep_shipping_category", remarks: "物料类别发运属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "PFEP_SHIPPING_CATEGORY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_CATEGORY_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料类别站点主键，标识唯一物料类别站点分配数据，限定为生产站点")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "组织类型，可选计划站点下区域、生产线、工作单元等类型")   
            column(name: "ORGANIZATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元")   
            column(name: "CUSTOMER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户")   
            column(name: "CUSTOMER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户地点")   
            column(name: "SHIPPING_CHANNEL", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "分销渠道")   
            column(name: "CUSTOMER_GOODS_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户物料号")   
            column(name: "PACKAGE_LIST_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "包装清单，关联包装类型装配清单")   
            column(name: "ATTACHMENT_LIST_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "附件清单，关联附件类型装配清单")   
            column(name: "TRANSPORT_MODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "运输方式")   
            column(name: "TRANSPORT_COMPANY", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "运输公司")   
            column(name: "SHIPPING_CALENDAR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "发货日历")   
            column(name: "PACKAGE_STRATEGY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "包装策略")   
            column(name: "PACKAGE_IDENTIFY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "包装标识，关联标识模板")   
            column(name: "PRODUCT_IDENTIFY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "产品标识，关联产品模板")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_pfep_shipping_category", indexName: "MT_PFEP_SHIPPING_CATEGORY_N1") {
            column(name: "ENABLE_FLAG")
        }
   createIndex(tableName: "mt_pfep_shipping_category", indexName: "MT_PFEP_SHIPPING_CATEGORY_N2") {
            column(name: "PACKAGE_LIST_ID")
        }
   createIndex(tableName: "mt_pfep_shipping_category", indexName: "MT_PFEP_SHIPPING_CATEGORY_N3") {
            column(name: "ATTACHMENT_LIST_ID")
        }
   createIndex(tableName: "mt_pfep_shipping_category", indexName: "MT_PFEP_SHIPPING_CATEGORY_N4") {
            column(name: "SHIPPING_CALENDAR_ID")
        }
   createIndex(tableName: "mt_pfep_shipping_category", indexName: "MT_PFEP_SHIPPING_CATEGORY_N5") {
            column(name: "PACKAGE_IDENTIFY_ID")
        }
   createIndex(tableName: "mt_pfep_shipping_category", indexName: "MT_PFEP_SHIPPING_CATEGORY_N6") {
            column(name: "PRODUCT_IDENTIFY_ID")
        }

        addUniqueConstraint(columnNames:"MATERIAL_CATEGORY_SITE_ID,ORGANIZATION_TYPE,ORGANIZATION_ID,CUSTOMER_ID,CUSTOMER_SITE_ID,TENANT_ID",tableName:"mt_pfep_shipping_category",constraintName: "MT_PFEP_SHIPPING_CATEGORY_U1")
    }
}