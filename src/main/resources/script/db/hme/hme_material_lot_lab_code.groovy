package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_material_lot_lab_code.groovy') {
    changeSet(author: "penglin.sui@hand-china.com", id: "2021-01-25-hme_material_lot_lab_code") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_material_lot_lab_code_s', startValue:"1")
        }
        createTable(tableName: "hme_material_lot_lab_code", remarks: "条码实验代码表") {
            column(name: "tenant_id", type: "bigint(20)",  remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "lab_code_id", type: "varchar(" + 100 * weight + ")",  remarks: "主键ID，标识唯一一条记录")  {constraints(primaryKey: true)} 
            column(name: "material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "物料批ID")  {constraints(nullable:"false")}  
            column(name: "object", type: "varchar(" + 10 * weight + ")",  remarks: "对象")   
            column(name: "material_lot_load_id", type: "varchar(" + 100 * weight + ")",  remarks: "装载行ID")   
            column(name: "lab_code", type: "varchar(" + 50 * weight + ")",  remarks: "实验代码")  {constraints(nullable:"false")}  
            column(name: "job_id", type: "varchar(" + 100 * weight + ")",  remarks: "工序作业ID")   
            column(name: "workcell_id", type: "varchar(" + 100 * weight + ")",  remarks: "工位ID")   
            column(name: "work_order_id", type: "varchar(" + 100 * weight + ")",  remarks: "工单ID")   
            column(name: "source_object", type: "varchar(" + 10 * weight + ")",  remarks: "来源对象（原材料条码、工序）")  {constraints(nullable:"false")}  
            column(name: "router_step_id", type: "varchar(" + 100 * weight + ")",  remarks: "步骤ID")   
            column(name: "source_material_lot_id", type: "varchar(" + 100 * weight + ")",  remarks: "来源物料批ID")   
            column(name: "source_material_id", type: "varchar(" + 100 * weight + ")",  remarks: "来源物料")   
            column(name: "enable_flag", type: "varchar(" + 1 * weight + ")",  remarks: "有效性")  {constraints(nullable:"false")}  
            column(name: "cid", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "object_version_number", type: "bigint(20)",   defaultValue:"1",   remarks: "行版本号，用来处理锁")  {constraints(nullable:"false")}  
            column(name: "creation_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "created_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_updated_by", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "last_update_date", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "ATTRIBUTE_CATEGORY", type: "varchar(" + 30 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE1", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE2", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE3", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE4", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE5", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE6", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE7", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE8", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE9", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE10", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE11", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE12", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE13", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE14", type: "varchar(" + 150 * weight + ")",  remarks: "")   
            column(name: "ATTRIBUTE15", type: "varchar(" + 150 * weight + ")",  remarks: "")   

        }

        addUniqueConstraint(columnNames:"tenant_id,material_lot_id,router_step_id,lab_code",tableName:"hme_material_lot_lab_code",constraintName: "hme_material_lot_lab_code_u1")
    }
}