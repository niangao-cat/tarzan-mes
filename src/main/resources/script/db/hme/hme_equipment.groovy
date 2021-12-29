package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_equipment.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_equipment") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_equipment_s', startValue:"1")
        }
        createTable(tableName: "hme_equipment", remarks: "设备表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")   
            column(name: "EQUIPMENT_ID", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "ASSET_ENCODING", type: "varchar(" + 100 * weight + ")",  remarks: "资产编码")  {constraints(nullable:"false")}  
            column(name: "ASSET_NAME", type: "varchar(" + 255 * weight + ")",  remarks: "资产名称")   
            column(name: "ASSET_CLASS", type: "varchar(" + 255 * weight + ")",  remarks: "资产类别")   
            column(name: "DESCRIPTIONS", type: "varchar(" + 255 * weight + ")",  remarks: "设备描述")   
            column(name: "SAP_NUM", type: "varchar(" + 100 * weight + ")",  remarks: "SAP流水号")   
            column(name: "EQUIPMENT_BODY_NUM", type: "varchar(" + 100 * weight + ")",  remarks: "机身序列号")   
            column(name: "EQUIPMENT_CONFIG", type: "varchar(" + 255 * weight + ")",  remarks: "配置")   
            column(name: "OA_CHECK_NUM", type: "varchar(" + 100 * weight + ")",  remarks: "OA验收单号")   
            column(name: "EQUIPMENT_TYPE", type: "varchar(" + 255 * weight + ")",  remarks: "设备类型")   
            column(name: "EQUIPMENT_CATEGORY", type: "varchar(" + 255 * weight + ")",  remarks: "设备类别")   
            column(name: "APPLY_TYPE", type: "varchar(" + 255 * weight + ")",  remarks: "应用类型")   
            column(name: "EQUIPMENT_STATUS", type: "varchar(" + 255 * weight + ")",  remarks: "设备状态")   
            column(name: "DEAL_NUM", type: "varchar(" + 100 * weight + ")",  remarks: "处置单号")   
            column(name: "DEAL_REASON", type: "varchar(" + 255 * weight + ")",  remarks: "处置原因")   
            column(name: "BUSINESS_ID", type: "varchar(" + 100 * weight + ")",  remarks: "保管部门ID")   
            column(name: "USER", type: "varchar(" + 50 * weight + ")",  remarks: "使用人")   
            column(name: "PRESERVER", type: "varchar(" + 50 * weight + ")",  remarks: "保管人")   
            column(name: "LOCATION", type: "varchar(" + 255 * weight + ")",  remarks: "存放地点")   
            column(name: "MEASURE_FLAG", type: "varchar(" + 1 * weight + ")",  remarks: "是否计量")   
            column(name: "FREQUENCY", type: "varchar(" + 255 * weight + ")",  remarks: "使用频次")   
            column(name: "BELONG_TO", type: "varchar(" + 255 * weight + ")",  remarks: "归属权")   
            column(name: "POSTING_DATE", type: "datetime",  remarks: "入账日期")   
            column(name: "SUPPLIER", type: "varchar(" + 255 * weight + ")",  remarks: "销售商")   
            column(name: "BRAND", type: "varchar(" + 255 * weight + ")",  remarks: "品牌")   
            column(name: "MODEL", type: "varchar(" + 255 * weight + ")",  remarks: "型号")   
            column(name: "UNIT", type: "varchar(" + 20 * weight + ")",  remarks: "单位")   
            column(name: "QUANTITY", type: "bigint(10)",  remarks: "数量")   
            column(name: "AMOUNT", type: "decimal(20,2)",  remarks: "金额")   
            column(name: "CURRENCY", type: "varchar(" + 100 * weight + ")",  remarks: "币种")   
            column(name: "CONTRACT_NUM", type: "varchar(" + 100 * weight + ")",  remarks: "合同编号")   
            column(name: "RECRUITEMENT", type: "varchar(" + 100 * weight + ")",  remarks: "募投")   
            column(name: "RECRUITEMENT_NUM", type: "varchar(" + 100 * weight + ")",  remarks: "募投编号")   
            column(name: "WARRANTY_DATE", type: "datetime",  remarks: "质保期")   
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "组织ID")   
            column(name: "REMARK", type: "varchar(" + 255 * weight + ")",  remarks: "备注")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE_CATEGORY", type: "varchar(" + 30 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE6", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }

        addUniqueConstraint(columnNames:"TENANT_ID,ASSET_ENCODING",tableName:"hme_equipment",constraintName: "HME_EQUIPMENT_U1")
    }
}