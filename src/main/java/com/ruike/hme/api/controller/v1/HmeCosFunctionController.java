package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosFunctionHeadDTO;
import com.ruike.hme.api.dto.HmeFunctionExportDTO;
import com.ruike.hme.api.dto.HmeFunctionReportDTO;
import com.ruike.hme.app.service.HmeCosFunctionService;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.repository.HmeCosFunctionRepository;
import com.ruike.wms.api.dto.WmsStocktakeDetailExportDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 芯片性能表 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-08-07 15:08:16
 */
@RestController("hmeCosFunctionController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-functions")
@Api(tags = SwaggerApiConfig.HME_COS_FUNCTION)
@Slf4j
public class HmeCosFunctionController extends BaseController {

    @Autowired
    private HmeCosFunctionService hmeCosFunctionService;

    @Autowired
    private HmeCosFunctionRepository hmeCosFunctionRepository;


    @ApiOperation(value = "查询芯片性能头数据")
    @GetMapping(value = "/cosfunction/headquery", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeCosFunctionHeadDTO>> cosFunctionHeadQuery(@PathVariable("organizationId") Long tenantId,
                                                                            HmeCosFunctionHeadDTO dto,
                                                                            PageRequest pageRequest) {
        log.info("<====HmeCosFunctionController-cosFunctionHeadQuery:{},{}", tenantId, dto.getMaterialCode());
        Page<HmeCosFunctionHeadDTO> hmeCosContainers = hmeCosFunctionService.cosFunctionHeadQuery(tenantId, dto, pageRequest);
        return Results.success(hmeCosContainers);
    }


    @ApiOperation(value = "查询芯片性能数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/cosFunction/query")
    public ResponseEntity<Page<HmeCosFunction>> cosFunctionQuery(@PathVariable("organizationId") Long tenantId,
                                                                 String loadSequence,
                                                                 PageRequest pageRequest) {
        log.info("<====HmeCosFunctionController-cosFunctionQuery:{},{}", tenantId, loadSequence);
        Page<HmeCosFunction> hmeCosFunctions = hmeCosFunctionService.cosFunctionQuery(tenantId, loadSequence, pageRequest);
        return Results.success(hmeCosFunctions);
    }

    @ApiOperation(value = "芯片性能报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/mes-report/function")
    public ResponseEntity<Page<HmeFunctionReportDTO>> cosFunctionReport(@PathVariable("organizationId") Long tenantId,
                                                                        HmeCosFunctionHeadDTO dto,
                                                                        PageRequest pageRequest) {
        log.info("<====HmeCosFunctionController-cosFunctionReport:{},{}", tenantId, dto);
        Page<HmeFunctionReportDTO> hmeCosFunctions = hmeCosFunctionService.cosFunctionReport(tenantId, dto, pageRequest);
        return Results.success(hmeCosFunctions);
    }

    @ApiOperation(value = "性能导出")
    @GetMapping(value = "/mes-report/function/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeFunctionExportDTO.class)
    public ResponseEntity<List<HmeFunctionExportDTO>> exportDetail(@PathVariable("organizationId") Long tenantId,
                                                                   ExportParam exportParam,
                                                                   HttpServletResponse response,
                                                                   HmeCosFunctionHeadDTO dto) {
        return Results.success(hmeCosFunctionService.exportDetail(tenantId, exportParam, dto));
    }

}
