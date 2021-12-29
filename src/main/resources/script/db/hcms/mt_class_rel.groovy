package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_class_rel.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_class_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_class_rel_s', startValue:"1")
        }
        createTable(tableName: "mt_class_rel", remarks: "") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "REL_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "CLASS_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "编码")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "描述")   
            column(name: "SERVICE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "服务API")  {constraints(nullable:"false")}  
            column(name: "BEFORE_FUN", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "预操作")   
            column(name: "AFTER_FUN", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "完结操作")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"CLASS_CODE,TENANT_ID",tableName:"mt_class_rel",constraintName: "mt_class_rel_u1")
    }
}