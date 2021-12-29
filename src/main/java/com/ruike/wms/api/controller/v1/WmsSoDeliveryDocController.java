package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsSoDeliveryQueryDTO;
import com.ruike.wms.api.dto.WmsSoDeliverySubmitDTO;
import com.ruike.wms.app.service.WmsSoDeliveryDocService;
import com.ruike.wms.domain.vo.WmsSoDeliveryDocVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseController;
import org.hzero.core.cache.ProcessCacheValue;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static tarzan.config.SwaggerApiConfig.WMS_SO_DELIVERY_DOC;

/**
 * <p>
 * 出货单单据 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 15:18
 */
@RestController("wmsSoDeliveryDocController.v1")
@RequestMapping("/v1/{organizationId}/wms-so-delivery-docs")
@Api(tags = WMS_SO_DELIVERY_DOC)
public class WmsSoDeliveryDocController extends BaseController {
    private final WmsSoDeliveryDocService wmsSoDeliveryDocService;

    public WmsSoDeliveryDocController(WmsSoDeliveryDocService wmsSoDeliveryDocService) {
        this.wmsSoDeliveryDocService = wmsSoDeliveryDocService;
    }

    @ApiOperation(value = "出货单单据 分页列表")
    @ProcessLovValue
    @ProcessCacheValue
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsSoDeliveryDocVO>> list(@PathVariable("organizationId") Long tenantId,
                                                         PageRequest pageRequest,
                                                         WmsSoDeliveryQueryDTO dto) {
        Page<WmsSoDeliveryDocVO> list = wmsSoDeliveryDocService.pageList(tenantId, pageRequest, dto);
        return Results.success(list);
    }

    @ApiOperation(value = "出货单单据 保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<WmsSoDeliveryDocVO> submit(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody WmsSoDeliverySubmitDTO dto) {
        this.validObject(dto);
        this.validList(dto.getLineList());
        WmsSoDeliveryDocVO vo = wmsSoDeliveryDocService.submit(tenantId, dto);
        return Results.success(vo);
    }

    @ApiOperation(value = "出货单单据 取消")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cancel")
    public ResponseEntity<WmsSoDeliveryDocVO> cancel(@PathVariable("organizationId") Long tenantId,
                                                     @RequestParam String instructionDocId) {
        WmsSoDeliveryDocVO vo = wmsSoDeliveryDocService.cancel(tenantId, instructionDocId);
        return Results.success(vo);
    }

    @ApiOperation(value = "出货单单据 下达")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release")
    public ResponseEntity<WmsSoDeliveryDocVO> release(@PathVariable("organizationId") Long tenantId,
                                                      @RequestParam String instructionDocId) {
        WmsSoDeliveryDocVO vo = wmsSoDeliveryDocService.release(tenantId, instructionDocId);
        return Results.success(vo);
    }

    @ApiOperation(value = "出货单单据 取消下达")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-cancel")
    public ResponseEntity<WmsSoDeliveryDocVO> releaseCancel(@PathVariable("organizationId") Long tenantId,
                                                            @RequestParam String instructionDocId) {
        WmsSoDeliveryDocVO vo = wmsSoDeliveryDocService.releaseCancel(tenantId, instructionDocId);
        return Results.success(vo);
    }

    @ApiOperation(value = "出货单单据 过账")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/confirm")
    public ResponseEntity<WmsSoDeliveryDocVO> confirm(@PathVariable("organizationId") Long tenantId,
                                                      @RequestParam String instructionDocId) {
        WmsSoDeliveryDocVO vo = wmsSoDeliveryDocService.confirm(tenantId, instructionDocId);
        return Results.success(vo);
    }
}
