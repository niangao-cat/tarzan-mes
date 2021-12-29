package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfSapSnIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 成品SN同步接口表Mapper
 *
 * @author li.zhang13@hand-china.com 2021-07-01 11:05:34
 */
public interface ItfSapSnIfaceMapper extends BaseMapper<ItfSapSnIface> {

    void update(@Param(value = "tenantId")Long tenantId,
                @Param(value = "status")String status,
                @Param(value = "msg")String msg,
                @Param(value = "ifaceId")String ifaceId);
}
