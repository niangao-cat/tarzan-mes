package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_subinventory.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_subinventory") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_subinventory_s', startValue:"1")
        }
        createTable(tableName: "mt_subinventory", remarks: "ERP库存表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "SUBINVENTORY_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID ,表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "SUBINVENTORY_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "库存地点代码（子库存）")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "库存地点描述（子库存描述)")   
            column(name: "PLANT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工厂CODE")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "有效标识")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"SUBINVENTORY_CODE,PLANT_CODE,TENANT_ID",tableName:"mt_subinventory",constraintName: "MT_SUBINVENTORY_B_U1")
    }
}