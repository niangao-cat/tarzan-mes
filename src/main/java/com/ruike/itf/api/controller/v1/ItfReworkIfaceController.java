package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.*;
import com.ruike.itf.app.service.ItfReworkIfaceService;
import com.ruike.itf.domain.vo.ItfReworkIfaceVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 返修设备接口管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-10-26 10:08:12
 */
@RestController("itfReworkIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-rework-ifaces")
@Slf4j
public class ItfReworkIfaceController {

    @Autowired
    private ItfReworkIfaceService itfReworkIfaceService;

    @ApiOperation(value = "返修进站接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/in-site")
    public ResponseEntity<ItfReworkIfaceVO> inSiteInvoke(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody ItfReworkIfaceDTO dto){
        ItfReworkIfaceVO itfReworkIfaceVO = new ItfReworkIfaceVO();
        try {
            itfReworkIfaceVO = itfReworkIfaceService.inSiteInvoke(tenantId, dto);
        }catch (Exception ex){
            String message = ex.getMessage();
            itfReworkIfaceVO.setResult(false);
            itfReworkIfaceVO.setErrorDescription(message);
        }finally {
            return Results.success(itfReworkIfaceVO);
        }
    }

    @ApiOperation(value = "返修/单件公用进站接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/common-in-site")
    public ResponseEntity<ItfReworkIfaceVO> commonInSiteInvoke(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody ItfReworkIfaceDTO dto){
        ItfReworkIfaceVO itfReworkIfaceVO = new ItfReworkIfaceVO();
        try {
            itfReworkIfaceVO = itfReworkIfaceService.commonInSiteInvoke(tenantId, dto);
        }catch (Exception ex){
            String message = ex.getMessage();
            itfReworkIfaceVO.setResult(false);
            itfReworkIfaceVO.setErrorDescription(message);
        }finally {
            return Results.success(itfReworkIfaceVO);
        }
    }

    @ApiOperation(value = "返修投料接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release")
    public ResponseEntity<ItfReworkIfaceVO> releaseInvoke(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody ItfReworkIfaceDTO2 dto){
        ItfReworkIfaceVO itfReworkIfaceVO = new ItfReworkIfaceVO();
        try {
            itfReworkIfaceVO = itfReworkIfaceService.releaseInvoke(tenantId, dto);
        }catch (Exception ex){
            String message = ex.getMessage();
            itfReworkIfaceVO.setResult(false);
            itfReworkIfaceVO.setErrorDescription(message);
        }finally {
            return Results.success(itfReworkIfaceVO);
        }
    }

    @ApiOperation(value = "返修出站接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/out-site")
    public ResponseEntity<ItfReworkIfaceVO> outSiteInvoke(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody ItfReworkIfaceDTO3 dto){
        ItfReworkIfaceVO itfReworkIfaceVO = new ItfReworkIfaceVO();
        try {
            itfReworkIfaceVO = itfReworkIfaceService.outSiteInvoke(tenantId, dto);
        }catch (Exception ex){
            String message = ex.getMessage();
            itfReworkIfaceVO.setResult(false);
            itfReworkIfaceVO.setErrorDescription(message);
        }finally {
            return Results.success(itfReworkIfaceVO);
        }
    }
}
