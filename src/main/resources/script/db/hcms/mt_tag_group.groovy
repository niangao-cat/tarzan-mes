package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_tag_group.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_tag_group") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_tag_group_s', startValue:"1")
        }
        createTable(tableName: "mt_tag_group", remarks: "数据收集组表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "TAG_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据收集组ID")  {constraints(primaryKey: true)} 
            column(name: "TAG_GROUP_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据收集组编码")  {constraints(nullable:"false")}  
            column(name: "TAG_GROUP_DESCRIPTION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据收集组描述")   
            column(name: "TAG_GROUP_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "收集组类型")  {constraints(nullable:"false")}  
            column(name: "SOURCE_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "来源数据收集组ID")   
            column(name: "BUSINESS_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "业务类型")   
            column(name: "STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "状态")  {constraints(nullable:"false")}  
            column(name: "COLLECTION_TIME_CONTROL", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据收集时点")  {constraints(nullable:"false")}  
            column(name: "USER_VERIFICATION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "需要用户验证")   
            column(name: "LATEST_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "最新一次新增历史表的主键")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TAG_GROUP_CODE,TENANT_ID",tableName:"mt_tag_group",constraintName: "MT_TAG_GROUP_U1")
    }
}