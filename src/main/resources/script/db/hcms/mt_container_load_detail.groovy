package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_container_load_detail.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_container_load_detail") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_container_load_detail_s', startValue:"1")
        }
        createTable(tableName: "mt_container_load_detail", remarks: "容器装载明细，记录具体容器装载的执行作业或物料批或其他容器的情况") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "CONTAINER_LOAD_DETAIL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "作为容器装载明细的唯一标识，用于其他数据结构引用此容器装载明细")  {constraints(primaryKey: true)} 
            column(name: "CONTAINER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "指示该容器装载明细记录所属容器标识ID")  {constraints(nullable:"false")}  
            column(name: "LOCATION_ROW", type: "bigint(20)",  remarks: "描述装载对象被装载到容器对象的行位置")   
            column(name: "LOCATION_COLUMN", type: "bigint(20)",  remarks: "描述装载对象被装载到容器对象的列位置")   
            column(name: "LOAD_OBJECT_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "描述容器装载的对象类型，包括：")  {constraints(nullable:"false")}  
            column(name: "LOAD_QTY", type: "decimal(36,6)",  remarks: "装载数量")   
            column(name: "LOAD_EO_STEP_ACTUAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "装载步骤")   
            column(name: "LOAD_OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "描述容器装入的具体对象，配合装载对象类型LOAD_OBJECT_TYPE一起使用，内容为EO、MATERIAL_LOT、CONTAINER的唯一标识ID")  {constraints(nullable:"false")}  
            column(name: "LOAD_SEQUENCE", type: "bigint(20)",  remarks: "装载次序")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"CONTAINER_ID,LOAD_SEQUENCE,TENANT_ID",tableName:"mt_container_load_detail",constraintName: "MT_CONTAINER_LOAD_DETAIL_U1")
        addUniqueConstraint(columnNames:"LOAD_OBJECT_TYPE,LOAD_OBJECT_ID,TENANT_ID,LOCATION_COLUMN,LOCATION_ROW,CONTAINER_ID",tableName:"mt_container_load_detail",constraintName: "MT_CONTAINER_LOAD_DETAIL_U2")
    }
}