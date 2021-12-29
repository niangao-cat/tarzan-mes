package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_equipment_his.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2021-03-23-hme_equipment_his") {
        def weight = 1
        if (helper.isSqlServer()) {
            weight = 2
        } else if (helper.isOracle()) {
            weight = 3
        }
        if (helper.dbType().isSupportSequence()) {
            createSequence(sequenceName: 'hme_equipment_his_s', startValue: "1")
        }
        createTable(tableName: "hme_equipment_his", remarks: "设备历史表") {
            column(name: "tenant_id", type: "bigint(20)", defaultValue: "0", remarks: "租户id") { constraints(nullable: "false") }
            column(name: "equipment_his_id", type: "varchar(" + 100 * weight + ")", remarks: "主键") { constraints(primaryKey: true) }
            column(name: "equipment_id", type: "varchar(" + 100 * weight + ")", remarks: "设备主键") { constraints(nullable: "false") }
            column(name: "asset_encoding", type: "varchar(" + 100 * weight + ")", remarks: "资产编码") { constraints(nullable: "false") }
            column(name: "asset_name", type: "varchar(" + 100 * weight + ")", remarks: "资产名称") { constraints(nullable: "false") }
            column(name: "asset_class", type: "varchar(" + 100 * weight + ")", remarks: "资产类型") { constraints(nullable: "false") }
            column(name: "descriptions", type: "varchar(" + 255 * weight + ")", remarks: "设备描述")
            column(name: "sap_num", type: "varchar(" + 100 * weight + ")", remarks: "SAP流水号")
            column(name: "equipment_body_num", type: "varchar(" + 100 * weight + ")", remarks: "机身序列号")
            column(name: "equipment_config", type: "varchar(" + 255 * weight + ")", remarks: "配置")
            column(name: "oa_check_num", type: "varchar(" + 100 * weight + ")", remarks: "OA验收单号")
            column(name: "equipment_type", type: "varchar(" + 255 * weight + ")", remarks: "设备类型")
            column(name: "equipment_category", type: "varchar(" + 255 * weight + ")", remarks: "设备类别")
            column(name: "apply_type", type: "varchar(" + 255 * weight + ")", remarks: "应用类型")
            column(name: "equipment_status", type: "varchar(" + 255 * weight + ")", remarks: "设备状态")
            column(name: "deal_num", type: "varchar(" + 100 * weight + ")", remarks: "处置单号")
            column(name: "deal_reason", type: "varchar(" + 255 * weight + ")", remarks: "处置原因")
            column(name: "business_id", type: "varchar(" + 100 * weight + ")", remarks: "保管部门ID")
            column(name: "user", type: "varchar(" + 50 * weight + ")", remarks: "使用人")
            column(name: "preserver", type: "varchar(" + 50 * weight + ")", remarks: "保管人")
            column(name: "location", type: "varchar(" + 255 * weight + ")", remarks: "存放地点")
            column(name: "measure_flag", type: "varchar(" + 1 * weight + ")", remarks: "是否计量")
            column(name: "frequency", type: "varchar(" + 255 * weight + ")", remarks: "使用频次")
            column(name: "belong_to", type: "varchar(" + 255 * weight + ")", remarks: "归属权")
            column(name: "posting_date", type: "datetime", remarks: "入账日期")
            column(name: "supplier", type: "varchar(" + 255 * weight + ")", remarks: "销售商")
            column(name: "brand", type: "varchar(" + 255 * weight + ")", remarks: "品牌")
            column(name: "model", type: "varchar(" + 255 * weight + ")", remarks: "型号")
            column(name: "unit", type: "varchar(" + 20 * weight + ")", remarks: "单位")
            column(name: "quantity", type: "bigint(20)", remarks: "数量")
            column(name: "amount", type: "decimal(36,6)", remarks: "金额")
            column(name: "currency", type: "varchar(" + 100 * weight + ")", remarks: "币种")
            column(name: "contract_num", type: "varchar(" + 100 * weight + ")", remarks: "合同编号")
            column(name: "recruitement", type: "varchar(" + 100 * weight + ")", remarks: "募投")
            column(name: "recruitement_num", type: "varchar(" + 100 * weight + ")", remarks: "募投编号")
            column(name: "warranty_date", type: "datetime", remarks: "质保期")
            column(name: "site_id", type: "varchar(" + 100 * weight + ")", remarks: "组织ID")
            column(name: "remark", type: "varchar(" + 255 * weight + ")", remarks: "备注")
            column(name: "event_id", type: "varchar(" + 100 * weight + ")", remarks: "事件id") { constraints(nullable: "false") }
            column(name: "CID", type: "bigint(100)", remarks: "CID") { constraints(nullable: "false") }
            column(name: "object_version_number", type: "bigint(20)", defaultValue: "1", remarks: "行版本号，用来处理锁") { constraints(nullable: "false") }
            column(name: "creation_date", type: "datetime", defaultValueComputed: "CURRENT_TIMESTAMP", remarks: "") { constraints(nullable: "false") }
            column(name: "created_by", type: "bigint(20)", defaultValue: "-1", remarks: "") { constraints(nullable: "false") }
            column(name: "last_updated_by", type: "bigint(20)", defaultValue: "-1", remarks: "") { constraints(nullable: "false") }
            column(name: "last_update_date", type: "datetime", defaultValueComputed: "CURRENT_TIMESTAMP", remarks: "") { constraints(nullable: "false") }
            column(name: "ATTRIBUTE_CATEGORY", type: "varchar(" + 30 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE1", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE2", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE3", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE4", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE5", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE6", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE7", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE8", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE9", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE10", type: "varchar(" + 150 * weight + ")", remarks: "")

        }
        createIndex(tableName: "hme_equipment_his", indexName: "hme_equipment_his_n1") {
            column(name: "tenant_id")
            column(name: "equipment_id")
        }

    }
}