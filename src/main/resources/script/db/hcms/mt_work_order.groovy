package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_work_order.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_work_order") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_work_order_s', startValue:"1")
        }
        createTable(tableName: "mt_work_order", remarks: "生产指令") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "WORK_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产指令ID, 主键，供其他表做外键")  {constraints(primaryKey: true)} 
            column(name: "WORK_ORDER_NUM", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产指令编码")  {constraints(nullable:"false")}  
            column(name: "WORK_ORDER_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产指令类型")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "PRODUCTION_LINE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产线ID")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工作单元ID")   
            column(name: "MAKE_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "制造订单ID")   
            column(name: "PRODUCTION_VERSION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "生产版本")   
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料ID")   
            column(name: "QTY", type: "decimal(36,6)",  remarks: "数量")  {constraints(nullable:"false")}  
            column(name: "UOM_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "单位")  {constraints(nullable:"false")}  
            column(name: "PRIORITY", type: "bigint(100)",  remarks: "优先级")   
            column(name: "STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "生产指令状态")  {constraints(nullable:"false")}  
            column(name: "LAST_WO_STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "前次指令状态")   
            column(name: "PLAN_START_TIME", type: "datetime",  remarks: "计划开始时间")  {constraints(nullable:"false")}  
            column(name: "PLAN_END_TIME", type: "datetime",  remarks: "计划结束时间")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "默认完工库位ID，表示唯一货位")   
            column(name: "BOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配清单ID")   
            column(name: "ROUTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工艺路线ID")   
            column(name: "VALIDATE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "WO验证通过标记（Y/N）")  {constraints(nullable:"false")}  
            column(name: "REMARK", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "备注")   
            column(name: "OPPORTUNITY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "机会订单ID")   
            column(name: "CUSTOMER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产指令对应客户")   
            column(name: "COMPLETE_CONTROL_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "完工限制类型")   
            column(name: "COMPLETE_CONTROL_QTY", type: "decimal(36,6)",  remarks: "完工限制值")   
            column(name: "SOURCE_IDENTIFICATION_ID", type: "decimal(36,6)",  remarks: "外部来源标识Id")   
            column(name: "LATEST_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "最新一次新增历史表的主键")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"WORK_ORDER_NUM,TENANT_ID",tableName:"mt_work_order",constraintName: "MT_WORK_ORDER_U1")
    }
}