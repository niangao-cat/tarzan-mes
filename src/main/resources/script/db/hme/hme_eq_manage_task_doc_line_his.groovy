package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_eq_manage_task_doc_line_his.groovy') {
    changeSet(author: "sanfeng.zhang@china-hand.com", id: "2021-03-04-hme_eq_manage_task_doc_line_his") {
        def weight = 1
        if (helper.isSqlServer()) {
            weight = 2
        } else if (helper.isOracle()) {
            weight = 3
        }
        if (helper.dbType().isSupportSequence()) {
            createSequence(sequenceName: 'hme_eq_manage_task_doc_line_his_s', startValue: "1")
        }
        createTable(tableName: "hme_eq_manage_task_doc_line_his", remarks: "设备管理任务单行历史表") {
            column(name: "tenant_id", type: "bigint(20)", defaultValue: "0", remarks: "租户id")
            column(name: "task_doc_his_id", type: "varchar(" + 100 * weight + ")", remarks: "主键") { constraints(primaryKey: true) }
            column(name: "task_doc_id", type: "varchar(" + 100 * weight + ")", remarks: "单据头id")
            column(name: "task_doc_line_id", type: "varchar(" + 100 * weight + ")", remarks: "单据行id")
            column(name: "manage_tag_id", type: "varchar(" + 100 * weight + ")", remarks: "项目id")
            column(name: "check_value", type: "varchar(" + 100 * weight + ")", remarks: "检验值")
            column(name: "result", type: "varchar(" + 255 * weight + ")", remarks: "结果")
            column(name: "check_date", type: "datetime", remarks: "检验日期") { constraints(nullable: "false") }
            column(name: "wkc_id", type: "varchar(" + 100 * weight + ")", remarks: "点检工位") { constraints(nullable: "false") }
            column(name: "check_by", type: "bigint(20)", remarks: "检验人") { constraints(nullable: "false") }
            column(name: "event_id", type: "varchar(" + 100 * weight + ")", remarks: "事件id")
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
        createIndex(tableName: "hme_eq_manage_task_doc_line_his", indexName: "hme_eq_manage_task_doc_line_his_n1") {
            column(name: "tenant_id")
            column(name: "task_doc_id")
            column(name: "task_doc_line_id")
        }

    }
}