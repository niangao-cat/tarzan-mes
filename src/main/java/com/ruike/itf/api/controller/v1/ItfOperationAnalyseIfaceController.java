package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfOperationAnalyseDTO;
import com.ruike.itf.app.service.ItfOperationAnalyseIfaceService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("itfOperationAnalyseIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-operation-analyse-ifaces")
@Slf4j
public class ItfOperationAnalyseIfaceController extends BaseController {


    private final ItfOperationAnalyseIfaceService itfOperationAnalyseIfaceService;

    @Autowired
    public ItfOperationAnalyseIfaceController(ItfOperationAnalyseIfaceService itfOperationAnalyseIfaceService) {
        this.itfOperationAnalyseIfaceService = itfOperationAnalyseIfaceService;
    }

    @ApiOperation(value = "条码不良信息查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/nc/query")
    public ResponseEntity<ItfOperationAnalyseDTO> ncQueryInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody ItfOperationAnalyseDTO.QueryDTO dto) {
        log.info("<====ItfOperationAnalyseIfaceController-ncQueryInvoke.start:条码{}", dto.getSn() != null?dto.getSn().get(0):"");
        long startDate = System.currentTimeMillis();
        ItfOperationAnalyseDTO list = itfOperationAnalyseIfaceService.invokeA(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("<====ItfOperationAnalyseIfaceController-ncQueryInvoke.end:条码{}。总耗时：{}毫秒", dto.getSn() != null?dto.getSn().get(0):"", (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "SN投料查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/issue/query")
    public ResponseEntity<ItfOperationAnalyseDTO.ReturnDTO1> issueQueryInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody ItfOperationAnalyseDTO.QueryDTO dto){
        log.info("<====ItfOperationAnalyseIfaceController-issueQueryInvoke.start:条码{}", dto.getSn() != null?dto.getSn().get(0):"");
        long startDate = System.currentTimeMillis();
        ItfOperationAnalyseDTO.ReturnDTO1 list = itfOperationAnalyseIfaceService.invokeB(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("<====ItfOperationAnalyseIfaceController-issueQueryInvoke.end:条码{}。总耗时：{}毫秒", dto.getSn() != null?dto.getSn().get(0):"", (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "工序工艺质量查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/eoJobData/query")
    public ResponseEntity<ItfOperationAnalyseDTO.ReturnDTO2> eoJobDataQueryInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody ItfOperationAnalyseDTO.QueryDTO dto){
        log.info("<====ItfOperationAnalyseIfaceController-eoJobDataQueryInvoke.start:条码{}", dto.getSn() != null?dto.getSn().get(0):"");
        long startDate = System.currentTimeMillis();
        ItfOperationAnalyseDTO.ReturnDTO2 list = itfOperationAnalyseIfaceService.invokeC(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("<====ItfOperationAnalyseIfaceController-eoJobDataQueryInvoke.end:条码{}。总耗时：{}毫秒", dto.getSn() != null?dto.getSn().get(0):"", (endDate - startDate));
        return Results.success(list);
    }

    @ApiOperation(value = "良率统计")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/yield")
    public ResponseEntity<ItfOperationAnalyseDTO.AcceptedRate> yieldInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody ItfOperationAnalyseDTO.QueryDTO dto){
        log.info("<====ItfOperationAnalyseIfaceController-yieldInvoke.start:条码{}", dto.getSn() != null?dto.getSn().get(0):"");
        long startDate = System.currentTimeMillis();
        ItfOperationAnalyseDTO.AcceptedRate list = itfOperationAnalyseIfaceService.invokeD(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("<====ItfOperationAnalyseIfaceController-yieldInvoke.end:条码{}。总耗时：{}毫秒", dto.getSn() != null?dto.getSn().get(0):"", (endDate - startDate));
        return Results.success(list);
    }
}
