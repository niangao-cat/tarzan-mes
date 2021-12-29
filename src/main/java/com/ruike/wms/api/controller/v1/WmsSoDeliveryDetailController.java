package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsSoDeliveryDetailQueryDTO;
import com.ruike.wms.app.service.WmsSoDeliveryDetailService;
import com.ruike.wms.domain.repository.WmsSoDeliveryDetailRepository;
import com.ruike.wms.domain.vo.WmsSoDeliveryDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_SO_DELIVERY_DETAIL;

/**
 * <p>
 * 出货单明细 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 11:18
 */
@RestController("wmsSoDeliveryDetailController.v1")
@RequestMapping("/v1/{organizationId}/wms-so-delivery-details")
@Api(tags = WMS_SO_DELIVERY_DETAIL)
public class WmsSoDeliveryDetailController extends BaseController {
    private final WmsSoDeliveryDetailRepository wmsSoDeliveryLineRepository;
    private final WmsSoDeliveryDetailService wmsSoDeliveryDetailService;

    public WmsSoDeliveryDetailController(WmsSoDeliveryDetailRepository wmsSoDeliveryLineRepository,
                                         WmsSoDeliveryDetailService wmsSoDeliveryDetailService) {
        this.wmsSoDeliveryLineRepository = wmsSoDeliveryLineRepository;
        this.wmsSoDeliveryDetailService = wmsSoDeliveryDetailService;
    }

    @ApiOperation(value = "出货单明细 分页列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsSoDeliveryDetailVO>> list(@PathVariable("organizationId") Long tenantId,
                                                            WmsSoDeliveryDetailQueryDTO dto,
                                                            PageRequest pageRequest) {
        this.validObject(dto);
        if (StringUtils.isBlank(dto.getInstructionIdList())) {
            return Results.success();
        }
        List<String> idList = Arrays.asList(dto.getInstructionIdList().split(","));
        Page<WmsSoDeliveryDetailVO> list = wmsSoDeliveryLineRepository.selectListByCondition(tenantId, idList, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "出货单明细 删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> delete(@PathVariable("organizationId") Long tenantId,
                                    @RequestBody List<WmsSoDeliveryDetailVO> voList,
                                    @RequestParam Double lineActualQty,
                                    @RequestParam Double lineDemandQty,
                                    @RequestParam String instructionDocId) {
        wmsSoDeliveryDetailService.delete(tenantId, voList, lineActualQty, lineDemandQty, instructionDocId);
        return Results.success();
    }
}
