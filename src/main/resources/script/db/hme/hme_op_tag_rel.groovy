package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_op_tag_rel.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2020-12-25-hme_op_tag_rel") {
        def weight = 1
        if (helper.isSqlServer()) {
            weight = 2
        } else if (helper.isOracle()) {
            weight = 3
        }
        if (helper.dbType().isSupportSequence()) {
            createSequence(sequenceName: 'hme_op_tag_rel_s', startValue: "1")
        }
        createTable(tableName: "hme_op_tag_rel", remarks: "工艺数据项关系表") {
            column(name: "op_tag_rel_id", type: "varchar(" + 100 * weight + ")", remarks: "主键") { constraints(primaryKey: true) }
            column(name: "tenant_id", type: "bigint(20)", defaultValue: "0", remarks: "租户ID") { constraints(nullable: "false") }
            column(name: "operation_id", type: "varchar(" + 100 * weight + ")", remarks: "工艺ID") { constraints(nullable: "false") }
            column(name: "tag_id", type: "varchar(" + 100 * weight + ")", remarks: "数据项ID") { constraints(nullable: "false") }
            column(name: "CID", type: "bigint(100)", remarks: "") { constraints(nullable: "false") }
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)", defaultValue: "1", remarks: "") { constraints(nullable: "false") }
            column(name: "CREATED_BY", type: "bigint(20)", defaultValue: "-1", remarks: "") { constraints(nullable: "false") }
            column(name: "CREATION_DATE", type: "datetime", defaultValueComputed: "CURRENT_TIMESTAMP", remarks: "") { constraints(nullable: "false") }
            column(name: "LAST_UPDATED_BY", type: "bigint(20)", defaultValue: "-1", remarks: "") { constraints(nullable: "false") }
            column(name: "LAST_UPDATE_DATE", type: "datetime", defaultValueComputed: "CURRENT_TIMESTAMP", remarks: "") { constraints(nullable: "false") }
            column(name: "ATTRIBUTE_CATEGORY", type: "varchar(" + 30 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE1", type: "varchar(" + 150 * weight + ")", remarks: "是否强校验")
            column(name: "ATTRIBUTE2", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE3", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE4", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "ATTRIBUTE5", type: "varchar(" + 150 * weight + ")", remarks: "")

        }
        createIndex(tableName: "hme_op_tag_rel", indexName: "HME_OP_TAG_REL_N1") {
            column(name: "tenant_id")
            column(name: "operation_id")
        }
        createIndex(tableName: "hme_op_tag_rel", indexName: "HME_OP_TAG_REL_N2") {
            column(name: "tenant_id")
            column(name: "tag_id")
        }

        addUniqueConstraint(columnNames: "tenant_id,operation_id,tag_id", tableName: "hme_op_tag_rel", constraintName: "HME_OP_TAG_REL_U1")
    }
}