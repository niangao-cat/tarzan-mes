package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsProductionExecutionWholeProcessMonitoringReportDTO;
import com.ruike.wms.domain.repository.WmsProductionExecutionWholeProcessMonitoringReportRepository;
import com.ruike.wms.domain.vo.WmsProductionExecutionWholeProcessMonitoringReportVO;
import com.ruike.wms.infra.mapper.WmsProductionExecutionWholeProcessMonitoringReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/***
 * @description：生产执行全过程监控报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/2/19
 * @time 15:03
 * @version 0.0.1
 * @return
 */
@Component
public class WmsProductionExecutionWholeProcessMonitoringReportRepositoryImpl implements WmsProductionExecutionWholeProcessMonitoringReportRepository {

    @Autowired
    WmsProductionExecutionWholeProcessMonitoringReportMapper mapper;


    /**
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO>
     * @description 工单在制明细查询报表
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/18
     * @time 15:31
     * @version 0.0.1
     */
    @Override
    public Page<WmsProductionExecutionWholeProcessMonitoringReportVO> list(Long tenantId, PageRequest pageRequest, WmsProductionExecutionWholeProcessMonitoringReportDTO dto) {
        if(StringUtils.isNotEmpty(dto.getItemMaterialAttr())){
            dto.setItemMaterialList(Arrays.asList(dto.getItemMaterialAttr().split(",")));
        }
        Page<WmsProductionExecutionWholeProcessMonitoringReportVO> result = PageHelper.doPage(pageRequest, () -> mapper.list(tenantId, dto));
        return result;
    }

}
