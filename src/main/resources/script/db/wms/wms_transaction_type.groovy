package script.db

databaseChangeLog(logicalFilePath: 'script/db/wms_transaction_type.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-wms_transaction_type") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'wms_transaction_type_s', startValue:"1")
        }
        createTable(tableName: "wms_transaction_type", remarks: "事务类型表") {
            column(name: "TENANT_ID", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "TRANSACTION_TYPE_ID", type: "varchar(" + 100 * weight + ")",  remarks: "事务类型ID")  {constraints(primaryKey: true)} 
            column(name: "TRANSACTION_TYPE_CODE", type: "varchar(" + 255 * weight + ")",  remarks: "事务类型编码")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",  remarks: "描述")   
            column(name: "MOVE_TYPE", type: "varchar(" + 100 * weight + ")",  remarks: "移动类型")   
            column(name: "PROCESS_ERP_FLAG", type: "varchar(" + 1 * weight + ")",  remarks: "是否回传ERP")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",  remarks: "是否有效")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")   
            column(name: "CREATED_BY", type: "bigint(20)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",  remarks: "")   
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",  remarks: "")   
            column(name: "ATTRIBUTE1", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE6", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE11", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE12", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE13", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE14", type: "varchar(" + 255 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE15", type: "varchar(" + 255 * weight + ")",  remarks: "")   

        }

        addUniqueConstraint(columnNames:"TENANT_ID,TRANSACTION_TYPE_CODE",tableName:"wms_transaction_type",constraintName: "Z_TRANSACTION_TYPE_U1")
    }
}