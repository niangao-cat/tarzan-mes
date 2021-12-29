package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_retest_import_data.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2021-01-22-hme_retest_import_data") {
        def weight = 1
        if (helper.isSqlServer()) {
            weight = 2
        } else if (helper.isOracle()) {
            weight = 3
        }
        if (helper.dbType().isSupportSequence()) {
            createSequence(sequenceName: 'hme_retest_import_data_s', startValue: "1")
        }
        createTable(tableName: "hme_retest_import_data", remarks: "COS复测导入数据临时表") {
            column(name: "tenant_id", type: "bigint(20)", defaultValue: "0", remarks: "租户id") { constraints(nullable: "false") }
            column(name: "retest_import_data_id", type: "varchar(" + 100 * weight + ")", remarks: "表ID，主键") { constraints(primaryKey: true) }
            column(name: "work_num", type: "varchar(" + 100 * weight + ")", remarks: "工单号")
            column(name: "cos_type", type: "varchar(" + 100 * weight + ")", remarks: "COS类型")
            column(name: "workcell", type: "varchar(" + 100 * weight + ")", remarks: "工位")
            column(name: "import_lot", type: "varchar(" + 30 * weight + ")", remarks: "导入批次")
            column(name: "import_type", type: "varchar(" + 30 * weight + ")", remarks: "导入类型")
            column(name: "target_barcode", type: "varchar(" + 100 * weight + ")", remarks: "目标条码")
            column(name: "source_barcode", type: "varchar(" + 100 * weight + ")", remarks: "来料条码")
            column(name: "fox_num", type: "varchar(" + 100 * weight + ")", remarks: "盒号")
            column(name: "supplier_code", type: "varchar(" + 255 * weight + ")", remarks: "供应商")
            column(name: "rc_num", type: "varchar(" + 100 * weight + ")", remarks: "热沉号")
            column(name: "wafer", type: "varchar(" + 255 * weight + ")", remarks: "WAFER")
            column(name: "container_type", type: "varchar(" + 100 * weight + ")", remarks: "容器类型")
            column(name: "lotno", type: "varchar(" + 255 * weight + ")", remarks: "LOTNO")
            column(name: "avg_wavelenght", type: "decimal(36,6)", remarks: "Avg (nm)")
            column(name: "type", type: "varchar(" + 100 * weight + ")", remarks: "TYPE")
            column(name: "remark", type: "varchar(" + 255 * weight + ")", remarks: "备注")
            column(name: "position", type: "varchar(" + 10 * weight + ")", remarks: "位置")
            column(name: "qty", type: "decimal(36,6)", remarks: "合格芯片数")
            column(name: "current", type: "varchar(" + 100 * weight + ")", remarks: "电流")
            column(name: "print_flag", type: "varchar(" + 1 * weight + ")", defaultValue: "N", remarks: "是否打印 Y-是 N-否") { constraints(nullable: "false") }
            column(name: "a01", type: "varchar(" + 20 * weight + ")", remarks: "功率等级")
            column(name: "a02", type: "decimal(20,3)", remarks: "功率")
            column(name: "a03", type: "varchar(" + 20 * weight + ")", remarks: "波长等级")
            column(name: "a04", type: "decimal(20,3)", remarks: "波长")
            column(name: "a05", type: "decimal(20,3)", remarks: "波长差")
            column(name: "a06", type: "decimal(20,3)", remarks: "电压")
            column(name: "a07", type: "decimal(20,3)", remarks: "光谱宽度（单点）")
            column(name: "a08", type: "varchar(" + 100 * weight + ")", remarks: "设备")
            column(name: "a09", type: "varchar(" + 100 * weight + ")", remarks: "测试模式")
            column(name: "a010", type: "decimal(20,3)", remarks: "阈值电流")
            column(name: "a011", type: "decimal(20,3)", remarks: "阈值电压")
            column(name: "a012", type: "decimal(20,3)", remarks: "SE")
            column(name: "a013", type: "decimal(20,3)", remarks: "线宽")
            column(name: "a014", type: "decimal(20,3)", remarks: "光电能转换效率")
            column(name: "a15", type: "decimal(20,3)", remarks: "偏振度")
            column(name: "a16", type: "decimal(20,3)", remarks: "X半宽高")
            column(name: "a17", type: "decimal(20,3)", remarks: "Y半宽高")
            column(name: "a18", type: "decimal(20,3)", remarks: "X86能量宽")
            column(name: "a19", type: "decimal(20,3)", remarks: "Y86能量宽")
            column(name: "a20", type: "decimal(20,3)", remarks: "X95能量宽")
            column(name: "a21", type: "decimal(20,3)", remarks: "Y95能量宽")
            column(name: "a22", type: "decimal(20,3)", remarks: "透镜功率")
            column(name: "a23", type: "decimal(20,3)", remarks: "PBS功率")
            column(name: "a24", type: "varchar(" + 100 * weight + ")", remarks: "不良代码")
            column(name: "a25", type: "varchar(" + 100 * weight + ")", remarks: "操作者")
            column(name: "a26", type: "varchar(" + 100 * weight + ")", remarks: "备注")
            column(name: "cid", type: "bigint(100)", remarks: "CID") { constraints(nullable: "false") }
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
        createIndex(tableName: "hme_retest_import_data", indexName: "hme_retest_import_data_n1") {
            column(name: "tenant_id")
            column(name: "work_num")
            column(name: "cos_type")
        }

    }
}