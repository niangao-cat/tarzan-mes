package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsMaterialWasteExchangeService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_MATERIAL_WASTE_EXCHANGE;

/**
 * @Description 料废调换发出执行
 * @Author tong.li
 * @Date 2020/5/7 14:46
 * @Version 1.0
 */
@RestController("wmsMaterialWasteExchangeController.v1")
@RequestMapping("/v1/{organizationId}/wms-material-waste-exchange")
@Api(tags = WMS_MATERIAL_WASTE_EXCHANGE)
@Slf4j
public class WmsMaterialWasteExchangeController extends BaseController {
    @Autowired
    private WmsMaterialWasteExchangeService materialWasteExchangeService;

    @ApiOperation(value = "实物条码扫码查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/material-lot", produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsMaterialWasteExchangeDTO2> containerOrMaterialLotQuery(@PathVariable("organizationId") Long tenantId,
                                                                                    String barCode) {
        return Results.success(materialWasteExchangeService.containerOrMaterialLotQuery(tenantId, barCode));
    }

    @ApiOperation(value = "执行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/execute", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> execute(@PathVariable("organizationId") Long tenantId, @RequestBody WmsMaterialWasteExchangeDTO6 dto) {
        materialWasteExchangeService.execute(tenantId, dto.getLineList());
        return Results.success("执行成功！");
    }

}
