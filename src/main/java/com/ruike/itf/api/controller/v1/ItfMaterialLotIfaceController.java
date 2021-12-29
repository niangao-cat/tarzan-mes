package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfMaterialLotReturnDTO;
import com.ruike.itf.app.service.ItfMaterialLotIfaceService;
import com.ruike.itf.domain.entity.ItfMaterialLotIface;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
 * 物料批，提供实物管理简易装载物料的结构，记录包括物料、位置、状态、数量、所有者、预留对象等数据 管理 API
 *
 * @author yapeng.yao@hand-china.com 2020-09-01 09:32:35
 */
@RestController("itfMaterialLotIfaceSiteController.v1")
@RequestMapping("/v1/itf-material-lot-ifaces")
@Slf4j
public class ItfMaterialLotIfaceController extends BaseController {

    @Autowired
    private ItfMaterialLotIfaceService itfMaterialLotIfaceService;

    @ApiOperation(value = "物料条码接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/material-lot-and-attr-sync")
    public ResponseEntity<?> materialLotAndAttrSync(@RequestBody List<ItfMaterialLotIface> itfMaterialLotIfaceList) {
        List<ItfMaterialLotReturnDTO> itfCommonReturnDTOList = itfMaterialLotIfaceService.invoke(itfMaterialLotIfaceList);
        return Results.success(itfCommonReturnDTOList);
    }
}
