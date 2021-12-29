package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_numrange_assign_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_numrange_assign_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_numrange_assign_his_s', startValue:"1")
        }
        createTable(tableName: "mt_numrange_assign_his", remarks: "号码段分配历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "NUMRANGE_ASSIGN_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "号码段分配历史表主键")  {constraints(primaryKey: true)} 
            column(name: "NUMRANGE_ASSIGN_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "号码段分配表主键")  {constraints(nullable:"false")}  
            column(name: "NUMRANGE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "号码段定义表主键")  {constraints(nullable:"false")}  
            column(name: "OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "编码对象ID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_TYPE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "对象类型ID")   
            column(name: "OBJECT_TYPE_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "对象类型编码")   
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")   
            column(name: "SITE", type: "varchar(" + 20 * weight + ")",   defaultValue:"",   remarks: "站点编码")   
            column(name: "EVENT_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"NUMRANGE_ASSIGN_ID,EVENT_ID,TENANT_ID",tableName:"mt_numrange_assign_his",constraintName: "MT_NUMRANGE_ASSIGN_HIS_U1")
    }
}