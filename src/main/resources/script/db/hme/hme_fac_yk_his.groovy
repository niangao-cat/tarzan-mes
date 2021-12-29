package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_fac_yk_his.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2021-04-07-hme_fac_yk_his") {
        def weight = 1
        if (helper.isSqlServer()) {
            weight = 2
        } else if (helper.isOracle()) {
            weight = 3
        }
        if (helper.dbType().isSupportSequence()) {
            createSequence(sequenceName: 'hme_fac_yk_his_s', startValue: "1")
        }
        createTable(tableName: "hme_fac_yk_his", remarks: "FAC-Y宽判定标准历史") {
            column(name: "fac_yk_his_id", type: "varchar(" + 100 * weight + ")", remarks: "主键") { constraints(primaryKey: true) }
            column(name: "fac_yk_id", type: "varchar(" + 100 * weight + ")", remarks: "业务表主键") { constraints(nullable: "false") }
            column(name: "material_id", type: "varchar(" + 100 * weight + ")", remarks: "物料id") { constraints(nullable: "false") }
            column(name: "cos_type", type: "varchar(" + 100 * weight + ")", remarks: "芯片类型") { constraints(nullable: "false") }
            column(name: "fac_material_id", type: "varchar(" + 100 * weight + ")", remarks: "FAC物料id") { constraints(nullable: "false") }
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")", remarks: "工位id") { constraints(nullable: "false") }
            column(name: "standard_value", type: "decimal(36,6)", remarks: "标准值") { constraints(nullable: "false") }
            column(name: "allow_differ", type: "decimal(36,6)", remarks: "允差") { constraints(nullable: "false") }
            column(name: "event_id", type: "varchar(" + 100 * weight + ")", remarks: "事件ID") { constraints(nullable: "false") }
            column(name: "tenant_id", type: "bigint(20)", defaultValue: "0", remarks: "租户ID") { constraints(nullable: "false") }
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
            column(name: "ATTRIBUTE11", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE12", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE13", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE14", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE15", type: "varchar(" + 150 * weight + ")", remarks: "")

        }
        createIndex(tableName: "hme_fac_yk_his", indexName: "hme_fac_yk_his_n1") {
            column(name: "fac_yk_id")
        }

    }
}