package com.ruike.wms.api.controller.v1;

import com.ruike.wms.domain.entity.WmsSoDeliveryContainerNum;
import com.ruike.wms.domain.repository.WmsSoDeliveryContainerNumRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static tarzan.config.SwaggerApiConfig.WMS_SO_DELIVERY_CONTAINER_NUM;

/**
 * 发货箱号表 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2020-12-09 13:44:15
 */
@RestController("wmsSoDeliveryContainerNumController.v1")
@RequestMapping("/v1/{organizationId}/wms-so-delivery-container-nums")
@Api(tags = WMS_SO_DELIVERY_CONTAINER_NUM)
public class WmsSoDeliveryContainerNumController extends BaseController {

    private final WmsSoDeliveryContainerNumRepository wmsSoDeliveryContainerNumRepository;

    public WmsSoDeliveryContainerNumController(WmsSoDeliveryContainerNumRepository wmsSoDeliveryContainerNumRepository) {
        this.wmsSoDeliveryContainerNumRepository = wmsSoDeliveryContainerNumRepository;
    }

    @ApiOperation(value = "发货箱号表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsSoDeliveryContainerNum>> list(WmsSoDeliveryContainerNum wmsSoDeliveryContainerNum, @ApiIgnore @SortDefault(value = WmsSoDeliveryContainerNum.FIELD_TENANT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsSoDeliveryContainerNum> list = wmsSoDeliveryContainerNumRepository.pageAndSort(pageRequest, wmsSoDeliveryContainerNum);
        return Results.success(list);
    }

}
