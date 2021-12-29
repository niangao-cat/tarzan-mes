package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsSampleSizeCodeLetterDTO;
import com.ruike.qms.app.service.QmsSampleSizeCodeLetterService;
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
 * 样本量字码表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:11
 */
@RestController("qmsSampleSizeCodeLetterController.v1")
@RequestMapping("/v1/{organizationId}/qms-sample-size-code-letters")
@Api(tags = SwaggerApiConfig.QMS_SAMPLE_SIZE_CODE_LETTERS)
@Slf4j
public class QmsSampleSizeCodeLetterController extends BaseController {

    @Autowired
    private QmsSampleSizeCodeLetterService qmsSampleSizeCodeLetterService;

    @ApiOperation(value = "样本量字码查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsSampleSizeCodeLetterDTO>> listSampleSizeCodeLetterForUi(@ApiIgnore PageRequest pageRequest,
                                                                                        @PathVariable(value = "organizationId") Long tenantId,
                                                                                        Long  lotSize) {
        Page<QmsSampleSizeCodeLetterDTO> list = qmsSampleSizeCodeLetterService.listSampleSizeCodeLetterForUi(tenantId, lotSize, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "样本量字码保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Void> saveSampleSizeCodeLetterForUi(@RequestBody List<QmsSampleSizeCodeLetterDTO> dtoList,
                                                            @PathVariable(value = "organizationId") Long tenantId) {
        log.info("<====QmsSampleSizeCodeLetterController-saveSampleSizeCodeLetterForUi:{},{}",tenantId,dtoList);
        validList(dtoList);
        qmsSampleSizeCodeLetterService.saveSampleSizeCodeLetterForUi(tenantId, dtoList);
        return Results.success();
    }

}
