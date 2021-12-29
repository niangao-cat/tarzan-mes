package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.*;
import com.ruike.itf.app.service.ItfFirstProcessIfaceService;
import com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 首序接口
 *
 * @author sanfeng.zhang@hand-china.com 2021/10/15 10:35
 */
@RestController("itfFirstProcessIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-first-process-ifaces")
public class ItfFirstProcessIfaceController {

    @Autowired
    private ItfFirstProcessIfaceService itfFirstProcessIfaceService;

    @ApiOperation(value = "首序-进站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/in-site")
    public ResponseEntity<ItfFirstProcessIfaceVO> inSiteInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody ItfFirstProcessIfaceDTO request) {
        ItfFirstProcessIfaceVO firstProcessIfaceVO = new ItfFirstProcessIfaceVO();
        try {
            firstProcessIfaceVO = itfFirstProcessIfaceService.inSiteInvoke(tenantId, request);
        } catch (Exception ex){
            String message = ex.getMessage();
            firstProcessIfaceVO.setResult(false);
            firstProcessIfaceVO.setErrorDescription(message);
        } finally {
            return Results.success(firstProcessIfaceVO);
        }
    }

    @ApiOperation(value = "首序-投料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/release")
    public ResponseEntity<ItfFirstProcessIfaceVO> releaseInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody ItfFirstProcessIfaceDTO2 request) {
        ItfFirstProcessIfaceVO firstProcessIfaceVO = new ItfFirstProcessIfaceVO();
        try {
            firstProcessIfaceVO = itfFirstProcessIfaceService.releaseInvoke(tenantId, request);
        } catch (Exception ex){
            String message = ex.getMessage();
            firstProcessIfaceVO.setResult(false);
            firstProcessIfaceVO.setErrorDescription(message);
        } finally {
            return Results.success(firstProcessIfaceVO);
        }
    }

    @ApiOperation(value = "首序-出站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/out-site")
    public ResponseEntity<?> outSiteInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody ItfFirstProcessIfaceDTO3 request) {
        ItfFirstProcessIfaceVO firstProcessIfaceVO = new ItfFirstProcessIfaceVO();
        try {
            firstProcessIfaceVO = itfFirstProcessIfaceService.outSiteInvoke(tenantId, request);
        } catch (Exception ex){
            String message = ex.getMessage();
            firstProcessIfaceVO.setResult(false);
            firstProcessIfaceVO.setErrorDescription(message);
        } finally {
            return Results.success(firstProcessIfaceVO);
        }
    }

}
