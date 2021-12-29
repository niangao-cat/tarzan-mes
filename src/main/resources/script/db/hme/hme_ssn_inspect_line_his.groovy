package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_ssn_inspect_line_his.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2021-04-08-hme_ssn_inspect_line_his") {
        def weight = 1
        if (helper.isSqlServer()) {
            weight = 2
        } else if (helper.isOracle()) {
            weight = 3
        }
        if (helper.dbType().isSupportSequence()) {
            createSequence(sequenceName: 'hme_ssn_inspect_line_his_s', startValue: "1")
        }
        createTable(tableName: "hme_ssn_inspect_line_his", remarks: "标准件检验标准行历史表") {
            column(name: "ssn_inspect_line_his_id", type: "varchar(" + 100 * weight + ")", remarks: "主键") { constraints(primaryKey: true) }
            column(name: "ssn_inspect_line_id", type: "varchar(" + 100 * weight + ")", remarks: "行ID") { constraints(nullable: "false") }
            column(name: "ssn_inspect_header_id", type: "varchar(" + 100 * weight + ")", remarks: "头ID") { constraints(nullable: "false") }
            column(name: "sequence", type: "bigint(20)", remarks: "序号") { constraints(nullable: "false") }
            column(name: "tag_id", type: "varchar(" + 100 * weight + ")", remarks: "检验项目id") { constraints(nullable: "false") }
            column(name: "minimum_value", type: "decimal(36,6)", remarks: "最小值")
            column(name: "maximal_value", type: "decimal(36,6)", remarks: "最大值")
            column(name: "allow_differ", type: "decimal(36,6)", remarks: "允差")
            column(name: "couple_flag", type: "varchar(" + 1 * weight + ")", remarks: "影响耦合标识")
            column(name: "judge_flag", type: "varchar(" + 1 * weight + ")", remarks: "影响判定标识")
            column(name: "cos_couple_flag", type: "varchar(" + 1 * weight + ")", remarks: "COS耦合标志")
            column(name: "cos_pos", type: "varchar(" + 60 * weight + ")", remarks: "COS位置")
            column(name: "tenant_id", type: "bigint(20)", defaultValue: "0", remarks: "租户ID") { constraints(nullable: "false") }
            column(name: "check_allow_differ", type: "decimal(36,6)", remarks: "校验差值")
            column(name: "event_id", type: "varchar(" + 100 * weight + ")", remarks: "事件ID") { constraints(nullable: "false") }
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
        createIndex(tableName: "hme_ssn_inspect_line_his", indexName: "hme_ssn_inspect_line_his_n1") {
            column(name: "ssn_inspect_line_id")
        }

    }
}