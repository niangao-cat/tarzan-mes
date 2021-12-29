package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfReworkTimeIfaceDTO;
import com.ruike.itf.api.dto.ItfReworkTimeIfaceDTO2;
import com.ruike.itf.api.dto.ItfTimeProcessIfaceDTO;
import com.ruike.itf.api.dto.ItfTimeProcessIfaceDTO2;
import com.ruike.itf.app.service.ItfReworkTimeIfaceService;
import com.ruike.itf.app.service.ItfTimeProcessIfaceService;
import com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 返修时效接口
 *
 * @author sanfeng.zhang@hand-china.com 2021/10/15 10:35
 */
@RestController("itfReworkTimeIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-rework-time-ifaces")
public class ItfReworkTimeIfaceController {

    @Autowired
    private ItfReworkTimeIfaceService itfReworkTimeIfaceService;

    @ApiOperation(value = "返修时效-进站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/in-site")
    public ResponseEntity<ItfProcessReturnIfaceVO> inSiteInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody ItfReworkTimeIfaceDTO request) {
        ItfProcessReturnIfaceVO returnIfaceVO = new ItfProcessReturnIfaceVO();
        try {
            returnIfaceVO = itfReworkTimeIfaceService.inSiteInvoke(tenantId, request);
        } catch (Exception ex){
            String message = ex.getMessage();
            returnIfaceVO.setResult(false);
            returnIfaceVO.setErrorDescription(message);
        } finally {
            return Results.success(returnIfaceVO);
        }
    }

    @ApiOperation(value = "返修时效-出站")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/out-site")
    public ResponseEntity<?> outSiteInvoke(@PathVariable("organizationId") Long tenantId, @RequestBody ItfReworkTimeIfaceDTO2 request) {
        ItfProcessReturnIfaceVO firstProcessIfaceVO = new ItfProcessReturnIfaceVO();
        try {
            firstProcessIfaceVO = itfReworkTimeIfaceService.outSiteInvoke(tenantId, request);
        } catch (Exception ex){
            String message = ex.getMessage();
            firstProcessIfaceVO.setResult(false);
            firstProcessIfaceVO.setErrorDescription(message);
        } finally {
            return Results.success(firstProcessIfaceVO);
        }
    }
}
