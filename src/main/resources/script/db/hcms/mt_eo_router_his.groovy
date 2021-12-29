package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_eo_router_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_eo_router_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_eo_router_his_s', startValue:"1")
        }
        createTable(tableName: "mt_eo_router_his", remarks: "EO工艺路线历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EO_ROUTER_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "EO_ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "执行作业工艺路线ID")  {constraints(nullable:"false")}  
            column(name: "ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线ID")  {constraints(nullable:"false")}  
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO主键，标识唯一EO")  {constraints(nullable:"false")}  
            column(name: "EVENT_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事件ID，关联唯一事件以获取同时影响的其他所有对象")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"EVENT_ID,EO_ID,TENANT_ID",tableName:"mt_eo_router_his",constraintName: "MT_EO_ROUTER_HIS_U1")
        addUniqueConstraint(columnNames:"EVENT_ID,EO_ROUTER_ID,TENANT_ID",tableName:"mt_eo_router_his",constraintName: "MT_EO_ROUTER_HIS_U2")
    }
}