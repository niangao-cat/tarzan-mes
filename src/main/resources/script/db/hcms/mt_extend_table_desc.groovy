package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_extend_table_desc.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_extend_table_desc") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_extend_table_desc_s', startValue:"1")
        }
        createTable(tableName: "mt_extend_table_desc", remarks: "扩展说明表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EXTEND_TABLE_DESC_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "拓展表描述主键")  {constraints(primaryKey: true)} 
            column(name: "ATTR_TABLE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "拓展表")  {constraints(nullable:"false")}  
            column(name: "ATTR_TABLE_DESC", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "拓展表描述")  {constraints(nullable:"false")}  
            column(name: "SERVICE_PACKAGE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "服务包")  {constraints(nullable:"false")}  
            column(name: "MAIN_TABLE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主表")  {constraints(nullable:"false")}  
            column(name: "MAIN_TABLE_KEY", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主表主键")  {constraints(nullable:"false")}  
            column(name: "HIS_TABLE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "历史表")   
            column(name: "HIS_TABLE_KEY", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "历史表主键")   
            column(name: "HIS_ATTR_TABLE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "历史表扩展表")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "有效性")  {constraints(nullable:"false")}  
            column(name: "INITIAL_FLAG", type: "varchar(" + 1 * weight + ")",  remarks: "初始化")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ATTR_TABLE,TENANT_ID",tableName:"mt_extend_table_desc",constraintName: "MT_EXTEND_TABLE_DESC_U1")
    }
}