package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_work_order_rel.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_work_order_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_work_order_rel_s', startValue:"1")
        }
        createTable(tableName: "mt_work_order_rel", remarks: "生产指令关系,标识生产指令的父子关系") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "WORK_ORDER_REL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "REL_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "关系类型：按BOM分解、按工艺路线分解、同层次拆分")  {constraints(nullable:"false")}  
            column(name: "PARENT_WORK_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "父生产指令ID")  {constraints(nullable:"false")}  
            column(name: "WORK_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产指令ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}