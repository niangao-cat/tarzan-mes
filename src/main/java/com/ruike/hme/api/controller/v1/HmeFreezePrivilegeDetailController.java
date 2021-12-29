package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeFreezePrivilegeDetailDeleteCommand;
import com.ruike.hme.api.dto.HmeFreezePrivilegeDetailQueryDTO;
import com.ruike.hme.domain.repository.HmeFreezePrivilegeDetailRepository;
import com.ruike.hme.domain.vo.HmeFreezePrivilegeDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

/**
 * 条码冻结权限明细 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2021-02-26 17:41:20
 */
@RestController("hmeFreezePrivilegeDetailController.v1")
@RequestMapping("/v1/{organizationId}/hme-freeze-privilege-details")
@Api(tags = SwaggerApiConfig.HME_FREEZE_PRIVILEGE_DETAIL)
public class HmeFreezePrivilegeDetailController extends BaseController {

    private final HmeFreezePrivilegeDetailRepository hmeFreezePrivilegeDetailRepository;

    public HmeFreezePrivilegeDetailController(HmeFreezePrivilegeDetailRepository hmeFreezePrivilegeDetailRepository) {
        this.hmeFreezePrivilegeDetailRepository = hmeFreezePrivilegeDetailRepository;
    }

    @ApiOperation(value = "条码冻结权限明细列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeFreezePrivilegeDetailVO>> list(@PathVariable("organizationId") Long tenantId,
                                                                 HmeFreezePrivilegeDetailQueryDTO dto,
                                                                 @ApiIgnore PageRequest pageRequest) {
        dto.validation();
        Page<HmeFreezePrivilegeDetailVO> list = hmeFreezePrivilegeDetailRepository.pageByCondition(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "删除条码冻结权限明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable("organizationId") Long tenantId,
                                    @RequestBody HmeFreezePrivilegeDetailDeleteCommand command) {
        command.setTenantId(tenantId);
        hmeFreezePrivilegeDetailRepository.deleteByPrimaryKey(HmeFreezePrivilegeDetailDeleteCommand.toEntity(command));
        return Results.success();
    }

}
