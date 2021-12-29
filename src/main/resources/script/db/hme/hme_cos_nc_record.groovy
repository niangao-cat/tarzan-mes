package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_cos_nc_record.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2021-01-07-hme_cos_nc_record") {
        def weight = 1
        if (helper.isSqlServer()) {
            weight = 2
        } else if (helper.isOracle()) {
            weight = 3
        }
        if (helper.dbType().isSupportSequence()) {
            createSequence(sequenceName: 'hme_cos_nc_record_s', startValue: "1")
        }
        createTable(tableName: "hme_cos_nc_record", remarks: "芯片不良记录表") {
            column(name: "tenant_id", type: "bigint(20)", defaultValue: "0", remarks: "租户id") { constraints(nullable: "false") }
            column(name: "cos_nc_record_id", type: "varchar(" + 100 * weight + ")", remarks: "表ID，主键") { constraints(primaryKey: true) }
            column(name: "site_id", type: "varchar(" + 100 * weight + ")", remarks: "站点id") { constraints(nullable: "false") }
            column(name: "user_id", type: "bigint(20)", remarks: "操作人")
            column(name: "job_id", type: "varchar(" + 100 * weight + ")", remarks: "EO_JOB_SN表主键")
            column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")", remarks: "物料批")
            column(name: "defect_count", type: "decimal(20,0)", remarks: "缺陷数量")
            column(name: "nc_code_id", type: "varchar(" + 100 * weight + ")", remarks: "不良代码ID")
            column(name: "nc_type", type: "varchar(" + 50 * weight + ")", remarks: "不良代码分类，缺陷/瑕疵/修复")
            column(name: "component_material_id", type: "varchar(" + 100 * weight + ")", remarks: "NC记录的组件")
            column(name: "operation_id", type: "varchar(" + 100 * weight + ")", remarks: "工艺")
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")", remarks: "工作单元")
            column(name: "load_sequence", type: "varchar(" + 100 * weight + ")", remarks: "芯片序列号")
            column(name: "hot_sink_code", type: "varchar(" + 100 * weight + ")", remarks: "热沉编号")
            column(name: "work_order_id", type: "varchar(" + 100 * weight + ")", remarks: "工单号")
            column(name: "wafer_num", type: "varchar(" + 100 * weight + ")", remarks: "wafer")
            column(name: "cos_type", type: "varchar(" + 100 * weight + ")", remarks: "芯片类型")
            column(name: "comments", type: "varchar(" + 1000 * weight + ")", remarks: "备注")
            column(name: "nc_load_row", type: "bigint(2)", remarks: "来源行")
            column(name: "nc_load_column", type: "bigint(2)", remarks: "来源列")
            column(name: "status", type: "varchar(" + 20 * weight + ")", remarks: "状态")
            column(name: "load_num", type: "varchar(" + 100 * weight + ")", remarks: "位置信息")
            column(name: "cid", type: "bigint(100)", remarks: "CID") { constraints(nullable: "false") }
            column(name: "object_version_number", type: "bigint(30)", defaultValue: "1", remarks: "行版本号，用来处理锁") { constraints(nullable: "false") }
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
            column(name: "ATTRIBUTE11", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE12", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE13", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE14", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE15", type: "varchar(" + 150 * weight + ")", remarks: "")

        }

    }
}