package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeWkcShiftAttrService;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeWkcShiftAttr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import tarzan.config.SwaggerApiConfig;

/**
 * 班组交接事项记录表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-07-31 11:00:48
 */
@RestController("hmeWkcShiftAttrController.v1")
@RequestMapping("/v1/{organizationId}/hme-wkc-shift-attrs")
@Api(tags = SwaggerApiConfig.HME_WKC_SHIFT_ATTR)
public class HmeWkcShiftAttrController extends BaseController {

    @Autowired
    private HmeWkcShiftAttrService hmeQualifications;

    @ApiOperation(value = "创建或者更新交接事项")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/createOrUpdate"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeWkcShiftAttr> createOrUpdate(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody HmeWkcShiftAttr dto){
        return Results.success(hmeQualifications.createOrUpdate(tenantId, dto));
    }

}
