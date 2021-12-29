package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsMaterialLotEditDTO;
import com.ruike.wms.api.dto.WmsMaterialLotEditResponseDTO;
import com.ruike.wms.api.dto.WmsMaterialLotEditUomDTO;
import com.ruike.wms.app.service.WmsMaterialLotEditService;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static tarzan.config.SwaggerApiConfig.WMS_MATERIAL_LOT_EDIT;

/**
 * @author kun.zhou
 * @Classname MaterialLotEditController
 * @Description 条码调整Controller
 * @Date 2020-03-17 11:08
 */
@RestController("MaterialLotEditController.v1")
@RequestMapping("/v1/{organizationId}/material-lot-edit")
@Api(tags = WMS_MATERIAL_LOT_EDIT)
@Slf4j
public class WmsMaterialLotEditController {

    @Autowired
    private WmsMaterialLotEditService materialLotEditService;

    @ApiOperation(value = "条码调整查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/queryMaterialLotEdit"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<WmsMaterialLotEditResponseDTO>> queryMaterialLotEdit(@PathVariable("organizationId") Long tenantId,
                                                                                    WmsMaterialLotEditDTO dto, PageRequest pageRequest) {
        return Results.success(materialLotEditService.queryMaterialLotEdit(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "单位LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/UomDropDownBoxQuery")
    public ResponseEntity<Page<WmsMaterialLotEditUomDTO>> queryUom(@PathVariable("organizationId") Long tenantId, PageRequest pageRequest) {
        return Results.success(materialLotEditService.queryUom(tenantId, pageRequest));
    }

    @ApiOperation(value = "条码更新")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/updateMaterialLotEdit")
    public ResponseEntity<Page<WmsMaterialLotEditDTO>> updateMaterialLotEdit(@PathVariable("organizationId") Long tenantId,
                                                                             @RequestBody List<WmsMaterialLotEditResponseDTO> dtoList) {
        return Results.success(materialLotEditService.updateMaterialLotData(tenantId, dtoList));
    }

}
