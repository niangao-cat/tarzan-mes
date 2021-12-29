package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_organization_unit_rel.groovy') {
    changeSet(author: "sanfeng.zahng@hand-china.com", id: "2020-07-28-hme_organization_unit_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_organization_unit_rel_s', startValue:"1")
        }
        createTable(tableName: "hme_organization_unit_rel", remarks: "组织职能关系表") {
            column(name: "rel_id", type: "varchar(" + 200 * weight + ")",  remarks: "表ID，主键")  {constraints(primaryKey: true)} 
            column(name: "organization_id", type: "varchar(" + 100 * weight + ")",  remarks: "组织id")  {constraints(nullable:"false")}  
            column(name: "organization_type", type: "varchar(" + 30 * weight + ")",  remarks: "组织类型")  {constraints(nullable:"false")}  
            column(name: "unit_id", type: "bigint(20)",  remarks: "部门id")  {constraints(nullable:"false")}  
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

        addUniqueConstraint(columnNames:"organization_id,organization_type,rel_id",tableName:"hme_organization_unit_rel",constraintName: "hme_organization_unit_rel_u1")
    }
}