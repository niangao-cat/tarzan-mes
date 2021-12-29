package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsMaterialExchangeService;
import com.ruike.wms.domain.vo.WmsMaterialExchangeVO;
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

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;

/**
 * 库存调拨发出执行 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-05-18 10:29
 */
@RestController("wmsMaterialExchangeController.v1")
@RequestMapping("/v1/{organizationId}/material-exchange-query")
@Api(tags = SwaggerApiConfig.WMS_MATERIAL_EXCHANGE)
public class WmsMaterialExchangeController extends BaseController {

    @Autowired
    private WmsMaterialExchangeService wmsMaterialExchangeService;

    @ApiOperation(value = "单据查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list/ui", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<WmsMaterialExchangeDocDTO>> listDocForUi(@ApiIgnore PageRequest pageRequest,
                                                                      @PathVariable("organizationId") Long tenantId,
                                                                      WmsMaterialExchangeParamDTO dto) {
        return Results.success(wmsMaterialExchangeService.listDocForUi(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "单据行查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/line/list/ui", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<WmsMaterialExchangeDocLineDTO>> listDocLineForUi(@ApiIgnore PageRequest pageRequest,
                                                                                @PathVariable("organizationId") Long tenantId,
                                                                                String instructionDocId) {
        return Results.success(wmsMaterialExchangeService.listDocLineForUi(tenantId, instructionDocId, pageRequest));
    }

    @ApiOperation(value = "库存调拨")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/line-stock-transfer", produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsMaterialExchangeVO> lineStockTransfer(@PathVariable("organizationId") Long tenantId,
                                                                                @RequestBody WmsMaterialExchangeVO wmsMaterialExchangeVO) {
        return Results.success(wmsMaterialExchangeService.lineStockTransfer(tenantId, wmsMaterialExchangeVO));
    }
}
