package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_disposition_group_member.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_disposition_group_member") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_disposition_group_member_s', startValue:"1")
        }
        createTable(tableName: "mt_disposition_group_member", remarks: "处置组分配") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "DISPOSITION_GROUP_MEMBER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "表ID，主键，供其他表做外键")  {constraints(primaryKey: true)} 
            column(name: "DISPOSITION_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "处置组ID")  {constraints(nullable:"false")}  
            column(name: "DISPOSITION_FUNCTION_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "处置方法")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(20)",  remarks: "序号")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"DISPOSITION_GROUP_ID,DISPOSITION_FUNCTION_ID,TENANT_ID",tableName:"mt_disposition_group_member",constraintName: "MT_DISPOSITION_GROUP_MEMBER_U1")
    }
}