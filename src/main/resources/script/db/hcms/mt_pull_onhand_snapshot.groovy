package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_pull_onhand_snapshot.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_pull_onhand_snapshot") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_pull_onhand_snapshot_s', startValue:"1")
        }
        createTable(tableName: "mt_pull_onhand_snapshot", remarks: "拉动线边库存快照") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ONHAND_SNAPSHOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，唯一标识")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "站点ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料ID")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "库位ID")  {constraints(nullable:"false")}  
            column(name: "ONHAND_QTY", type: "decimal(36,6)",  remarks: "现有量")   
            column(name: "AREA_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "配送路线")  {constraints(nullable:"false")}  
            column(name: "SNAPSHOT_REVISION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "快照版本：配送路线+YYYYMMDDHHMMSS")  {constraints(nullable:"false")}  
            column(name: "LATEST_REVISION_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否最新版本，Y/N")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"SITE_ID,MATERIAL_ID,LOCATOR_ID,AREA_ID,SNAPSHOT_REVISION,TENANT_ID",tableName:"mt_pull_onhand_snapshot",constraintName: "MT_PULL_ONHAND_SNAPSHOT_U1")
    }
}