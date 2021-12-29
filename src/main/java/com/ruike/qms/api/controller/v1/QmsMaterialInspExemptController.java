package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO;
import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO2;
import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO3;
import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO4;
import com.ruike.qms.app.service.QmsMaterialInspExemptService;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 物料免检表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:06:18
 */
@RestController("qmsMaterialInspExemptController.v1")
@RequestMapping("/v1/{organizationId}/qms-material-insp-exempts")
@Api(tags = SwaggerApiConfig.QMS_MATERIAL_INSP_EXEMPT)
@Slf4j
public class QmsMaterialInspExemptController extends BaseController {

    @Autowired
    private QmsMaterialInspExemptService qmsMaterialInspExemptService;

    @ApiOperation(value = "物料免检查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsMaterialInspExemptDTO>> listMaterialInspExemptForUi(QmsMaterialInspExemptDTO2 dto,
                            @ApiIgnore PageRequest pageRequest, @PathVariable(value = "organizationId") Long tenantId) {
        Page<QmsMaterialInspExemptDTO> list = qmsMaterialInspExemptService.listMaterialInspExemptForUi(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "物料免检保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> saveMaterialInspExemptForUi(@RequestBody QmsMaterialInspExemptDTO4 dto,
                                                            @PathVariable(value = "organizationId") Long tenantId) {
        log.info("<====QmsMaterialInspExemptController-saveMaterialInspExemptForUi:{},{}  ", tenantId , dto );
        validObject(dto);
        String str = qmsMaterialInspExemptService.saveMaterialInspExemptForUi(tenantId, dto);
        return Results.success(str);
    }

    @ApiOperation(value = "物料免检删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Void> removeMaterialInspExemptForUi(@RequestBody List<QmsMaterialInspExemptDTO3> list,
                                     @PathVariable(value = "organizationId") Long tenantId) {
        log.info("<====QmsMaterialInspExemptController-removeMaterialInspExemptForUi:{},{}  ", tenantId , list );
        qmsMaterialInspExemptService.removeMaterialInspExemptForUi(tenantId, list);
        return Results.success();
    }

}
