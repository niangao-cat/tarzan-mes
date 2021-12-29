package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_material_lot.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_material_lot") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_material_lot_s', startValue:"1")
        }
        createTable(tableName: "mt_material_lot", remarks: "物料批，提供实物管理简易装载物料的结构，记录包括物料、位置、状态、数量、所有者、预留对象等数据") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_LOT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "作为物料批唯一标识，用于其他数据结构引用该物料批")  {constraints(primaryKey: true)} 
            column(name: "MATERIAL_LOT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "描述该物料批的唯一编码，用于方便识别")  {constraints(nullable:"false")}  
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "该物料批所在生产站点的标识ID")  {constraints(nullable:"false")}  
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "描述该物料批的有效状态：描述该物料批的有效状态：Y：是，表示物料批当前有效N：否，表示物料批当前已经无效")  {constraints(nullable:"false")}  
            column(name: "QUALITY_STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "物料批所标识实物的质量状态：")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "该物料批所表示的实物的物料标识ID")  {constraints(nullable:"false")}  
            column(name: "PRIMARY_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "该物料批表示实物的主计量单位")  {constraints(nullable:"false")}  
            column(name: "PRIMARY_UOM_QTY", type: "decimal(20,6)",  remarks: "实物在主计量单位下的数量")  {constraints(nullable:"false")}  
            column(name: "SECONDARY_UOM_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "该物料批表示实物的辅助计量单位")   
            column(name: "SECONDARY_UOM_QTY", type: "decimal(20,6)",  remarks: "实物在辅助计量单位下的数量")   
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批当前所在货位标识ID，表示物料批仓库下存储位置")   
            column(name: "ASSEMBLE_POINT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批所在装配点")   
            column(name: "LOAD_TIME", type: "datetime",  remarks: "指示将物料装载到物料批的时间，在物料批装载时进行赋值")   
            column(name: "UNLOAD_TIME", type: "datetime",  remarks: "指示将物料批内物料消耗的时间，在物料批卸载时赋值，每次卸载时都更新")   
            column(name: "OWNER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "描述物料批表示的实物所有权属于客户还是供应商，包括：C：客户，表示该物料批对应物料属于客户 S：供应商，表示该物料批对应物料属于供应商 空：表示物料批属于厂内自有")   
            column(name: "OWNER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "配合所有者类型使用，描述物料批表示的实物具体的所有权对象，如所属于的具体供应商或具体客户，内容为供应商标识ID或客户标识ID")   
            column(name: "LOT", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "指示物料批所表示实物的来源批次编号")   
            column(name: "OVEN_NUMBER", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "实物存在炉号管理时，指示物料批所表示实物对应的炉号")   
            column(name: "SUPPLIER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批对应实物通过采购获取时，指示物料批采购来源供应商标识ID")   
            column(name: "SUPPLIER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批对应实物通过采购获取时，指示物料批采购来源供应商地点标识ID")   
            column(name: "CUSTOMER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批对应实物通过客户退货获取时，指示物料批采购来源客户标识ID物料批对应实物通过客户退货获取时，指示物料批采购来源客户地点标识ID")   
            column(name: "CUSTOMER_SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料批对应实物通过客户退货获取时，指示物料批采购来源客户地点标识ID")   
            column(name: "RESERVED_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"N",   remarks: "指示物料批标识实物是否被保留，默认值为N")   
            column(name: "RESERVED_OBJECT_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "当物料批被保留时，指示物料批的保留对象类型，内容包括：WO：该物料批被某一生产指令保留 EO：该物料批被某一执行作业保留 DRIVING：该物料批被某一驱动指令保留 CUSTOM：该物料批被某一客户保留 OO：该物料批被某一机会订单预留")   
            column(name: "RESERVED_OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "当物料批被保留时，指示物料批的保留对象值，配合保留对象类型使用")   
            column(name: "CREATE_REASON", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "该物料批被创建的原因，放入原因代码，包括：1.INITIALIZE：初始化 2.INVENTORY：盘点 3.SPLIT：拆分 4.MERAGE：合并 5.COMPLETE：完工 6.SUPPLY：外供")   
            column(name: "IDENTIFICATION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "表示该物料批使用的物料批编码外的可视化标识")  {constraints(nullable:"false")}  
            column(name: "EO_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "当物料批由于执行作业完工被创建时，指示物料批的来源EO")   
            column(name: "IN_LOCATOR_TIME", type: "datetime",  remarks: "入库时间 ，表示物料批进入当前库位的时间（生效时更新，库位变化时更新）")   
            column(name: "FREEZE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "物料批冻结标识，用来盘点时冻结物料批的移动。为Y时不允许移动物料批。")   
            column(name: "STOCKTAKE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "盘点停用标识")   
            column(name: "BUSINESS_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "业务类型")   
            column(name: "INSTRUCTION_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "指令id")   
            column(name: "IN_SITE_TIME", type: "datetime",  remarks: "入场时间")   
            column(name: "CURRENT_CONTAINER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "当前容器ID")   
            column(name: "TOP_CONTAINER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "顶层容器ID")   
            column(name: "INSTRUCTION_DOC_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "指令单据ID")   
            column(name: "LATEST_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "最新一次新增历史表的主键")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"IDENTIFICATION,TENANT_ID",tableName:"mt_material_lot",constraintName: "MT_MATERIAL_LOT_U1")
        addUniqueConstraint(columnNames:"MATERIAL_LOT_CODE,TENANT_ID",tableName:"mt_material_lot",constraintName: "MT_MATERIAL_LOT_U2")
    }
}