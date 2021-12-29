package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_nc_valid_oper.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_nc_valid_oper") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_nc_valid_oper_s', startValue:"1")
        }
        createTable(tableName: "mt_nc_valid_oper", remarks: "不良代码工艺分配") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "NC_VALID_OPER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "表ID，主键，供其他表做外键")  {constraints(primaryKey: true)} 
            column(name: "NC_OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "不良代码或不良代码组")  {constraints(nullable:"false")}  
            column(name: "NC_OBJECT_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "类型")  {constraints(nullable:"false")}  
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺ID")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "DISPOSITION_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "处置组")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"NC_OBJECT_TYPE,NC_OBJECT_ID,OPERATION_ID,TENANT_ID",tableName:"mt_nc_valid_oper",constraintName: "MT_NC_VALID_OPER_U1")
    }
}