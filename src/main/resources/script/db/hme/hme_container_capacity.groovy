package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_container_capacity.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2020-09-20-hme_container_capacity") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_container_capacity_s', startValue:"1")
        }
        createTable(tableName: "hme_container_capacity", remarks: "容器容量表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "CONTAINER_CAPACITY_ID", type: "varchar(" + 100 * weight + ")",  remarks: "表ID，主键")  {constraints(primaryKey: true)} 
            column(name: "CONTAINER_TYPE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "容器类型id")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "站点id")   
            column(name: "OPERATION_ID", type: "varchar(" + 100 * weight + ")",  remarks: "工艺id")   
            column(name: "COS_TYPE", type: "varchar(" + 100 * weight + ")",  remarks: "COS类型")   
            column(name: "CAPACITY", type: "bigint(20)",  remarks: "芯片数")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",  remarks: "有效性")  {constraints(nullable:"false")}  
            column(name: "LINE_NUM", type: "bigint(20)",  remarks: "行数")   
            column(name: "COLUMN_NUM", type: "bigint(20)",  remarks: "列数")   
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute5", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "cid", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,OPERATION_ID,COS_TYPE,CONTAINER_TYPE_ID",tableName:"hme_container_capacity",constraintName: "hme_container_capacity_u1")
    }
}