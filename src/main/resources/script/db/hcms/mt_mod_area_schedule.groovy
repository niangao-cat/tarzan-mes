package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_area_schedule.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_area_schedule") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_area_schedule_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_area_schedule", remarks: "区域计划属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "AREA_SCHEDULE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "AREA_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "区域ID，标识唯一区域")  {constraints(nullable:"false")}  
            column(name: "PLAN_START_TIME", type: "datetime",  remarks: "计划滚动起始时间")   
            column(name: "DEMAND_TIME_FENCE", type: "decimal(36,6)",  remarks: "需求时间栏(天)")   
            column(name: "FIX_TIME_FENCE", type: "decimal(36,6)",  remarks: "固定时间栏(天)")   
            column(name: "FROZEN_TIME_FENCE", type: "decimal(36,6)",  remarks: "冻结时间栏(天)")   
            column(name: "FORWARD_PLANNING_TIME_FENCE", type: "decimal(36,6)",  remarks: "顺排时间栏(天)")   
            column(name: "RELEASE_TIME_FENCE", type: "decimal(36,6)",  remarks: "下达时间栏(天)")   
            column(name: "ORDER_TIME_FENCE", type: "decimal(36,6)",  remarks: "订单时间栏(天)")   
            column(name: "BASIC_ALGORITHM", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "基础排程算法")   
            column(name: "FOLLOW_AREA_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "跟随区域")   
            column(name: "PROD_LINE_RULE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "选线规则")   
            column(name: "PHASE_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "区间类型")   
            column(name: "PLANNING_BASE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "排程类型")   
            column(name: "DELAY_TIME_FENCE", type: "decimal(36,6)",  remarks: "实际延迟时间")   
            column(name: "RELEASE_CONCURRENT_RULE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "关联下达策略")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_mod_area_schedule", indexName: "MT_MOD_AREA_SCHEDULE_N1") {
            column(name: "FOLLOW_AREA_ID")
        }

        addUniqueConstraint(columnNames:"AREA_ID,TENANT_ID",tableName:"mt_mod_area_schedule",constraintName: "MT_MOD_AREA_SCHEDULE_U1")
    }
}