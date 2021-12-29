package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_pfep_schedule_category.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_pfep_schedule_category") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_pfep_schedule_category_s', startValue:"1")
        }
        createTable(tableName: "mt_pfep_schedule_category", remarks: "物料类别计划属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "PFEP_SCHEDULE_CATEGORY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_CATEGORY_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料类别站点主键，标识唯一物料类别站点分配数据，限定为计划站点")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "组织类型，可选计划站点下区域、生产线、工作单元等类型")   
            column(name: "ORGANIZATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元")   
            column(name: "KEY_COMPONENT_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "关键组件标识，标识计算订单投入时，物料是否关键组件")   
            column(name: "SCHEDULE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "参与排产标识，只有标识为“是”的物料才能参与计划排程")   
            column(name: "MAKE_TO_ORDER_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否按单生产，标识物料是否按单生产，只有按单生产物料才可以通过需求或订单分解创建下层订单")   
            column(name: "PROD_LINE_RULE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "选线规则，如N、Z、N+Z、Z+等规则")   
            column(name: "PRE_PROCESSING_LEAD_TIME", type: "decimal(36,6)",  remarks: "前处理提前期（小时）")   
            column(name: "PROCESSING_LEAD_TIME", type: "decimal(36,6)",  remarks: "处理提前期（小时）")   
            column(name: "POST_PROCESSING_LEAD_TIME", type: "decimal(36,6)",  remarks: "后处理提前期（小时）")   
            column(name: "SAFETY_LEAD_TIME", type: "decimal(36,6)",  remarks: "安全生产周期（小时）")   
            column(name: "EXCEED_LEAD_TIME", type: "decimal(36,6)",  remarks: "最大提前生产周期（天）")   
            column(name: "DEMAND_TIME_FENCE", type: "decimal(36,6)",  remarks: "需求时间栏（天），通过设置需求时间栏的方式对进入计划排程的需求进行限定")   
            column(name: "RELEASE_TIME_FENCE", type: "decimal(36,6)",  remarks: "下达时间栏（天），通过设置下达时间栏来防止计划过早下达")   
            column(name: "ORDER_TIME_FENCE", type: "decimal(36,6)",  remarks: "订单时间栏（天），通过设置订单时间栏来限定下层物料计划的下达")   
            column(name: "DEMAND_MERGE_TIME_FENCE", type: "decimal(36,6)",  remarks: "需求合并时间栏，通过设置需求合并时间栏来将一定时间内（比如1天或3天或1周）的需求合并进行批量生产")   
            column(name: "SUPPLY_MERGE_TIME_FENCE", type: "decimal(36,6)",  remarks: "供应合并时间栏，通过设置供应合并时间栏来实现某个时间范围内的供应满足需求")   
            column(name: "SAFETY_STOCK_METHOD", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "安全库存方法")   
            column(name: "SAFETY_STOCK_PERIOD", type: "decimal(36,6)",  remarks: "安全库存周期（天）")   
            column(name: "SAFETY_STOCK_VALUE", type: "decimal(36,6)",  remarks: "安全库存")   
            column(name: "ECONOMIC_LOT_SIZE", type: "decimal(36,6)",  remarks: "经济批量，在综合考虑产能、计划的可能变更、生产组织管理等因素下确定的一次生产数量")   
            column(name: "ECONOMIC_SPLIT_PARAMETER", type: "decimal(36,6)",  remarks: "批量舍入比例%，用于确定经济批量的多少比例需要进行合并，避免出现按批量拆分后的小批量尾数订单")   
            column(name: "MIN_ORDER_QTY", type: "decimal(36,6)",  remarks: "最小订单数量")   
            column(name: "FIXED_LOT_MULTIPLE", type: "decimal(36,6)",  remarks: "圆整批量，考虑产品包装或生产过程中的物流器具装载而设置的生产订单数量最小倍数")   
            column(name: "RATE_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "速率类型，评估物料组织产能的具体单位，包括小时产量、秒两种")   
            column(name: "RATE", type: "decimal(36,6)",  remarks: "默认速率")   
            column(name: "ACTIVITY", type: "decimal(36,6)",  remarks: "开动率%，与默认速率配合使用，物料在组织上生产时的开动率")   
            column(name: "PRIORITY", type: "bigint(100)",  remarks: "优先级")   
            column(name: "PROCESS_BATCH", type: "decimal(36,6)",  remarks: "处理批量")   
            column(name: "STANDARD_RATE_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "标准速率类型，小时产量和生产节拍两种")   
            column(name: "STANDARD_RATE", type: "decimal(36,6)",  remarks: "标准速率，定义用于产能评估时考虑的速率")   
            column(name: "AUTO_ASSIGN_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "计划自动分配，维护组织时，用于标识自动计划排程时是否考虑该组织")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_pfep_schedule_category", indexName: "MT_PFEP_SCHEDULE_CATEGORY_N1") {
            column(name: "ENABLE_FLAG")
        }

        addUniqueConstraint(columnNames:"MATERIAL_CATEGORY_SITE_ID,ORGANIZATION_TYPE,ORGANIZATION_ID,TENANT_ID",tableName:"mt_pfep_schedule_category",constraintName: "MT_PFEP_SCHEDULE_CATEGORY_U1")
    }
}