package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosWipQueryDTO;
import com.ruike.hme.domain.vo.HmeCosWipQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HmeCosWipQueryMapper {

    /**
     * 查询COS在制信息
     *
     * @param tenantId
     * @param dto
     * @author yifan.xiong@hand-china.com 2020-9-28 14:43:54
     * @return java.util.List<HmeCosWipQueryVO>
     */
    List<HmeCosWipQueryVO> cosWipQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeCosWipQueryDTO dto);
}
