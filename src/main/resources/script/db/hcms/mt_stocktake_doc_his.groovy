package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_stocktake_doc_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_stocktake_doc_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_stocktake_doc_his_s', startValue:"1")
        }
        createTable(tableName: "mt_stocktake_doc_his", remarks: "盘点单据历史表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "STOCKTAKE_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "盘点单据历史ID")  {constraints(primaryKey: true)} 
            column(name: "STOCKTAKE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "盘点单据ID")  {constraints(nullable:"false")}  
            column(name: "STOCKTAKE_NUM", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "盘点单据编号")  {constraints(nullable:"false")}  
            column(name: "STOCKTAKE_STATUS", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "状态")  {constraints(nullable:"false")}  
            column(name: "STOCKTAKE_LAST_STATUS", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "上一状态，用于状态发生变更时找到上一状态的结果")   
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "AREA_LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "区域类型库位ID，用于用户查看盘点当前区域")  {constraints(nullable:"false")}  
            column(name: "OPEN_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否明盘，Y为明盘，N为盲盘")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_RANGE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否按物料盘点，Y/N，不能为空")  {constraints(nullable:"false")}  
            column(name: "ADJUST_TIMELY_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否允许实时调整，Y/N，不能为空")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_LOT_LOCK_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "物料批停用标识")  {constraints(nullable:"false")}  
            column(name: "IDENTIFICATION", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "单据条码")  {constraints(nullable:"false")}  
            column(name: "REMARK", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "备注")   
            column(name: "EVENT_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"STOCKTAKE_ID,EVENT_ID,TENANT_ID",tableName:"mt_stocktake_doc_his",constraintName: "MT_STOCKTAKE_DOC_HIS_U1")
    }
}