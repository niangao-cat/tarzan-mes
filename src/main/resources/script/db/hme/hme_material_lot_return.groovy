package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_material_lot_return.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2020-12-12-hme_material_lot_return") {
        def weight = 1
        if (helper.isSqlServer()) {
            weight = 2
        } else if (helper.isOracle()) {
            weight = 3
        }
        if (helper.dbType().isSupportSequence()) {
            createSequence(sequenceName: 'hme_material_lot_return_s', startValue: "1")
        }
        createTable(tableName: "hme_material_lot_return", remarks: "退料条码关系表") {
            column(name: "tenant_id", type: "bigint(20)", defaultValue: "0", remarks: "租户id") { constraints(nullable: "false") }
            column(name: "material_lot_return_id", type: "varchar(" + 100 * weight + ")", remarks: "主键") { constraints(primaryKey: true) }
            column(name: "work_order_id", type: "varchar(" + 100 * weight + ")", remarks: "工单id") { constraints(nullable: "false") }
            column(name: "return_material_lot_id", type: "varchar(" + 100 * weight + ")", remarks: "退料条码id") { constraints(nullable: "false") }
            column(name: "return_qty", type: "decimal(36,6)", remarks: "退料数量") { constraints(nullable: "false") }
            column(name: "target_material_lot_id", type: "varchar(" + 100 * weight + ")", remarks: "目标条码id") { constraints(nullable: "false") }
            column(name: "lot", type: "varchar(" + 255 * weight + ")", remarks: "批次") { constraints(nullable: "false") }
            column(name: "wafer_num", type: "varchar(" + 255 * weight + ")", defaultValue: "1", remarks: "wafer") { constraints(nullable: "false") }
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

        addUniqueConstraint(columnNames: "tenant_id,work_order_id,return_material_lot_id,target_material_lot_id", tableName: "hme_material_lot_return", constraintName: "hme_material_lot_return_u1")
    }
}