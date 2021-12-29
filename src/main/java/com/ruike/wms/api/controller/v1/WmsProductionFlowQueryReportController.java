package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsProductionFlowQueryReportDTO;
import com.ruike.wms.api.dto.WorkOrderInProcessDetailsQueryReportDTO;
import com.ruike.wms.domain.repository.WmsProductionFlowQueryReportRepository;
import com.ruike.wms.domain.repository.WorkOrderInProcessDetailsQueryReportRepository;
import com.ruike.wms.domain.vo.WmsProductionFlowQueryReportVO;
import com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description 生产流转查询报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
@RestController("WmsProductionFlowQueryReportController.v1")
@RequestMapping("/v1/{organizationId}/production-flow-query-report")
public class WmsProductionFlowQueryReportController {

    @Autowired
    private WmsProductionFlowQueryReportRepository wmsProductionFlowQueryReportRepository;

    @ApiOperation(value = "查询报表信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<WmsProductionFlowQueryReportVO>> list(@PathVariable("organizationId") Long tenantId,
                                                                     WmsProductionFlowQueryReportDTO dto, PageRequest pageRequest) {
        return Results.success(wmsProductionFlowQueryReportRepository.eoWorkcellQuery(tenantId, pageRequest, dto));
    }
}
