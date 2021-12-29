package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_area_distribution.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_area_distribution") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_area_distribution_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_area_distribution", remarks: "区域配送属性") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "AREA_DISTRIBUTION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键，唯一标识")  {constraints(primaryKey: true)} 
            column(name: "AREA_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "区域ID，标识唯一区域")  {constraints(nullable:"false")}  
            column(name: "DISTRIBUTION_MODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "配送模式：定时补货拉动、定量补货拉动、按订单补货拉动、定时顺序拉动")   
            column(name: "PULL_TIME_INTERVAL_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "是否启用拉动时段（按订单补货以及定时顺序拉动启用，其它模式不启用）")   
            column(name: "DISTRIBUTION_CYCLE", type: "decimal(36,6)",  remarks: "配送周期T")   
            column(name: "BUSINESS_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "指令业务类型")   
            column(name: "INSTRUCT_CREATED_BY_EO", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "指令按照EO做拆分")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"AREA_ID,TENANT_ID",tableName:"mt_mod_area_distribution",constraintName: "MT_MOD_AREA_DISTRIBUTION_U1")
    }
}