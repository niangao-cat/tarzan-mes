package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_nc_record_attr.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2020-07-23-hme_nc_record_attr") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_nc_record_attr_s', startValue:"1")
        }
        createTable(tableName: "hme_nc_record_attr", remarks: "不良记录扩展属性表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "attr_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "parent_record_id", type: "varchar(" + 100 * weight + ")",  remarks: "父不良记录ID")   
            column(name: "record_id", type: "varchar(" + 100 * weight + ")",  remarks: "子不良记录ID")   
            column(name: "process_method", type: "varchar(" + 30 * weight + ")",  remarks: "处置方式")  {constraints(nullable:"false")}  
            column(name: "cid", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute5", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute6", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute7", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute8", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute9", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "attribute10", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"tenant_id,parent_record_id,record_id",tableName:"hme_nc_record_attr",constraintName: "hme_nc_record_attr_u1")
    }
}