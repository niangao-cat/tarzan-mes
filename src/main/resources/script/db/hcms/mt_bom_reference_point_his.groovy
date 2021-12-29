package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_bom_reference_point_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_bom_reference_point_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_bom_reference_point_his_s', startValue:"1")
        }
        createTable(tableName: "mt_bom_reference_point_his", remarks: "装配清单行参考点关系历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "BOM_REFERENCE_POINT_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "历史表ID，主键，供其他表做外键")  {constraints(primaryKey: true)} 
            column(name: "BOM_REFERENCE_POINT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配清单行参考点ID")  {constraints(nullable:"false")}  
            column(name: "REFERENCE_POINT", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "装配清单行参考点描述")  {constraints(nullable:"false")}  
            column(name: "QTY", type: "decimal(36,6)",  remarks: "参考点数量")  {constraints(nullable:"false")}  
            column(name: "BOM_COMPONENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "参考点关联的BOM行ID")  {constraints(nullable:"false")}  
            column(name: "LINE_NUMBER", type: "bigint(100)",  remarks: "参考点序号")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "COPIED_FROM_POINT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "复制的来源参考点属性ID")   
            column(name: "EVENT_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,BOM_REFERENCE_POINT_ID,EVENT_ID",tableName:"mt_bom_reference_point_his",constraintName: "MT_BOM_REFERENCE_POINT_HIS_U1")
    }
}