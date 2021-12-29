package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.*;
import com.ruike.itf.app.service.ItfTimeProcessIfaceService;
import com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO;
import com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 时效接口
 *
 * @author sanfeng.zhang@hand-china.com 2021/10/15 10:35
 */
@RestController("itfTimeProcessIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-time-process-ifaces")
public class ItfTimeProcessIfaceController {

    @Autowired
    private ItfTimeProcessIfaceService itfTimeProcessIfaceService;

    @ApiOperation(value = "时效-进站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/in-site")
    public ResponseEntity<ItfProcessReturnIfaceVO> inSiteInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody ItfTimeProcessIfaceDTO request) {
        ItfProcessReturnIfaceVO returnIfaceVO = new ItfProcessReturnIfaceVO();
        try {
            returnIfaceVO = itfTimeProcessIfaceService.inSiteInvoke(tenantId, request);
        } catch (Exception ex){
            String message = ex.getMessage();
            returnIfaceVO.setResult(false);
            returnIfaceVO.setErrorDescription(message);
        } finally {
            return Results.success(returnIfaceVO);
        }
    }

    @ApiOperation(value = "时效/时效返修-进站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/common-in-site")
    public ResponseEntity<ItfProcessReturnIfaceVO> commonInSiteInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody ItfTimeProcessIfaceDTO request) {
        ItfProcessReturnIfaceVO returnIfaceVO = new ItfProcessReturnIfaceVO();
        try {
            returnIfaceVO = itfTimeProcessIfaceService.commonInSiteInvoke(tenantId, request);
        } catch (Exception ex){
            String message = ex.getMessage();
            returnIfaceVO.setResult(false);
            returnIfaceVO.setErrorDescription(message);
        } finally {
            return Results.success(returnIfaceVO);
        }
    }

    @ApiOperation(value = "时效-出站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/out-site")
    public ResponseEntity<?> outSiteInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody ItfTimeProcessIfaceDTO2 request) {
        ItfProcessReturnIfaceVO firstProcessIfaceVO = new ItfProcessReturnIfaceVO();
        try {
            firstProcessIfaceVO = itfTimeProcessIfaceService.outSiteInvoke(tenantId, request);
        } catch (Exception ex){
            String message = ex.getMessage();
            firstProcessIfaceVO.setResult(false);
            firstProcessIfaceVO.setErrorDescription(message);
        } finally {
            return Results.success(firstProcessIfaceVO);
        }
    }
}
