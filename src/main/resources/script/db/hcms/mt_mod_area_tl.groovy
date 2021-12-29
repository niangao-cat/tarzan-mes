package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_mod_area_tl.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_mod_area_tl") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_mod_area_tl_s', startValue:"1")
        }
        createTable(tableName: "mt_mod_area_tl", remarks: "") {
            column(name: "AREA_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LANG", type: "varchar(" + 10 * weight + ")",   defaultValue:"",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "COUNTRY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "国家")   
            column(name: "PROVINCE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "省")   
            column(name: "CITY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "市")   
            column(name: "COUNTY", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "县")   
            column(name: "ADDRESS", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "除国家省市县的详细地址")   
            column(name: "AREA_NAME", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "")   

        }

        addUniqueConstraint(columnNames:"AREA_ID,LANG",tableName:"mt_mod_area_tl",constraintName: "mt_mod_area_tl_pk")
    }
}