package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosTestPassRateDTO;
import com.ruike.hme.domain.entity.HmeCosTestPassRate;
import com.ruike.hme.domain.vo.HmeCosTestPassRateVO;
import feign.Param;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * COS测试良率维护表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-09-06 11:44:38
 */
public interface HmeCosTestPassRateMapper extends BaseMapper<HmeCosTestPassRate> {
    /**
     * COS测试良率维护表基础查询
     *
     * @param tenantId 租户id
     * @param dto      查询条件
     * @return
     */
    List<HmeCosTestPassRateVO> queryCosTestPassRate(@Param("tenantId") Long tenantId, @Param("dto") HmeCosTestPassRateDTO dto);

}
