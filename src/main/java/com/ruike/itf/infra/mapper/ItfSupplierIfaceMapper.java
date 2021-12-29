package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfSupplierIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应商数据接口表Mapper
 *
 * @author yapeng.yao@hand-china.com 2020-08-19 18:49:46
 */
public interface ItfSupplierIfaceMapper extends BaseMapper<ItfSupplierIface> {

    /**
     * 供应商数据全量表
     * @param tableName
     * @param itfSupplierIfaceList
     */
    void batchInsertItemIface(@Param(value = "tableName") String tableName,
                              @Param(value = "itfSupplierIfaceList") List<ItfSupplierIface> itfSupplierIfaceList);

    /**
     * 供应商数据业务表
     * @param tableName
     * @param itfSupplierIfaceList
     */
    void batchInsertItem(@Param(value = "tableName") String tableName,
                              @Param(value = "itfSupplierIfaceList") List<ItfSupplierIface> itfSupplierIfaceList);

}
