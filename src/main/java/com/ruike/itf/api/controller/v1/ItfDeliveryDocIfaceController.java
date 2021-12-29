package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfDeliveryDocReturnDTO;
import com.ruike.itf.app.service.ItfDeliveryDocIfaceService;
import com.ruike.itf.domain.vo.ItfDeliveryDocAndLineIfaceVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 送货单接口 管理 API
 *
 * @author yapeng.yao@hand-china.com 2020-09-04 16:29:21
 */
@RestController("itfDeliveryDocIfaceSiteController.v1")
@RequestMapping("/v1/itf-delivery-doc-ifaces")
public class ItfDeliveryDocIfaceController extends BaseController {

    @Autowired
    private ItfDeliveryDocIfaceService itfDeliveryDocIfaceService;

    @ApiOperation(value = "送货单接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/invoke")
    public ResponseEntity<?> invoke(@RequestBody List<ItfDeliveryDocAndLineIfaceVO> voList) throws Exception {
        List<ItfDeliveryDocReturnDTO> itfDeliveryDocReturnDTOList = itfDeliveryDocIfaceService.invoke(voList);
        return Results.success(itfDeliveryDocReturnDTOList);
    }

}
