package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfCustomerIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户数据全量接口表Mapper
 *
 * @author yapeng.yao@hand-china.com 2020-08-21 11:19:59
 */
public interface ItfCustomerIfaceMapper extends BaseMapper<ItfCustomerIface> {

    /**
     * 客户数据
     *
     * @param tableName
     * @param itfCustomerIfaceList
     */
    void batchInsertCustomer(@Param(value = "tableName") String tableName,
                             @Param(value = "itfCustomerIfaceList") List<ItfCustomerIface> itfCustomerIfaceList);

}