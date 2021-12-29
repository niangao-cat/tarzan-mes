package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_event_request.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_event_request") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_event_request_s', startValue:"1")
        }
        createTable(tableName: "mt_event_request", remarks: "事件请求记录") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EVENT_REQUEST_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件请求记录ID")  {constraints(primaryKey: true)} 
            column(name: "REQUEST_TYPE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件请求类型ID")  {constraints(nullable:"false")}  
            column(name: "REQUEST_BY", type: "bigint(20)",  remarks: "事件请求创建人")  {constraints(nullable:"false")}  
            column(name: "REQUEST_TIME", type: "datetime",  remarks: "事件请求创建时间")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}