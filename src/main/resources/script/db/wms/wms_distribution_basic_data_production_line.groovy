package script.db

databaseChangeLog(logicalFilePath: 'script/db/wms_distribution_basic_line.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2020-07-21-wms_distribution_basic_line") {
        def weight = 1
        if (helper.isSqlServer()) {
            weight = 2
        } else if (helper.isOracle()) {
            weight = 3
        }
        if (helper.dbType().isSupportSequence()) {
            createSequence(sequenceName: 'wms_distribution_basic_line_s', startValue: "1")
        }
        createTable(tableName: "wms_distribution_basic_line", remarks: "配送物料产线表") {
            column(name: "tenant_id", type: "bigint(20)", remarks: "租户ID") { constraints(nullable: "false") }
            column(name: "line_id", type: "varchar(" + 100 * weight + ")", remarks: "主键ID") { constraints(primaryKey: true) }
            column(name: "header_id", type: "varchar(" + 100 * weight + ")", remarks: "配送基础数据ID") { constraints(nullable: "false") }
            column(name: "production_line_id", type: "varchar(" + 100 * weight + ")", remarks: "产线ID") { constraints(nullable: "false") }
            column(name: "enabled_flag", type: "varchar(" + 1 * weight + ")", remarks: "是否有效")
            column(name: "CID", type: "bigint(100)", remarks: "CID") { constraints(nullable: "false") }
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)", defaultValue: "1", remarks: "行版本号，用来处理锁") { constraints(nullable: "false") }
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "attribute4", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "attribute5", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "attribute6", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "attribute7", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "attribute8", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "attribute9", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "attribute10", type: "varchar(" + 150 * weight + ")", remarks: "")
            column(name: "creation_date", type: "datetime", defaultValueComputed: "CURRENT_TIMESTAMP", remarks: "") { constraints(nullable: "false") }
            column(name: "created_by", type: "bigint(20)", defaultValue: "-1", remarks: "") { constraints(nullable: "false") }
            column(name: "last_updated_by", type: "bigint(20)", defaultValue: "-1", remarks: "") { constraints(nullable: "false") }
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}

        }

    }
}