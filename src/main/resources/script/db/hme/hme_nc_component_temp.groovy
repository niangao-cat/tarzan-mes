package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_nc_component_temp.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2020-07-01-hme_nc_component_temp") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_nc_component_temp_s', startValue:"1")
        }
        createTable(tableName: "hme_nc_component_temp", remarks: "不良材料清单临时表") {
            column(name: "TENANT_ID", type: "bigint(20)",  remarks: "租户Id")  {constraints(nullable:"false")}  
            column(name: "nc_component_temp_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "组织Id")  {constraints(nullable:"false")}  
            column(name: "sn", type: "varchar(" + 50 * weight + ")",  remarks: "序列号")  {constraints(nullable:"false")}  
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位")  {constraints(nullable:"false")}  
            column(name: "user_id", type: "varchar(" + 100 * weight + ")",  remarks: "用户id")  {constraints(nullable:"false")}  
            column(name: "material_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料ID")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE1", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   

        }

    }
}