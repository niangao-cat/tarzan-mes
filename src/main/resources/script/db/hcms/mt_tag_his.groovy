package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_tag_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_tag_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_tag_his_s', startValue:"1")
        }
        createTable(tableName: "mt_tag_his", remarks: "数据收集项历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "TAG_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据项历史表ID")  {constraints(primaryKey: true)} 
            column(name: "TAG_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据项ID")  {constraints(nullable:"false")}  
            column(name: "TAG_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据项编码")  {constraints(nullable:"false")}  
            column(name: "TAG_DESCRIPTION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据项描述")   
            column(name: "REMARK", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "备注")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否启用")  {constraints(nullable:"false")}  
            column(name: "COLLECTION_METHOD", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据收集方式")   
            column(name: "VALUE_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "数据类型")  {constraints(nullable:"false")}  
            column(name: "TRUE_VALUE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "符合值")   
            column(name: "FALSE_VALUE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "不符合值")   
            column(name: "MINIMUM_VALUE", type: "decimal(36,6)",  remarks: "最小值")   
            column(name: "MAXIMAL_VALUE", type: "decimal(36,6)",  remarks: "最大值")   
            column(name: "UNIT", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "计量单位")   
            column(name: "VALUE_ALLOW_MISSING", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "允许缺失值")   
            column(name: "MANDATORY_NUM", type: "bigint(20)",  remarks: "必需的数据条数")   
            column(name: "OPTIONAL_NUM", type: "bigint(20)",  remarks: "可选的数据条数")   
            column(name: "API_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "转化API_ID")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TAG_ID,EVENT_ID,TENANT_ID",tableName:"mt_tag_his",constraintName: "mt_tag_his_u1")
    }
}