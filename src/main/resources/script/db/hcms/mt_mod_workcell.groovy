package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_workcell.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_workcell") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_workcell_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_workcell", remarks: "工作单元") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "WORKCELL_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工作单元编号")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工作单元名称")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "工作单元描述")   
            column(name: "WORKCELL_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工作单元类型")   
            column(name: "WORKCELL_LOCATION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工作单元位置")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_CATEGORY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工作单元分类")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_mod_workcell", indexName: "MT_MOD_WORKCELL_N1") {
            column(name: "ENABLE_FLAG")
        }
   createIndex(tableName: "mt_mod_workcell", indexName: "MT_MOD_WORKCELL_N2") {
            column(name: "WORKCELL_TYPE")
        }

        addUniqueConstraint(columnNames:"WORKCELL_CODE,TENANT_ID",tableName:"mt_mod_workcell",constraintName: "MT_MOD_WORKCELL_U1")
    }
}