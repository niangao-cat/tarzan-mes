package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_gen_status.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_gen_status") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_gen_status_s', startValue:"1")
        }
        createTable(tableName: "mt_gen_status", remarks: "状态") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "GEN_STATUS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "通用状态主键")  {constraints(primaryKey: true)} 
            column(name: "MODULE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "服务包")  {constraints(nullable:"false")}  
            column(name: "STATUS_GROUP", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "状态组编码")  {constraints(nullable:"false")}  
            column(name: "STATUS_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "状态编码")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "备注")   
            column(name: "DEFAULT_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "默认状态，Y/N")   
            column(name: "RELATION_TABLE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "关联对象")   
            column(name: "INITIAL_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "初始数据标识")   
            column(name: "SEQUENCE", type: "decimal(36,2)",  remarks: "顺序")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_gen_status", indexName: "MT_GEN_STATUS_N1") {
            column(name: "DEFAULT_FLAG")
        }

        addUniqueConstraint(columnNames:"STATUS_GROUP,STATUS_CODE,TENANT_ID",tableName:"mt_gen_status",constraintName: "MT_GEN_STATUS_U1")
    }
}