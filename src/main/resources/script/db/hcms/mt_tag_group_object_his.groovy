package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_tag_group_object_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_tag_group_object_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_tag_group_object_his_s', startValue:"1")
        }
        createTable(tableName: "mt_tag_group_object_his", remarks: "数据收集组关联对象历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "TAG_GROUP_OBJECT_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据收集组关联对象历史ID")  {constraints(primaryKey: true)} 
            column(name: "TAG_GROUP_OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据收集组关联对象ID")  {constraints(nullable:"false")}  
            column(name: "TAG_GROUP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据收集组ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料ID")   
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺ID")   
            column(name: "ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线ID")   
            column(name: "ROUTER_STEP_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线步骤ID")   
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工作单元ID")   
            column(name: "WORK_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "WO ID")   
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO ID")   
            column(name: "NC_CODE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "NC代码ID")   
            column(name: "BOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配清单ID")   
            column(name: "BOM_COMPONENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配清单行ID")   
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批ID")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TAG_GROUP_OBJECT_ID,EVENT_ID,TENANT_ID",tableName:"mt_tag_group_object_his",constraintName: "mt_tag_group_object_his_u1")
    }
}