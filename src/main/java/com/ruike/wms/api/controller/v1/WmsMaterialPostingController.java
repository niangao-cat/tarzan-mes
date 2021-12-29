package com.ruike.wms.api.controller.v1;

import java.util.List;

import com.ruike.wms.domain.vo.WmsDeliveryPoRelVo;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ruike.wms.app.service.WmsMaterialPostingService;
import com.ruike.wms.domain.vo.WmsInstructionLineVO;
import com.ruike.wms.domain.vo.WmsMaterialLotLineVO;
import com.ruike.wms.domain.vo.WmsMaterialPostingVO;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

/**
 * WmsMaterialPostingController
 *
 * @author liyuan.lv@hand-china.com 2020/06/14 19:42
 */
@RestController("wmsMaterialPostingController.v1")
@RequestMapping("/v1/{organizationId}/wms-material-posting")
@Api(tags = SwaggerApiConfig.WMS_MATERIAL_POSTING)
@Slf4j
public class WmsMaterialPostingController extends BaseController {

    @Autowired
    private WmsMaterialPostingService service;

    @ApiOperation(value = "物料过账查询")
    @GetMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsInstructionLineVO>> uiQuery(@PathVariable("organizationId") Long tenantId,
                                                              WmsMaterialPostingVO dto,
                                                              @ApiIgnore PageRequest pageRequest) {
        return Results.success(service.uiQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "物料过账明细查询")
    @GetMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsMaterialLotLineVO>> detailUiQuery(@PathVariable("organizationId") Long tenantId,
                                                                    String instructionId,
                                                                    @ApiIgnore PageRequest pageRequest) {
        return Results.success(service.detailUiQuery(tenantId, instructionId, pageRequest));
    }

    @ApiOperation(value = "采购订单列表查询")
    @GetMapping("/po")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsDeliveryPoRelVo>> poList(@PathVariable("organizationId") Long tenantId,
                                                           @RequestParam String deliveryId,
                                                           @ApiIgnore PageRequest pageRequest) {
        return Results.success(service.poListQuery(tenantId, deliveryId, pageRequest));
    }

    @ApiOperation(value = "物料过账")
    @PostMapping(value = "/execute-posting", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsInstructionLineVO>> executePosting(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody List<WmsInstructionLineVO> dtoList) {
        return Results.success(service.executePosting(tenantId, dtoList));
    }

}
