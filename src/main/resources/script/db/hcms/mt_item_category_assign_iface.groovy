package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_item_category_assign_iface.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_item_category_assign_iface") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_item_category_assign_iface_s', startValue:"1")
        }
        createTable(tableName: "mt_item_category_assign_iface", remarks: "物料与类别关系数据接口") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "IFACE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "PLANT_CODE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "工厂代码")  {constraints(nullable:"false")}  
            column(name: "ITEM_CODE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "物料编码")  {constraints(nullable:"false")}  
            column(name: "CATEGORY_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "类别代码")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "有效性")  {constraints(nullable:"false")}  
            column(name: "ERP_CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP创建日期")  {constraints(nullable:"false")}  
            column(name: "ERP_CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "ERP创建人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "ERP最后更新人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP最后更新日期")  {constraints(nullable:"false")}  
            column(name: "BATCH_ID", type: "decimal(36,6)",  remarks: "数据批次ID")  {constraints(nullable:"false")}  
            column(name: "STATUS", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据处理状态，初始为N，失败为E，成功S，处理中P")  {constraints(nullable:"false")}  
            column(name: "MESSAGE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "数据处理消息返回")   
            column(name: "CID", type: "bigint(20)",  remarks: "CID")   
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")   
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")   
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")   
            column(name: "ATTRIBUTE_CATEGORY", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "")   

        }

        addUniqueConstraint(columnNames:"PLANT_CODE,ITEM_CODE,CATEGORY_CODE,BATCH_ID",tableName:"mt_item_category_assign_iface",constraintName: "MT_ITEM_CATEGORY_ASSIGN_IFACE_U1")
    }
}