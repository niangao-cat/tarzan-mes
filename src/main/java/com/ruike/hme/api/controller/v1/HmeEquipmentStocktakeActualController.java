package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeActualModifyCommand;
import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeActualRepresentation;
import com.ruike.hme.app.service.HmeEquipmentStocktakeActualService;
import com.ruike.hme.domain.repository.HmeEquipmentStocktakeActualRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

/**
 * 设备盘点实际 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2021-03-31 09:32:46
 */
@RestController("hmeEquipmentStocktakeActualController.v1")
@RequestMapping("/v1/{organizationId}/hme-equipment-stocktake-actuals")
@Api(tags = SwaggerApiConfig.HME_EQUIPMENT_STOCKTAKE_ACTUAL)
public class HmeEquipmentStocktakeActualController extends BaseController {

    private final HmeEquipmentStocktakeActualRepository hmeEquipmentStocktakeActualRepository;
    private final HmeEquipmentStocktakeActualService stocktakeActualService;

    public HmeEquipmentStocktakeActualController(HmeEquipmentStocktakeActualRepository hmeEquipmentStocktakeActualRepository, HmeEquipmentStocktakeActualService stocktakeActualService) {
        this.hmeEquipmentStocktakeActualRepository = hmeEquipmentStocktakeActualRepository;
        this.stocktakeActualService = stocktakeActualService;
    }

    @ApiOperation(value = "设备盘点实际 分页列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeEquipmentStocktakeActualRepresentation>> list(@PathVariable("organizationId") Long tenantId,
                                                                                String stocktakeId,
                                                                                @ApiIgnore PageRequest pageRequest) {
        Page<HmeEquipmentStocktakeActualRepresentation> list = hmeEquipmentStocktakeActualRepository.page(stocktakeId, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "设备盘点实际 更新")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<Void> update(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody HmeEquipmentStocktakeActualModifyCommand command) {
        command.setTenantId(tenantId);
        validObject(command);
        stocktakeActualService.update(command);
        return Results.success();
    }

}
