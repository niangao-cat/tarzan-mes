package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_assemble_control.groovy') {
    changeSet(author: "liyuan.lv@hand-china.com", id: "2020-05-20-mt_assemble_control") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_assemble_control_s', startValue:"1")
        }
        createTable(tableName: "mt_assemble_control", remarks: "装配控制，定义一组装配控制要求，包括装配组可安装位置和装配点可装载物料") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "ASSEMBLE_CONTROL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键ID,表示唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "OBJECT_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "对象类型，包括MATERIAL、WO、EO")  {constraints(nullable:"false")}  
            column(name: "OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "对象的具体值")  {constraints(nullable:"false")}  
            column(name: "ORGANIZATION_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "组织类型，包括SITE/PRODUCTION_LINE/WKC")   
            column(name: "ORGANIZATION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "组织的具体值")   
            column(name: "REFERENCE_AREA", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "装配参考区域，针对多个产品一起装配或者一个产品拆分为多次装配的情况")   
            column(name: "DATE_FROM", type: "datetime",  remarks: "生效时间")  {constraints(nullable:"false")}  
            column(name: "DATE_TO", type: "datetime",  remarks: "失效生产")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"OBJECT_TYPE,OBJECT_ID,ORGANIZATION_TYPE,ORGANIZATION_ID,REFERENCE_AREA,TENANT_ID",tableName:"mt_assemble_control",constraintName: "mt_assemble_control_u1")
    }
}