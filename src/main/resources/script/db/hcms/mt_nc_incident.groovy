package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_nc_incident.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_nc_incident") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_nc_incident_s', startValue:"1")
        }
        createTable(tableName: "mt_nc_incident", remarks: "不良事故") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "NC_INCIDENT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "表ID，主键，供其他表做外键")  {constraints(primaryKey: true)} 
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "生产站点")  {constraints(nullable:"false")}  
            column(name: "INCIDENT_NUMBER", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "不良事故编码")  {constraints(nullable:"false")}  
            column(name: "NC_INCIDENT_STATUS", type: "varchar(" + 20 * weight + ")",   defaultValue:"",   remarks: "不良事故状态：")  {constraints(nullable:"false")}  
            column(name: "INCIDENT_DATE_TIME", type: "datetime",  remarks: "事故发生日期")  {constraints(nullable:"false")}  
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"SITE_ID,INCIDENT_NUMBER,TENANT_ID",tableName:"mt_nc_incident",constraintName: "MT_NC_INCIDENT_U1")
    }
}