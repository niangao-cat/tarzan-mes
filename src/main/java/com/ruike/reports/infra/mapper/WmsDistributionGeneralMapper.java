package com.ruike.reports.infra.mapper;

import com.ruike.reports.api.dto.WmsDistributionGeneralQueryDTO;
import com.ruike.reports.domain.vo.WmsDistributionGeneralVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 配送综合查询报表 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 16:30
 */
public interface WmsDistributionGeneralMapper {

    /**
     * 根据条件查询列表
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.reports.domain.vo.WmsDistributionGeneralVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 04:31:47
     */
    List<WmsDistributionGeneralVO> selectListByCondition(@Param("tenantId") Long tenantId,
                                                         @Param("dto") WmsDistributionGeneralQueryDTO dto);
}
