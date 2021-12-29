package com.ruike.reports.infra.mapper;

import com.ruike.reports.api.dto.WmsTransferSummaryQueryDTO;
import com.ruike.reports.api.dto.WmsTransferSummaryQueryDTO2;
import com.ruike.reports.domain.vo.WmsTransferSummaryVO;
import com.ruike.reports.domain.vo.WmsTransferSummaryVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 调拨汇总报表 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/23 14:29
 */
public interface WmsTransferSummaryMapper {

    /**
     * 查询列表
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.reports.domain.vo.WmsTransferSummaryVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/23 02:51:08
     */
    List<WmsTransferSummaryVO> selectList(@Param("tenantId") Long tenantId,
                                          @Param("dto") WmsTransferSummaryQueryDTO dto);

    /**
     * 查询详情列表
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/28 17:14:17
     * @return java.util.List<com.ruike.reports.domain.vo.WmsTransferSummaryVO2>
     */
    List<WmsTransferSummaryVO2> selectDetailList(@Param("tenantId") Long tenantId,
                                                 @Param("dto") WmsTransferSummaryQueryDTO2 dto);
}
