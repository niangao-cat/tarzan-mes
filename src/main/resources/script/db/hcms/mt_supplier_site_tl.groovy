package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_supplier_site_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_supplier_site_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_supplier_site_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_supplier_site_tl", remarks: "") {
            column(name: "SUPPLIER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "SUPPLIER_SITE_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "供应商地点名称")   
            column(name: "COUNTRY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "国家")   
            column(name: "PROVINCE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "省")   
            column(name: "CITY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "市")   
            column(name: "COUNTY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "区")   
            column(name: "ADDRESS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "详细地址")   
            column(name: "PERSON", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "联系人")   

        }

        addUniqueConstraint(columnNames:"SUPPLIER_SITE_ID,LANG",tableName:"mt_supplier_site_tl",constraintName: "mt_supplier_site_tl_pk")
    }
}