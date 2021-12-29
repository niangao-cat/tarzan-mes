package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_user_organization.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_user_organization") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_user_organization_s', startValue:"1")
        }
        createTable(tableName: "mt_user_organization", remarks: "用户组织关系表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "USER_ORGANIZATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "用户组织关系ID")  {constraints(primaryKey: true)} 
            column(name: "USER_ID", type: "bigint(100)",  remarks: "用户ID")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "用户关联组织类型")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "用户关联组织对象ID")  {constraints(nullable:"false")}  
            column(name: "DEFAULT_ORGANIZATION_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "默认组织标识")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "有效性")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_user_organization", indexName: "MT_USER_ORGANIZATION_N1") {
            column(name: "ENABLE_FLAG")
        }
   createIndex(tableName: "mt_user_organization", indexName: "MT_USER_ORGANIZATION_N2") {
            column(name: "DEFAULT_ORGANIZATION_FLAG")
        }

        addUniqueConstraint(columnNames:"USER_ID,ORGANIZATION_TYPE,ORGANIZATION_ID,TENANT_ID",tableName:"mt_user_organization",constraintName: "MT_USER_ORGANIZATION_U1")
    }
}