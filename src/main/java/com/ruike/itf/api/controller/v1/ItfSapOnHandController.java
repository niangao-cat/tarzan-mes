package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfOnHandDTO;
import com.ruike.itf.domain.vo.MesOnHandVO;
import com.ruike.itf.domain.vo.SapOnHandVO;
import com.ruike.itf.app.service.ItfSapOnHandService;
import com.sap.conn.jco.JCoException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO10;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工单接口表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@RestController("ItfSapOnHandController.v1")
@RequestMapping("/v1/{organizationId}/itf-sap-on-hand")
@Slf4j
public class ItfSapOnHandController extends BaseController {


    private final ItfSapOnHandService itfSapOnHandService;

    public ItfSapOnHandController(ItfSapOnHandService itfSapOnHandService) {
        this.itfSapOnHandService = itfSapOnHandService;
    }

    @ApiOperation(value = "获取SAP现有量")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/on-hand-report")
    public ResponseEntity<List<MesOnHandVO>> onHandReport(@PathVariable("organizationId") Long tenantId, @RequestBody ItfOnHandDTO dto) throws JCoException {
        log.info("plantCode:{}", dto);
        List<MesOnHandVO> onHandDTOS = itfSapOnHandService.onHandReport(tenantId, dto);
        return Results.success(onHandDTOS);
    }

    @ApiOperation(value = "导出现有量")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(MesOnHandVO.class)
    public ResponseEntity<List<MesOnHandVO>> export(@PathVariable("organizationId") Long tenantId,
                                 ItfOnHandDTO dto,
                                 ExportParam exportParam,
                                 HttpServletResponse response) throws JCoException {
        return Results.success(itfSapOnHandService.onHandReport(tenantId, dto));
    }


}
