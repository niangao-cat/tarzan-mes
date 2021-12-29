package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_bom.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_bom") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_bom_s', startValue:"1")
        }
        createTable(tableName: "mt_bom", remarks: "装配清单头") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "BOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装配清单ID")  {constraints(primaryKey: true)} 
            column(name: "BOM_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "装配清单名称")  {constraints(nullable:"false")}  
            column(name: "REVISION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "版本")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "物料清单描述")   
            column(name: "BOM_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "装配清单类型，包括物料、WO、EO的BOM类型")  {constraints(nullable:"false")}  
            column(name: "BOM_STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "物料清单状态")   
            column(name: "PRIMARY_QTY", type: "decimal(36,6)",  remarks: "基本数量")   
            column(name: "COPIED_FROM_BOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "引用的源物料清单ID")   
            column(name: "RELEASED_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "已经下达EO标识")   
            column(name: "CURRENT_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "当前版本")   
            column(name: "DATE_FROM", type: "datetime",  remarks: "生效时间")  {constraints(nullable:"false")}  
            column(name: "DATE_TO", type: "datetime",  remarks: "失效生产")   
            column(name: "AUTO_REVISION_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "自动升版本标识")   
            column(name: "ASSEMBLE_AS_MATERIAL_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否按物料装配")   
            column(name: "LATEST_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "最新一次新增历史表的主键")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }
   createIndex(tableName: "mt_bom", indexName: "MT_BOM_N1") {
            column(name: "BOM_STATUS")
        }
   createIndex(tableName: "mt_bom", indexName: "MT_BOM_N2") {
            column(name: "COPIED_FROM_BOM_ID")
        }
   createIndex(tableName: "mt_bom", indexName: "MT_BOM_N3") {
            column(name: "RELEASED_FLAG")
        }
   createIndex(tableName: "mt_bom", indexName: "MT_BOM_N4") {
            column(name: "CURRENT_FLAG")
        }

        addUniqueConstraint(columnNames:"BOM_NAME,REVISION,BOM_TYPE,TENANT_ID",tableName:"mt_bom",constraintName: "MT_BOM_U1")
    }
}