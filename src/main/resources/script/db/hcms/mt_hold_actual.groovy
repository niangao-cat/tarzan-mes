package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_hold_actual.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_hold_actual") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_hold_actual_s', startValue:"1")
        }
        createTable(tableName: "mt_hold_actual", remarks: "保留实绩") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "HOLD_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "保留ID，主键，供其他表做外键")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "HOLD_REASON_CODE", type: "varchar(" + 240 * weight + ")",   defaultValue:"",   remarks: "保留原因代码")   
            column(name: "COMMENTS", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "保留备注")   
            column(name: "EXPIRED_RELEASE_TIME", type: "datetime",  remarks: "保留备注")   
            column(name: "HOLD_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "保留类型（包括immediate, future)")  {constraints(nullable:"false")}  
            column(name: "HOLD_BY", type: "bigint(20)",  remarks: "保留人")  {constraints(nullable:"false")}  
            column(name: "HOLD_TIME", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "保留时间")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}