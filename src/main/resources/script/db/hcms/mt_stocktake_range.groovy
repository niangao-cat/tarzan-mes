package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_stocktake_range.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_stocktake_range") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_stocktake_range_s', startValue:"1")
        }
        createTable(tableName: "mt_stocktake_range", remarks: "盘点范围表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "STOCKTAKE_RANGE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "单据范围ID")  {constraints(primaryKey: true)} 
            column(name: "STOCKTAKE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "单据ID")  {constraints(nullable:"false")}  
            column(name: "RANGE_OBJECT_TYPE", type: "varchar(" + 50 * weight + ")",   defaultValue:"",   remarks: "范围类型")  {constraints(nullable:"false")}  
            column(name: "RANGE_OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "范围ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}