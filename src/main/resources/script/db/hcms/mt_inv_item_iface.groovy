package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_inv_item_iface.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_inv_item_iface") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_inv_item_iface_s', startValue:"1")
        }
        createTable(tableName: "mt_inv_item_iface", remarks: "物料接口表") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "IFACE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "PLANT_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "工厂CODE")  {constraints(nullable:"false")}  
            column(name: "ITEM_ID", type: "decimal(36,6)",  remarks: "物料Id")   
            column(name: "ITEM_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "物料编码")  {constraints(nullable:"false")}  
            column(name: "OLD_ITEM_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "旧物料号")   
            column(name: "PRIMARY_UOM", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "主单位")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTIONS", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "物料描述")  {constraints(nullable:"false")}  
            column(name: "DESCRIPTION_MIR", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "长描述")   
            column(name: "ENABLE_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "有效标识")  {constraints(nullable:"false")}  
            column(name: "ITEM_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料类型")   
            column(name: "PLANNING_MAKE_BUY_CODE", type: "varchar(" + 30 * weight + ")",   defaultValue:"",   remarks: "制造/采购标志")   
            column(name: "LOT_CONTROL_CODE", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "批次管理标志")   
            column(name: "PRERPOCESSING_LEAD_TIME", type: "decimal(36,6)",  remarks: "前处理提前期")   
            column(name: "PURCHASE_LEADTIME", type: "decimal(36,6)",  remarks: "采购提前期")   
            column(name: "WIP_LEAD_TIME", type: "decimal(36,6)",  remarks: "制造提前期")   
            column(name: "INSTOCK_LEAD_TIME", type: "decimal(36,6)",  remarks: "后处理提前期")   
            column(name: "MIN_PACK_QTY", type: "decimal(36,6)",  remarks: "最小包装")   
            column(name: "MINIMUM_ORDER_QUANTITY", type: "decimal(36,6)",  remarks: "最小起订量")   
            column(name: "BUYER_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "采购员CODE")   
            column(name: "QC_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "检验标识")   
            column(name: "RECEIVING_ROUTING_ID", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "接收方式")   
            column(name: "WIP_SUPPLY_TYPE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "wip发料类型")   
            column(name: "VMI_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "寄售vmi标识")   
            column(name: "ITEM_GROUP", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "物料组")   
            column(name: "PRODUCT_GROUP", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "产品组")   
            column(name: "ERP_CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP创建日期")  {constraints(nullable:"false")}  
            column(name: "ERP_CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "ERP创建人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "ERP最后更新人")  {constraints(nullable:"false")}  
            column(name: "ERP_LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "ERP最后更新日期")  {constraints(nullable:"false")}  
            column(name: "MATERIAL_DESIGN_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "物料图号")   
            column(name: "MATERIAL_IDENTIFY_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "物料简称")   
            column(name: "SIZE_UOM_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "尺寸单位编码")   
            column(name: "MODEL", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "材质/型号")   
            column(name: "LENGTH", type: "decimal(36,6)",  remarks: "长")   
            column(name: "WIDTH", type: "decimal(36,6)",  remarks: "宽")   
            column(name: "HEIGHT", type: "decimal(36,6)",  remarks: "高")   
            column(name: "VOLUME", type: "decimal(36,6)",  remarks: "体积")   
            column(name: "WEIGHT", type: "decimal(36,6)",  remarks: "重量")   
            column(name: "SHELF_LIFE", type: "decimal(36,6)",  remarks: "保质期")   
            column(name: "CONVERSION_RATE", type: "decimal(36,6)",  remarks: "主辅单位转换比例：基本计量单位/辅助单位")   
            column(name: "VOLUME_UOM_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "体积单位编码")   
            column(name: "WEIGHT_UOM_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "重量单位编码")   
            column(name: "SHELF_LIFE_UOM_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "保质期单位编码")   
            column(name: "SECONDARY_UOM_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "辅助单位编码")   
            column(name: "STOCK_LOCATOR_CODE", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "默认存储位置")   
            column(name: "BATCH_ID", type: "decimal(36,6)",  remarks: "数据批次ID")  {constraints(nullable:"false")}  
            column(name: "STATUS", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "数据处理状态，初始为N，失败为E，成功S，处理中P")  {constraints(nullable:"false")}  
            column(name: "MESSAGE", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "数据处理消息返回")   
            column(name: "ATTRIBUTE1", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE6", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE11", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE12", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE13", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE14", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE15", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE16", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE17", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE18", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE19", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE20", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE21", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE22", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE23", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE24", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE25", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE26", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE27", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE28", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE29", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "ATTRIBUTE30", type: "varchar(" + 150 * weight + ")",   defaultValue:"",   remarks: "")   
            column(name: "CID", type: "bigint(100)",  remarks: "CID")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"TENANT_ID,PLANT_CODE,ITEM_CODE,BATCH_ID",tableName:"mt_inv_item_iface",constraintName: "MT_INV_ITEM_IFACE_U1")
    }
}