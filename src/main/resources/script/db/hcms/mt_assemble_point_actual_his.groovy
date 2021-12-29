package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_assemble_point_actual_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_assemble_point_actual_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_assemble_point_actual_his_s', startValue:"1")
        }
        createTable(tableName: "mt_assemble_point_actual_his", remarks: "装配点实绩历史，记录装配组下装配点实际装配物料和数量变更记录") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_POINT_ACTUAL_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "ASSEMBLE_POINT_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配点实绩ID，标识唯一一条装配点实际记录")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_GROUP_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配组ID")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_POINT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配点")  {constraints(nullable:"false")}  
            column(name: "FEEDING_SEQUENCE", type: "bigint(20)",  remarks: "装配点上料顺序（装载物料顺序？）")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配点装载物料")  {constraints(nullable:"false")}  
            column(name: "QTY", type: "decimal(36,6)",  remarks: "装配点当前装载物料数量")  {constraints(nullable:"false")}  
            column(name: "FEEDING_QTY", type: "decimal(36,6)",  remarks: "装配点上料初始数量")  {constraints(nullable:"false")}  
            column(name: "FEEDING_MATERIAL_LOT_SEQUENCE", type: "bigint(20)",  remarks: "上料批次顺序（一个点装载多个物料批时的顺序）")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配点装载物料批次")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "TRX_QTY", type: "decimal(36,6)",  remarks: "事务数量-当前数量")  {constraints(nullable:"false")}  
            column(name: "TRX_FEEDING_QTY", type: "decimal(36,6)",  remarks: "事务数量-初始装载数量")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"ASSEMBLE_POINT_ACTUAL_ID,EVENT_ID,TENANT_ID",tableName:"mt_assemble_point_actual_his",constraintName: "MT_ASS_POINT_ACT_HIS_U1")
    }
}