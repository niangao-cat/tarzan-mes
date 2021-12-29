package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_eo_rel.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2021-03-22-hme_eo_rel") {
        def weight = 1
        if (helper.isSqlServer()) {
            weight = 2
        } else if (helper.isOracle()) {
            weight = 3
        }
        if (helper.dbType().isSupportSequence()) {
            createSequence(sequenceName: 'hme_eo_rel_s', startValue: "1")
        }
        createTable(tableName: "hme_eo_rel", remarks: "EO返修记录关系表") {
            column(name: "tenant_id", type: "bigint(20)", defaultValue: "0", remarks: "租户ID") { constraints(nullable: "false") }
            column(name: "eo_rel_id", type: "varchar(" + 100 * weight + ")", remarks: "主键") { constraints(primaryKey: true) }
            column(name: "eo_id", type: "varchar(" + 100 * weight + ")", remarks: "当前eoId") { constraints(nullable: "false") }
            column(name: "top_eo_id", type: "varchar(" + 100 * weight + ")", remarks: "顶层eoId") { constraints(nullable: "false") }
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

        addUniqueConstraint(columnNames: "tenant_id,eo_id,top_eo_id", tableName: "hme_eo_rel", constraintName: "hme_eo_rel_u1")
    }
}