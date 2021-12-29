package com.ruike.wms.api.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;
import com.ruike.wms.api.dto.WmsMaterialLotDTO3;
import com.ruike.wms.api.dto.WmsMaterialLotDTO4;
import com.ruike.wms.app.service.WmsMaterialLotPdaService;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;

/**
 * PDA条码管理
 *
 * @author jiangling.zheng@hand-china.com 2020-04-03 11:15:27
 */

@RestController("pdaMaterialLotController.v1")
@RequestMapping("/v1/{organizationId}/pda-material-lot")
@Api(tags = SwaggerApiConfig.WMS_PDA_MATERIAL_LOT)
public class WmsMaterialLotPdaController extends BaseController {

    @Autowired
    private WmsMaterialLotPdaService mtMaterialLotPdaService;

    @ApiOperation("PDA条码查询")
    @GetMapping(value = {"/barCodeQuery"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsMaterialLotDTO3>> barCodeQuery(@PathVariable("organizationId") Long tenantId,
                                                                 WmsMaterialLotDTO4 dto, @ApiIgnore PageRequest pageRequest) {
        return Results.success(mtMaterialLotPdaService.selectBarCodeCondition(tenantId, dto, pageRequest));
    }
}
