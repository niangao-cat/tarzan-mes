package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_container_type.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_container_type") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_container_type_s', startValue:"1")
        }
        createTable(tableName: "mt_container_type", remarks: "容器类型，定义一类容器和一类容器的控制属性，包括容器可装载的对象类型、最大容量、混装类型等") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "CONTAINER_TYPE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "作为物料批唯一标识，用于其他数据结构引用该容器类型")  {constraints(primaryKey: true)} 
            column(name: "CONTAINER_TYPE_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "该容器类型的编码CODE")  {constraints(nullable:"false")}  
            column(name: "CONTAINER_TYPE_DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "该容器类型的描述")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "描述该容器类型的有效状描述该容器类型的有效状态：Y：是，表示物料批当前有效N：否，表示物料批当前已经无效态：")  {constraints(nullable:"false")}  
            column(name: "PACKING_LEVEL", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "该容器类型限定的装载对象类型：EO：表示该类容器作为在制品流转载具使用，允许装入执行作业MATERIAL_LOT：表示该类容器允许装入物料批CONTAINER：表示该类容器允许装入其他容器空：不做限制")   
            column(name: "CAPACITY_QTY", type: "decimal(20,6)",  remarks: "表示该类容器最多允许装入的数量（通过主单位计量）")   
            column(name: "MIXED_MATERIAL_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "指示该类容器是否允许放入不同的物料Y：允许放入多种物料N：只能放入一种物料")   
            column(name: "MIXED_EO_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "指示该类容器是否允许放入不同执行作业的产品Y：允许放入多个执行作业的产品N：只能放入一个执行作业的产品")   
            column(name: "MIXED_WO_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "指示该类容器是否允许放入不同生产指令的产品或在制品Y：允许放入多个生产指令的产品或在制品N：只能放入一个生产指令的产品或在制品")   
            column(name: "MIXED_OWNER_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "指示该类容器是否允许放入所有权的产品Y：允许放入多个所有权的产品N：只能放入一个所有权的产品")   
            column(name: "LENGTH", type: "decimal(20,6)",  remarks: "指示该容器的长度值，配合尺寸单位一起使用")   
            column(name: "WIDTH", type: "decimal(20,6)",  remarks: "指示该容器的宽度值，配合尺寸单位一起使用")   
            column(name: "HEIGHT", type: "decimal(20,6)",  remarks: "指示该容器的高度值，配合尺寸单位一起使用")   
            column(name: "WEIGHT", type: "decimal(20,6)",  remarks: "指示该容器的重量值，配合重量单位一起使用")   
            column(name: "SIZE_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "指定容器长宽高值的数值单位，如MM/CM/M，需与单位维护内容保持一致")   
            column(name: "MAX_LOAD_WEIGHT", type: "decimal(20,6)",  remarks: "指示该容器最大可承载重量值，配合重量单位一起使用")   
            column(name: "WEIGHT_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "指定容器重量、承载重量值的单位，如KG/G/T，应与单位维护内容保持一致")   
            column(name: "LOCATION_ENABLED_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "指示是否需要启用容器的位置，启用后需通过行、列字段定义容器的位置")   
            column(name: "LOCATION_ROW", type: "bigint(20)",  remarks: "启用容器位置时定义该类容器的行数")   
            column(name: "LOCATION_COLUMN", type: "bigint(20)",  remarks: "启用容器位置时定义该类容器的列数")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"CONTAINER_TYPE_CODE,TENANT_ID",tableName:"mt_container_type",constraintName: "MT_CONTAINER_TYPE_U1")
    }
}