package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEquipmentStocktakePdaDTO;
import com.ruike.hme.api.dto.HmeEquipmentStocktakePdaDTO2;
import com.ruike.hme.app.service.HmeEquipmentStocktakePdaService;
import com.ruike.hme.domain.vo.HmeEquipmentStocktakePdaVO;
import com.ruike.hme.domain.vo.HmeEquipmentStocktakePdaVO2;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * HmeEquipmentStocktakePdaController
 * 设备盘点PDA
 * @author: chaonan.hu@hand-china.com 2021/04/01 15:33:21
 **/
@RestController("hmeEquipmentStocktakePdaController.v1")
@RequestMapping("/v1/{organizationId}/hme-equipment-stocktake-pda")
public class HmeEquipmentStocktakePdaController extends BaseController {

    @Autowired
    private HmeEquipmentStocktakePdaService hmeEquipmentStocktakePdaService;

    @ApiOperation(value = "独立值集查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/lov-query/{lovCode}")
    public ResponseEntity<List<LovValueDTO>> equipmentStatusLovQuery(@PathVariable("organizationId") Long tenantId,
                                                                     @PathVariable("lovCode") String lovCode) {
        List<LovValueDTO> list = hmeEquipmentStocktakePdaService.equipmentStatusLovQuery(tenantId, lovCode);
        return Results.success(list);
    }

    @ApiOperation(value = "扫描单据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scan-stocktake/{stocktakeNum}")
    public ResponseEntity<HmeEquipmentStocktakePdaVO> scanStocktakeNum(@PathVariable("organizationId") Long tenantId,
                                                                       @PathVariable("stocktakeNum") String stocktakeNum) {
        HmeEquipmentStocktakePdaVO result = hmeEquipmentStocktakePdaService.scanStocktakeNum(tenantId, stocktakeNum);
        return Results.success(result);
    }

    @ApiOperation(value = "扫描设备")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scan-equipment")
    public ResponseEntity<HmeEquipmentStocktakePdaVO2> scanEquipment(@PathVariable("organizationId") Long tenantId,
                                                                     HmeEquipmentStocktakePdaDTO dto) {
        HmeEquipmentStocktakePdaVO2 result = hmeEquipmentStocktakePdaService.scanEquipment(tenantId, dto);
        return Results.success(result);
    }

    @ApiOperation(value = "提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/submit", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeEquipmentStocktakePdaDTO2> submit(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody HmeEquipmentStocktakePdaDTO2 dto) {
        HmeEquipmentStocktakePdaDTO2 result = hmeEquipmentStocktakePdaService.submit(tenantId, dto);
        return Results.success(result);
    }
}
