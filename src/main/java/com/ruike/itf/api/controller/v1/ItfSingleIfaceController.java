package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfSingleIfaceDTO;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO2;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO3;
import com.ruike.itf.app.service.ItfSingleIfaceService;
import com.ruike.itf.domain.vo.ItfSingleIfaceVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 单件设备接口管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-09-27 10:08:12
 */
@RestController("itfSingleIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-single-ifaces")
@Slf4j
public class ItfSingleIfaceController {

    @Autowired
    private ItfSingleIfaceService itfSingleIfaceService;

    @ApiOperation(value = "单件进站接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/in-site")
    public ResponseEntity<ItfSingleIfaceVO> singleInSite(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody ItfSingleIfaceDTO dto){
        ItfSingleIfaceVO itfSingleIfaceVO = new ItfSingleIfaceVO();
        try {
            itfSingleIfaceVO = itfSingleIfaceService.singleInSite(tenantId, dto);
        }catch (Exception ex){
            String message = ex.getMessage();
            itfSingleIfaceVO.setResult(false);
            itfSingleIfaceVO.setErrorDescription(message);
        }finally {
            return Results.success(itfSingleIfaceVO);
        }
    }

    @ApiOperation(value = "单件投料接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release")
    public ResponseEntity<ItfSingleIfaceVO> singleRelease(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody ItfSingleIfaceDTO2 dto){
        ItfSingleIfaceVO itfSingleIfaceVO = new ItfSingleIfaceVO();
        try {
            itfSingleIfaceVO = itfSingleIfaceService.singleRelease(tenantId, dto);
        }catch (Exception ex){
            String message = ex.getMessage();
            itfSingleIfaceVO.setResult(false);
            itfSingleIfaceVO.setErrorDescription(message);
        }finally {
            return Results.success(itfSingleIfaceVO);
        }
    }

    @ApiOperation(value = "单件出站接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/site-out")
    public ResponseEntity<ItfSingleIfaceVO> singleSiteOut(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody ItfSingleIfaceDTO3 dto){
        ItfSingleIfaceVO itfSingleIfaceVO = new ItfSingleIfaceVO();
        try {
            itfSingleIfaceVO = itfSingleIfaceService.singleSiteOut(tenantId, dto);
        }catch (Exception ex){
            String message = ex.getMessage();
            itfSingleIfaceVO.setResult(false);
            itfSingleIfaceVO.setErrorDescription(message);
        }finally {
            return Results.success(itfSingleIfaceVO);
        }
    }
}
