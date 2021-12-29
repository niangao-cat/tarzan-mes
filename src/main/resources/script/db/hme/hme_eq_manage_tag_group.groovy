package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_eq_manage_tag_group.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-hme_eq_manage_tag_group") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_eq_manage_tag_group_s', startValue:"1")
        }
        createTable(tableName: "hme_eq_manage_tag_group", remarks: "设备管理项目组表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID（企业ID）")  {constraints(nullable:"false")}  
            column(name: "manage_tag_group_id", type: "varchar(" + 100 * weight + ")",  remarks: "")  {constraints(primaryKey: true)} 
            column(name: "site_id", type: "varchar(" + 100 * weight + ")",  remarks: "组织id")   
            column(name: "equipment_id", type: "varchar(" + 100 * weight + ")",  remarks: "设备ID")   
            column(name: "equipment_category", type: "varchar(" + 100 * weight + ")",  remarks: "设备类别")   
            column(name: "tag_group_id", type: "varchar(" + 100 * weight + ")",  remarks: "项目组ID")  {constraints(nullable:"false")}  
            column(name: "status", type: "varchar(" + 10 * weight + ")",  remarks: "状态")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 20 * weight + ")",  remarks: "等级编码有效性")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
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
            column(name: "ATTRIBUTE6", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 400 * weight + ")",  remarks: "预留字段")   

        }

    }
}