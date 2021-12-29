package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_locator_group.groovy') {
    changeSet(author: "liyuan.lv@hand-china.com", id: "2020-05-20-mt_mod_locator_group") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_locator_group_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_locator_group", remarks: "库位组") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "LOCATOR_GROUP_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "库位组编码")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_GROUP_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "库位组名称")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_mod_locator_group", indexName: "MT_MOD_LOCATOR_GROUP_N2") {
            column(name: "ENABLE_FLAG")
        }

        addUniqueConstraint(columnNames:"LOCATOR_GROUP_CODE,TENANT_ID",tableName:"mt_mod_locator_group",constraintName: "MT_MOD_LOCATOR_GROUP_U1")
        addUniqueConstraint(columnNames:"LOCATOR_GROUP_NAME,TENANT_ID",tableName:"mt_mod_locator_group",constraintName: "MT_MOD_LOCATOR_GROUP_U2")
    }
}