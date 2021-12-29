package com.ruike.reports.infra.mapper;

import java.util.List;

import com.ruike.reports.api.dto.HmeSsnInspectResultDTO;
import com.ruike.reports.domain.vo.HmeSsnInspectResultHeaderLinesVO;
import com.ruike.reports.domain.vo.HmeSsnInspectResultHeaderVO;
import com.ruike.reports.domain.vo.HmeSsnInspectResultLineVO;
import org.apache.ibatis.annotations.Param;

/**
 * 标准件检验结果查询 mapper
 *
 * @author wenqiang.yin@hand-china.com 2021/02/04 8:34
 */
public interface HmeSsnInspectResultMapper {

    /**
     *
     *
     * @param tenantId
     * @param dto
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-16 10:29
     * @return HmeSsnInspectResultHeaderLinesVO
     */
    List<HmeSsnInspectResultHeaderLinesVO> selectHeaderLinesByCondition(@Param("tenantId") Long tenantId, @Param("dto") HmeSsnInspectResultDTO dto);
}
