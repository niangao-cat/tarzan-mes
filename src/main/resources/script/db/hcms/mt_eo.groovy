package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_eo.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_eo") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_eo_s', startValue:"1")
        }
        createTable(tableName: "mt_eo", remarks: "执行作业【执行作业需求和实绩拆分开】") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "EO_NUM", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "EO编号")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产站点")  {constraints(nullable:"false")}  
            column(name: "WORK_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "工单，EO对应生产指令")  {constraints(nullable:"false")}  
            column(name: "STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "EO状态（运行、完成、作废、序列化等）")  {constraints(nullable:"false")}  
            column(name: "LAST_EO_STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "前次EO状态")   
            column(name: "PRODUCTION_LINE_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "生产线ID")  {constraints(nullable:"false")}  
            column(name: "WORKCELL_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "当前工作单元（是否不需要，然后通过工艺路线步骤反映即可）")   
            column(name: "PLAN_START_TIME", type: "datetime",  remarks: "开始时间")  {constraints(nullable:"false")}  
            column(name: "PLAN_END_TIME", type: "datetime",  remarks: "结束时间")  {constraints(nullable:"false")}  
            column(name: "QTY", type: "decimal(36,6)",  remarks: "数量")  {constraints(nullable:"false")}  
            column(name: "UOM_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "单位")  {constraints(nullable:"false")}  
            column(name: "EO_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "EO类型")  {constraints(nullable:"false")}  
            column(name: "VALIDATE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否验证通过")  {constraints(nullable:"false")}  
            column(name: "IDENTIFICATION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "标识说明")   
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料ID")   
            column(name: "LATEST_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "最新一次新增历史表的主键")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_eo", indexName: "MT_EO_N1") {
            column(name: "LAST_UPDATE_DATE")
        }

        addUniqueConstraint(columnNames:"TENANT_ID,EO_NUM",tableName:"mt_eo",constraintName: "MT_EO_U1")
    }
}