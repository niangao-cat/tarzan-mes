package script.db

databaseChangeLog(logicalFilePath: 'script/db/hme_chip_import_data.groovy') {
    changeSet(author: "chaonan.hu@hand-china.com", id: "2021-01-25-hme_chip_import_data") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'hme_chip_import_data_s', startValue:"1")
        }
        createTable(tableName: "hme_chip_import_data", remarks: "六型芯片导入临时表") {
            column(name: "tenant_id", type: "bigint(20)",   defaultValue:"0",   remarks: "租户id")  {constraints(nullable:"false")}  
            column(name: "kid", type: "varchar(" + 100 * weight + ")",  remarks: "主键")  {constraints(primaryKey: true)} 
            column(name: "print_flag", type: "varchar(" + 1 * weight + ")",  remarks: "是否打印")  {constraints(nullable:"false")}  
            column(name: "work_num", type: "varchar(" + 10 * weight + ")",  remarks: "工单")  {constraints(nullable:"false")}  
            column(name: "cos_type", type: "varchar(" + 10 * weight + ")",  remarks: "COS类型")  {constraints(nullable:"false")}  
            column(name: "workcell", type: "varchar(" + 10 * weight + ")",  remarks: "工位")  {constraints(nullable:"false")}  
            column(name: "import_lot", type: "varchar(" + 255 * weight + ")",  remarks: "导入批次")  {constraints(nullable:"false")}  
            column(name: "target_barcode", type: "varchar(" + 255 * weight + ")",  remarks: "目标条码")  {constraints(nullable:"false")}  
            column(name: "source_barcode", type: "varchar(" + 255 * weight + ")",  remarks: "来料条码")  {constraints(nullable:"false")}  
            column(name: "fox_num", type: "varchar(" + 255 * weight + ")",  remarks: "盒号")  {constraints(nullable:"false")}  
            column(name: "wafer", type: "varchar(" + 255 * weight + ")",  remarks: "WAFER")  {constraints(nullable:"false")}  
            column(name: "container_type", type: "varchar(" + 10 * weight + ")",  remarks: "容器类型")  {constraints(nullable:"false")}  
            column(name: "lotno", type: "varchar(" + 10 * weight + ")",  remarks: "LOTNO")   
            column(name: "avg_wavelenght", type: "varchar(" + 10 * weight + ")",  remarks: "Avg (nm)")   
            column(name: "type", type: "varchar(" + 10 * weight + ")",  remarks: "TYPE")   
            column(name: "remark", type: "varchar(" + 255 * weight + ")",  remarks: "备注")   
            column(name: "position", type: "varchar(" + 10 * weight + ")",  remarks: "位置")  {constraints(nullable:"false")}  
            column(name: "bar_num", type: "bigint(2)",  remarks: "Bar条数")  {constraints(nullable:"false")}  
            column(name: "qty", type: "bigint(6)",  remarks: "合格芯片数")  {constraints(nullable:"false")}  
            column(name: "a001", type: "bigint(2)",  remarks: "")   
            column(name: "a002", type: "bigint(2)",  remarks: "")   
            column(name: "a003", type: "bigint(2)",  remarks: "")   
            column(name: "a004", type: "bigint(2)",  remarks: "")   
            column(name: "a005", type: "bigint(2)",  remarks: "")   
            column(name: "a006", type: "bigint(2)",  remarks: "")   
            column(name: "a007", type: "bigint(2)",  remarks: "")   
            column(name: "a008", type: "bigint(2)",  remarks: "")   
            column(name: "a009", type: "bigint(2)",  remarks: "")   
            column(name: "a010", type: "bigint(2)",  remarks: "")   
            column(name: "a011", type: "bigint(2)",  remarks: "")   
            column(name: "a012", type: "bigint(2)",  remarks: "")   
            column(name: "a013", type: "bigint(2)",  remarks: "")   
            column(name: "a014", type: "bigint(2)",  remarks: "")   
            column(name: "a015", type: "bigint(2)",  remarks: "")   
            column(name: "a016", type: "bigint(2)",  remarks: "")   
            column(name: "a017", type: "bigint(2)",  remarks: "")   
            column(name: "a018", type: "bigint(2)",  remarks: "")   
            column(name: "a019", type: "bigint(2)",  remarks: "")   
            column(name: "a020", type: "bigint(2)",  remarks: "")   
            column(name: "a021", type: "bigint(2)",  remarks: "")   
            column(name: "a022", type: "bigint(2)",  remarks: "")   
            column(name: "a023", type: "bigint(2)",  remarks: "")   
            column(name: "a024", type: "bigint(2)",  remarks: "")   
            column(name: "a025", type: "bigint(2)",  remarks: "")   
            column(name: "a026", type: "bigint(2)",  remarks: "")   
            column(name: "a027", type: "bigint(2)",  remarks: "")   
            column(name: "a028", type: "bigint(2)",  remarks: "")   
            column(name: "a029", type: "bigint(2)",  remarks: "")   
            column(name: "a030", type: "bigint(2)",  remarks: "")   
            column(name: "a031", type: "bigint(2)",  remarks: "")   
            column(name: "a032", type: "bigint(2)",  remarks: "")   
            column(name: "a033", type: "bigint(2)",  remarks: "")   
            column(name: "a034", type: "bigint(2)",  remarks: "")   
            column(name: "a035", type: "bigint(2)",  remarks: "")   
            column(name: "a036", type: "bigint(2)",  remarks: "")   
            column(name: "a037", type: "bigint(2)",  remarks: "")   
            column(name: "a038", type: "bigint(2)",  remarks: "")   
            column(name: "a039", type: "bigint(2)",  remarks: "")   
            column(name: "a040", type: "bigint(2)",  remarks: "")   
            column(name: "a041", type: "bigint(2)",  remarks: "")   
            column(name: "a042", type: "bigint(2)",  remarks: "")   
            column(name: "a043", type: "bigint(2)",  remarks: "")   
            column(name: "a044", type: "bigint(2)",  remarks: "")   
            column(name: "a045", type: "bigint(2)",  remarks: "")   
            column(name: "a046", type: "bigint(2)",  remarks: "")   
            column(name: "a047", type: "bigint(2)",  remarks: "")   
            column(name: "a048", type: "bigint(2)",  remarks: "")   
            column(name: "a049", type: "bigint(2)",  remarks: "")   
            column(name: "a050", type: "bigint(2)",  remarks: "")   
            column(name: "a051", type: "bigint(2)",  remarks: "")   
            column(name: "a052", type: "bigint(2)",  remarks: "")   
            column(name: "a053", type: "bigint(2)",  remarks: "")   
            column(name: "a054", type: "bigint(2)",  remarks: "")   
            column(name: "a055", type: "bigint(2)",  remarks: "")   
            column(name: "a056", type: "bigint(2)",  remarks: "")   
            column(name: "a057", type: "bigint(2)",  remarks: "")   
            column(name: "a058", type: "bigint(2)",  remarks: "")   
            column(name: "a059", type: "bigint(2)",  remarks: "")   
            column(name: "a060", type: "bigint(2)",  remarks: "")   
            column(name: "a061", type: "bigint(2)",  remarks: "")   
            column(name: "a062", type: "bigint(2)",  remarks: "")   
            column(name: "a063", type: "bigint(2)",  remarks: "")   
            column(name: "a064", type: "bigint(2)",  remarks: "")   
            column(name: "a065", type: "bigint(2)",  remarks: "")   
            column(name: "a066", type: "bigint(2)",  remarks: "")   
            column(name: "a067", type: "bigint(2)",  remarks: "")   
            column(name: "a068", type: "bigint(2)",  remarks: "")   
            column(name: "a069", type: "bigint(2)",  remarks: "")   
            column(name: "a070", type: "bigint(2)",  remarks: "")   
            column(name: "a071", type: "bigint(2)",  remarks: "")   
            column(name: "a072", type: "bigint(2)",  remarks: "")   
            column(name: "a073", type: "bigint(2)",  remarks: "")   
            column(name: "a074", type: "bigint(2)",  remarks: "")   
            column(name: "a075", type: "bigint(2)",  remarks: "")   
            column(name: "a076", type: "bigint(2)",  remarks: "")   
            column(name: "a077", type: "bigint(2)",  remarks: "")   
            column(name: "a078", type: "bigint(2)",  remarks: "")   
            column(name: "a079", type: "bigint(2)",  remarks: "")   
            column(name: "a080", type: "bigint(2)",  remarks: "")   
            column(name: "a081", type: "bigint(2)",  remarks: "")   
            column(name: "a082", type: "bigint(2)",  remarks: "")   
            column(name: "a083", type: "bigint(2)",  remarks: "")   
            column(name: "a084", type: "bigint(2)",  remarks: "")   
            column(name: "a085", type: "bigint(2)",  remarks: "")   
            column(name: "a086", type: "bigint(2)",  remarks: "")   
            column(name: "a087", type: "bigint(2)",  remarks: "")   
            column(name: "a088", type: "bigint(2)",  remarks: "")   
            column(name: "a089", type: "bigint(2)",  remarks: "")   
            column(name: "a090", type: "bigint(2)",  remarks: "")   
            column(name: "a091", type: "bigint(2)",  remarks: "")   
            column(name: "a092", type: "bigint(2)",  remarks: "")   
            column(name: "a093", type: "bigint(2)",  remarks: "")   
            column(name: "a094", type: "bigint(2)",  remarks: "")   
            column(name: "a095", type: "bigint(2)",  remarks: "")   
            column(name: "a096", type: "bigint(2)",  remarks: "")   
            column(name: "a097", type: "bigint(2)",  remarks: "")   
            column(name: "a098", type: "bigint(2)",  remarks: "")   
            column(name: "a099", type: "bigint(2)",  remarks: "")   
            column(name: "a100", type: "bigint(2)",  remarks: "")   
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

        addUniqueConstraint(columnNames:"tenant_id,fox_num,position",tableName:"hme_chip_import_data",constraintName: "hme_chip_import_data_u1")
    }
}