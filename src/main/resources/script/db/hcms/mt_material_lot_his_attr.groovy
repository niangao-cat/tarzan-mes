package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_material_lot_his_attr.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_material_lot_his_attr") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_material_lot_his_attr_s', startValue:"1")
        }
        createTable(tableName: "mt_material_lot_his_attr", remarks: "物料批扩展历史表的扩展表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ATTR_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "扩展属性历史表主键ID")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_LOT_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主表历史表ID")  {constraints(nullable:"false")}  
            column(name: "ATTR_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "扩展属性名")  {constraints(nullable:"false")}  
            column(name: "ATTR_VALUE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "扩展属性值")   
            column(name: "LANG", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "语言类型")   
            column(name: "EVENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(20)",  remarks: "CID")   
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")   
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")   
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")   
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")   

        }

        addUniqueConstraint(columnNames:"TENANT_ID,MATERIAL_LOT_HIS_ID,ATTR_NAME,EVENT_ID,LANG",tableName:"mt_material_lot_his_attr",constraintName: "MT_MATERIAL_LOT_HIS_ATTR_U1")
    }
}