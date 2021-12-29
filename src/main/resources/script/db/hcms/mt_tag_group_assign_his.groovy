package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_tag_group_assign_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_tag_group_assign_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_tag_group_assign_his_s', startValue:"1")
        }
        createTable(tableName: "mt_tag_group_assign_his", remarks: "数据收集项分配收集组历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "TAG_GROUP_ASSIGN_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据收集项分配收集组历史ID")  {constraints(primaryKey: true)} 
            column(name: "TAG_GROUP_ASSIGN_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据收集项分配收集组ID")  {constraints(nullable:"false")}  
            column(name: "TAG_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据收集组ID")  {constraints(nullable:"false")}  
            column(name: "SERIAL_NUMBER", type: "decimal(36,6)",  remarks: "序号")  {constraints(nullable:"false")}  
            column(name: "TAG_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据项ID")  {constraints(nullable:"false")}  
            column(name: "COLLECTION_METHOD", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据收集方式")   
            column(name: "VALUE_ALLOW_MISSING", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "允许缺失值")   
            column(name: "TRUE_VALUE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "符合值")   
            column(name: "FALSE_VALUE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "不符合值")   
            column(name: "MINIMUM_VALUE", type: "decimal(36,6)",  remarks: "最小值")   
            column(name: "MAXIMAL_VALUE", type: "decimal(36,6)",  remarks: "最大值")   
            column(name: "UNIT", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "计量单位")   
            column(name: "MANDATORY_NUM", type: "bigint(20)",  remarks: "必需的数据条数")   
            column(name: "OPTIONAL_NUM", type: "bigint(20)",  remarks: "可选的数据条数")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TAG_GROUP_ASSIGN_ID,EVENT_ID,TENANT_ID",tableName:"mt_tag_group_assign_his",constraintName: "mt_tag_group_assign_his_u1")
    }
}