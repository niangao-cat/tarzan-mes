package script.db

databaseChangeLog(logicalFilePath: 'script/db/mt_container_his.groovy') {
    changeSet(author: "jiangling.zheng@hand-china.com", id: "2020-06-30-mt_container_his") {
        def weight = 1
        if(helper.isSqlServer()){
            weight = 2
        } else if(helper.isOracle()){
            weight = 3
        }
        if(helper.dbType().isSupportSequence()){
            createSequence(sequenceName: 'mt_container_his_s', startValue:"1")
        }
        createTable(tableName: "mt_container_his", remarks: "容器历史") {
            column(name: "TENANT_ID", type: "bigint(20)",   defaultValue:"0",   remarks: "租户ID")  {constraints(nullable:"false")}  
            column(name: "CONTAINER_HIS_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "作为容器唯一标识，用于其他数据结构引用该容器")  {constraints(primaryKey: true)} 
            column(name: "CONTAINER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "容器ID")  {constraints(nullable:"false")}  
            column(name: "CONTAINER_CODE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "标识该容器的唯一编码CODE，用于可视化识别")  {constraints(nullable:"false")}  
            column(name: "CONTAINER_TYPE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "表示该容器所属的容器类型ID")  {constraints(nullable:"false")}  
            column(name: "CONTAINER_NAME", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "该容器的名称")   
            column(name: "DESCRIPTION", type: "varchar(" + 1000 * weight + ")",   defaultValue:"",   remarks: "对该容器的详细描述")   
            column(name: "SITE_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "描述该容器所在的站点标识ID")  {constraints(nullable:"false")}  
            column(name: "LOCATOR_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "描述该容器所在的货位标识ID")   
            column(name: "STATUS", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "描述该容器的状态，包括：")  {constraints(nullable:"false")}  
            column(name: "OWNER_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "描述容器下表示实物的所有权属于客户还是供应商，内容包括：C：客户，表示该容器表示的实物属于客户S：供应商，表示该容器表示的实物属于供应商空：表示容器表示的实物属于厂内自有")   
            column(name: "OWNER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "配合所有者类型使用，描述容器表示的实物具体的所有权对象，如所属于的具体供应商或具体客户，内容为供应商标识ID或客户标识ID")   
            column(name: "RESERVED_FLAG", type: "varchar(" + 1 * weight + ")",   defaultValue:"",   remarks: "指示物料批标识实物是否被保留")   
            column(name: "RESERVED_OBJECT_TYPE", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "当容器被保留时，指示容器的被保留对象的类型，内容包括：WO：该容器表示的实物被某一生产指令保留EO：该容器表示的实物被某一执行作业保留DRIVING：该容器表示的实物被某一驱动指令保留CUSTOM：该容器表示的实物被某一客户保留OO：该容器表示的实物被某一机会订单预留")   
            column(name: "RESERVED_OBJECT_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "当容器被保留时，指示容器的保留对象值，配合保留对象类型使用；优先考虑容器的保留和保留对象，其次考虑容器下表示实物的保留和保留对象")   
            column(name: "IDENTIFICATION", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "标识该容器临时使用的容器编码以外的可视化标识")  {constraints(nullable:"false")}  
            column(name: "LAST_LOAD_TIME", type: "datetime",  remarks: "描述该容器最后一次进行装载操作的时间")   
            column(name: "LAST_UNLOAD_TIME", type: "datetime",  remarks: "描述该容器最后一次进行卸载操作的时间")   
            column(name: "EVENT_ID", type: "varchar(" + 255 * weight + ")",   defaultValue:"",   remarks: "事件ID")  {constraints(nullable:"false")}  
            column(name: "CURRENT_CONTAINER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "当前容器ID")   
            column(name: "TOP_CONTAINER_ID", type: "varchar(" + 100 * weight + ")",   defaultValue:"",   remarks: "顶层容器ID")   
            column(name: "CID", type: "bigint(100)",  remarks: "")  {constraints(nullable:"false")}  
            column(name: "OBJECT_VERSION_NUMBER", type: "bigint(20)",   defaultValue:"1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "CREATION_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATED_BY", type: "bigint(20)",   defaultValue:"-1",   remarks: "")  {constraints(nullable:"false")}  
            column(name: "LAST_UPDATE_DATE", type: "datetime",   defaultValueComputed:"CURRENT_TIMESTAMP",   remarks: "")  {constraints(nullable:"false")}  

        }

        addUniqueConstraint(columnNames:"CONTAINER_ID,EVENT_ID,TENANT_ID",tableName:"mt_container_his",constraintName: "MT_CONTAINER_HIS_U1")
    }
}