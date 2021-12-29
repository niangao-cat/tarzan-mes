package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsProductionFlowQueryReportDTO;
import com.ruike.wms.domain.vo.WmsProductionFlowQueryReportVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description 生产流转查询报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
public interface WmsProductionFlowQueryReportMapper {

    /**
     * 查询工序流转信息
     *
     * @param tenantId 租户ID
     * @param topSiteId 顶层站点ID
     * @param dto 查询信息
     * @return
     */
    List<WmsProductionFlowQueryReportVO> eoWorkcellQuery(@Param("tenantId") Long tenantId,
                                                         @Param("topSiteId") String topSiteId,
                                                         @Param("dto") WmsProductionFlowQueryReportDTO dto);

    /**
     * 查询不良信息点击标识
     *
     * @param tenantId   租户Id
     * @param workcellId 工位Id
     * @param eoId       EO
     * @return java.lang.Long
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/15 15:07:45
     */
    Long ncInfoFlagQuery(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                         @Param("eoId") String eoId);
}
