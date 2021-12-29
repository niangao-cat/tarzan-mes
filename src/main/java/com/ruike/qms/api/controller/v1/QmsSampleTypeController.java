package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsSampleTypeQueryDTO;
import com.ruike.qms.api.dto.QmsSampleTypeReturnDTO;
import com.ruike.qms.api.dto.QmsSampleTypeSaveDTO;
import com.ruike.qms.domain.repository.QmsSampleTypeRepository;
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
 * 抽样类型管理 管理 API
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:44
 */
@RestController("qmsSampleTypeController.v1")
@RequestMapping("/v1/{organizationId}/qms-sample-types")
@Api(tags = SwaggerApiConfig.QMS_SAMPLE_TYPE)
@Slf4j
public class QmsSampleTypeController extends BaseController {

    @Autowired
    private QmsSampleTypeRepository qmsSampleTypeRepository;

    /**
     * @Description 查询抽样类型列表
     * @param tenantId
     * @param queryDTO
     * @param pageRequest
     * @return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsSampleTypeReturnDTO>>
     * @Date 2020-05-07 10:21
     * @Author han.zhang
     */
    @ApiOperation(value = "查询抽样类型列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list/ui")
    public ResponseEntity<Page<QmsSampleTypeReturnDTO>> list(@PathVariable("organizationId") Long tenantId,QmsSampleTypeQueryDTO queryDTO, @ApiIgnore PageRequest pageRequest) {

        Page<QmsSampleTypeReturnDTO> list = qmsSampleTypeRepository.selectSampleType(tenantId,queryDTO,pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "抽样类型保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> saveSampleSchemeForUi(@RequestBody QmsSampleTypeSaveDTO dto,
                                                      @PathVariable(value = "organizationId") Long tenantId) {
        log.info("<====QmsSampleTypeController-saveSampleSchemeForUi:{},{}",tenantId, dto);
        validObject(dto);
        String str = qmsSampleTypeRepository.saveSampleTypeForUi(tenantId, dto);
        return Results.success(str);
    }


}
