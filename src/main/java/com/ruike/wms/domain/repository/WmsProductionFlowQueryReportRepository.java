package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsProductionFlowQueryReportDTO;
import com.ruike.wms.domain.vo.WmsProductionFlowQueryReportVO;
import com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @author ywj
 * @version 0.0.1
 * @description 生产流转查询报表
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @return
 */
public interface WmsProductionFlowQueryReportRepository {

    /**
     * 查询工序流转信息
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return List<HmeEoTraceBackQueryDTO> HmeEoTraceBackQueryDTO
     * @author jiangling.zheng@hand-china.com
     */
    Page<WmsProductionFlowQueryReportVO> eoWorkcellQuery(Long tenantId, PageRequest pageRequest, WmsProductionFlowQueryReportDTO dto);
}
