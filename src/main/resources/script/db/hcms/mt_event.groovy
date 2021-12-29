package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_event.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_event") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_event_s', startValue:"1")
        }
        createTable(tableName: "mt_event", remarks: "事件记录") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID，唯一性标识")  {constraints(primaryKey: true)} 
            column(name: "EVENT_TYPE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件类型ID")  {constraints(nullable:"false")}  
            column(name: "EVENT_BY", type: "bigint(100)",  remarks: "事件记录创建人")  {constraints(nullable:"false")}  
            column(name: "EVENT_TIME", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "事件记录创建时间")   
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件发生WKC ID")   
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件发生库位ID")   
            column(name: "PARENT_EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "父事件ID")   
            column(name: "EVENT_REQUEST_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件组ID")   
            column(name: "SHIFT_DATE", type: "date",  remarks: "事件所属班次日期")   
            column(name: "SHIFT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事件班次代码")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}