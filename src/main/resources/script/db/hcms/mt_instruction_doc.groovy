package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_instruction_doc.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_instruction_doc") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_instruction_doc_s', startValue:"1")
        }
        createTable(tableName: "mt_instruction_doc", remarks: "指令单据头表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "INSTRUCTION_DOC_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID，表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "INSTRUCTION_DOC_NUM", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "单据编号")  {constraints(nullable:"false")}  
            column(name: "INSTRUCTION_DOC_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "单据类型（业务类型，由项目定义）")  {constraints(nullable:"false")}  
            column(name: "INSTRUCTION_DOC_STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "状态（NEW，CANCEL，PRINTERD，RELEASED，COMPLETE，COMPLETE_CANCEL）")   
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商ID")   
            column(name: "SUPPLIER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "供应商地点ID")   
            column(name: "CUSTOMER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户ID")   
            column(name: "CUSTOMER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "客户地点ID")   
            column(name: "SOURCE_ORDER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "来源ERP订单类型（PO，SO）")   
            column(name: "SOURCE_ORDER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "订单ID（采购订单/销售订单")   
            column(name: "DEMAND_TIME", type: "datetime",  remarks: "需求时间")   
            column(name: "EXPECTED_ARRIVAL_TIME", type: "datetime",  remarks: "预计送达时间")   
            column(name: "COST_CENTER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "成本中心或账户别名")   
            column(name: "PERSON_ID", type: "bigint(20)",  remarks: "申请人/领料人")   
            column(name: "IDENTIFICATION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "实际业务需要的单据编号")   
            column(name: "REMARK", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "备注")   
            column(name: "REASON", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "原因")   
            column(name: "LATEST_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "最新一次新增历史表的主键")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "MARK", type: "varchar(" + 255 * weight + ")",  remarks: "标记")   

        }

        addUniqueConstraint(columnNames:"INSTRUCTION_DOC_NUM,TENANT_ID",tableName:"mt_instruction_doc",constraintName: "MT_INSTRUCTION_DOC_U1")
        addUniqueConstraint(columnNames:"IDENTIFICATION,TENANT_ID",tableName:"mt_instruction_doc",constraintName: "MT_INSTRUCTION_DOC_U2")
    }
}