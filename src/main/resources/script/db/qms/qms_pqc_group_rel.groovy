package script.db

databaseChangeLog(logicalFilePath: 'script/db/qms_pqc_group_rel.groovy') {
    changeSet(author: "sanfeng.zhang@hand-china.com", id: "2020-08-12-qms_pqc_group_rel") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'qms_pqc_group_rel_s', startValue:"1")
        }
        createTable(tableName: "qms_pqc_group_rel", remarks: "巡检物料与检验组关系表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "PQC_GROUP_REL_ID", type: "varchar(" + 100 * weight + ")",  remarks: "主键id")  {constraints(primaryKey: true)} 
            column(name: "SCHEME_ID", type: "varchar(" + 100 * weight + ")",  remarks: "物料检验计划主键ID")  {constraints(nullable:"false")}  
            column(name: "TAG_GROUP_ID", type: "varchar(" + 100 * weight + ")",  remarks: "检验组ID")  {constraints(nullable:"false")}  
            column(name: "REMARK", type: "varchar(" + 500 * weight + ")",  remarks: "备注")   
            column(name: "attribute1", type: "varchar(" + 150 * weight + ")",  remarks: "扩展字段1")   
            column(name: "attribute2", type: "varchar(" + 150 * weight + ")",  remarks: "扩展字段2")   
            column(name: "attribute3", type: "varchar(" + 150 * weight + ")",  remarks: "扩展字段3")   
            column(name: "cid", type: "bigint(100)",  remarks: "cid")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

    }
}