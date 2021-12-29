package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_error_message.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_error_message") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_error_message_s', startValue:"1")
        }
        createTable(tableName: "mt_error_message", remarks: "") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "MESSAGE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "消息主键ID，唯一性标识")  {constraints(primaryKey: true)} 
            column(name: "MESSAGE_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "消息编码")  {constraints(nullable:"false")}  
            column(name: "MESSAGE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "消息内容")   
            column(name: "MODULE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "服务包")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"MESSAGE_CODE,TENANT_ID",tableName:"mt_error_message",constraintName: "MT_ERROR_MESSAGE_U1")
    }
}