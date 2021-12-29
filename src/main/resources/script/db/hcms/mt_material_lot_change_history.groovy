package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_material_lot_change_history.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_material_lot_change_history") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_material_lot_change_history_s', startValue:"1")
        }
        createTable(tableName: "mt_material_lot_change_history", remarks: "物料批变更历史，记录物料批拆分合并的变更情况") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_LOT_CHANGE_HISTORY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "作为物料批变更更历史唯一标识，用于其他数据结构引用")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_LOT_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批历史ID")   
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "表示发生拆分合并变更后的物料批唯一标识ID")  {constraints(nullable:"false")}  
            column(name: "SOURCE_MATERIAL_LOT_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "作为一次变更来源物料批，如拆分合并的来源物料批")  {constraints(nullable:"false")}  
            column(name: "REASON", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "表示发生此次变更的原因：P：拆分;M：合并，合并时可能记录多条记录")  {constraints(nullable:"false")}  
            column(name: "SEQUENCE", type: "bigint(20)",  remarks: "物料批存在多次变更记录，此处记录物料批变更顺序")  {constraints(nullable:"false")}  
            column(name: "EVENT_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事务ID，用于表示一次变更操作")  {constraints(nullable:"false")}  
            column(name: "TRX_QTY", type: "decimal(20,6)",  remarks: "变更数量")  {constraints(nullable:"false")}  
            column(name: "SOURCE_TRX_QTY", type: "decimal(20,6)",  remarks: "来源执行作业变更数量")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"MATERIAL_LOT_ID,EVENT_ID,SEQUENCE,TENANT_ID",tableName:"mt_material_lot_change_history",constraintName: "MT_MATERIAL_LOT_CHANGE_HIS_U1")
    }
}