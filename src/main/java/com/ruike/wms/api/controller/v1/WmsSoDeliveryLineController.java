package com.ruike.wms.api.controller.v1;

import com.ruike.wms.app.service.WmsSoDeliveryLineService;
import com.ruike.wms.domain.repository.WmsSoDeliveryLineRepository;
import com.ruike.wms.domain.vo.WmsSoDeliveryLineVO;
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

import static tarzan.config.SwaggerApiConfig.WMS_SO_DELIVERY_LINE;

/**
 * <p>
 * 出货单行 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 15:18
 */
@RestController("wmsSoDeliveryLineController.v1")
@RequestMapping("/v1/{organizationId}/wms-so-delivery-lines")
@Api(tags = WMS_SO_DELIVERY_LINE)
public class WmsSoDeliveryLineController extends BaseController {
    private final WmsSoDeliveryLineRepository wmsSoDeliveryLineRepository;
    private final WmsSoDeliveryLineService wmsSoDeliveryLineService;

    public WmsSoDeliveryLineController(WmsSoDeliveryLineRepository wmsSoDeliveryLineRepository, WmsSoDeliveryLineService wmsSoDeliveryLineService) {
        this.wmsSoDeliveryLineRepository = wmsSoDeliveryLineRepository;
        this.wmsSoDeliveryLineService = wmsSoDeliveryLineService;
    }

    @ApiOperation(value = "出货单行 分页列表")
    @ProcessLovValue
    @ProcessCacheValue
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsSoDeliveryLineVO>> list(@PathVariable("organizationId") Long tenantId,
                                                          PageRequest pageRequest,
                                                          @RequestParam String instructionDocId) {
        Page<WmsSoDeliveryLineVO> list = wmsSoDeliveryLineRepository.listByDocId(tenantId, pageRequest, instructionDocId);
        return Results.success(list);
    }

    @ApiOperation(value = "出货单行 取消")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("organizationId") Long tenantId,
                                       String instructionId) {
        wmsSoDeliveryLineService.cancel(tenantId, instructionId);
        return Results.success();
    }
}
