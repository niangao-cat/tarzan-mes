package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeSnReplaceDTO;
import com.ruike.hme.api.dto.HmeSnReplaceDTO2;
import com.ruike.hme.app.service.HmeSnReplaceService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * HmeSnReplaceController
 * @description: SN替换
 * @author: chaonan.hu@hand-china.com 2020-11-03 22:29:34
 **/
@RestController("hmeSnReplaceController.v1")
@RequestMapping("/v1/{organizationId}/hme-sn-replace")
public class HmeSnReplaceController extends BaseController {

    @Autowired
    private HmeSnReplaceService hmeSnReplaceService;

    @ApiOperation(value = "SN替换")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/sn-replace", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeSnReplaceDTO>> snReplace(@PathVariable("organizationId")Long tenantId,
                                                           @RequestBody List<HmeSnReplaceDTO> dtoList){
        return Results.success(hmeSnReplaceService.snReplace(tenantId, dtoList));
    }

    @ApiOperation(value = "SN替换以及用户ID")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/cms-sn-replace", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeSnReplaceDTO2> cmsSnReplace(@PathVariable("organizationId")Long tenantId,
                                                         @RequestBody HmeSnReplaceDTO2 dto2){
        return Results.success(hmeSnReplaceService.cmsSnReplace(tenantId, dto2));
    }
}
