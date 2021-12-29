package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsSampleSchemeDTO;
import com.ruike.qms.api.dto.QmsSampleSchemeDTO2;
import com.ruike.qms.api.dto.QmsSampleSchemeDTO3;
import com.ruike.qms.api.dto.QmsSampleSchemeDTO4;
import com.ruike.qms.app.service.QmsSampleSchemeService;
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

/**
 * 抽样方案表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:10
 */
@RestController("qmsSampleSchemeController.v1")
@RequestMapping("/v1/{organizationId}/qms-sample-schemes")
@Api(tags = SwaggerApiConfig.QMS_SAMPLE_SCHEMES)
@Slf4j
public class QmsSampleSchemeController extends BaseController {

    @Autowired
    private QmsSampleSchemeService qmsSampleSchemeService;

    @ApiOperation(value = "抽样方案查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsSampleSchemeDTO>> listSampleSchemeForUi(@ApiIgnore PageRequest pageRequest,
                                                                        @PathVariable(value = "organizationId") Long tenantId,
                                                                        QmsSampleSchemeDTO2 dto) {
        Page<QmsSampleSchemeDTO> list = qmsSampleSchemeService.listSampleSchemeForUi(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "抽样方案保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> saveSampleSchemeForUi(@RequestBody QmsSampleSchemeDTO3 dto,
                                                            @PathVariable(value = "organizationId") Long tenantId) {
        log.info("<====QmsSampleSchemeController-saveSampleSchemeForUi:{},{}",tenantId,dto);
        validObject(dto);
        String str = qmsSampleSchemeService.saveSampleSchemeForUi(tenantId, dto);
        return Results.success(str);
    }

    @ApiOperation(value = "抽样方案LOV查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/scheme/lov/query"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsSampleSchemeDTO>> sampleSchemeLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                         QmsSampleSchemeDTO4 dto, PageRequest pageRequest){
        return Results.success(qmsSampleSchemeService.sampleSchemeLovQuery(tenantId, dto, pageRequest));
    }
}
