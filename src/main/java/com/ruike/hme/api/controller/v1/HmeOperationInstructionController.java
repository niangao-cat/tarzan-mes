package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeOperationInstructionService;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeOperationInstruction;
import com.ruike.hme.domain.repository.HmeOperationInstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 作业指导 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-10-20 16:07:50
 */
@RestController("hmeOperationInstructionController.v1")
@RequestMapping("/v1/{organizationId}/hme-operation-instructions")
@Api(tags = SwaggerApiConfig.HME_OPERATION_INSTRUCTION)
public class HmeOperationInstructionController extends BaseController {

    @Autowired
    private HmeOperationInstructionRepository hmeOperationInstructionRepository;

    @Autowired
    private HmeOperationInstructionService hmeOperationInstructionService;

    @ApiOperation(value = "作业指导列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list-ui/{insHeadId}")
    public ResponseEntity<Page<HmeOperationInstruction>> listForUi(@PathVariable String insHeadId,
                                                                   @PathVariable(value = "organizationId") Long tenantId,
                                                                   PageRequest pageRequest) {
        Page<HmeOperationInstruction> list =
                hmeOperationInstructionService.listForUi(tenantId, insHeadId, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "创建作业指导")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-ui")
    public ResponseEntity<List<HmeOperationInstruction>> saveForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                             @RequestBody List<HmeOperationInstruction> hmeOperationInsList) {
        validList(hmeOperationInsList);
        return Results.success(hmeOperationInstructionService.saveForUi(tenantId, hmeOperationInsList));
    }

    @ApiOperation(value = "删除作业指导")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable(value = "organizationId") Long tenantId,
                                    @RequestBody HmeOperationInstruction hmeOperationInstruction) {
        SecurityTokenHelper.validToken(hmeOperationInstruction);
        hmeOperationInstructionRepository.deleteByPrimaryKey(hmeOperationInstruction);
        return Results.success();
    }

}
