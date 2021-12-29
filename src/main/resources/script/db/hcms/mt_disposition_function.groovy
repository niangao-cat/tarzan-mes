package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_disposition_function.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_disposition_function") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_disposition_function_s', startValue:"1")
        }
        createTable(tableName: "mt_disposition_function", remarks: "处置方法") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "DISPOSITION_FUNCTION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "表ID，主键，供其他表做外键")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产站点")  {constraints(nullable:"false")}  
            column(name: "DISPOSITION_FUNCTION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "处置方法")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "描述")   
            column(name: "ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线:与表ROUTER中字段ROUTER_ID对应。当FUNCTION_TYPE为.[NC_ROUTER]时必填")   
            column(name: "FUNCTION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "方法类型，包括以下类型（TYPE_GROUP:FUNCTION_TYPE）：1.[FUTURE_HOLD]未来保留;2.[REWORK]原地重工;3.[NC_ROUTER]处置工艺路线（可以是NC）;4.[IMIDIATE_HOLD]立即保留;方法类型，包括以下类型（TYPE_GROUP:FUNCTION_TYPE）：1.[FUTURE_HOLD]未来保留;2.[REWORK]原地重工;3.[NC_ROUTER]处置工艺路线（可以是NC）;4.[IMIDIATE_HOLD]立即保留;")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"SITE_ID,DISPOSITION_FUNCTION,FUNCTION_TYPE,ROUTER_ID,TENANT_ID",tableName:"mt_disposition_function",constraintName: "MT_DISPOSITION_FUNCTION_U1")
    }
}