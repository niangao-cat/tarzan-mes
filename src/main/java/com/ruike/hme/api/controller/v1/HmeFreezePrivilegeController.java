package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeFreezePrivilegeQueryDTO;
import com.ruike.hme.api.dto.HmeFreezePrivilegeSaveCommandDTO;
import com.ruike.hme.domain.repository.HmeFreezePrivilegeRepository;
import com.ruike.hme.domain.service.HmeFreezePrivilegeDomainService;
import com.ruike.hme.domain.vo.HmeFreezePrivilegeVO;
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
 * 条码冻结权限 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2021-02-26 17:41:20
 */
@RestController("hmeFreezePrivilegeController.v1")
@RequestMapping("/v1/{organizationId}/hme-freeze-privileges")
@Api(tags = SwaggerApiConfig.HME_FREEZE_PRIVILEGE)
public class HmeFreezePrivilegeController extends BaseController {

    private final HmeFreezePrivilegeRepository hmeFreezePrivilegeRepository;
    private final HmeFreezePrivilegeDomainService domainService;

    public HmeFreezePrivilegeController(HmeFreezePrivilegeRepository hmeFreezePrivilegeRepository, HmeFreezePrivilegeDomainService domainService) {
        this.hmeFreezePrivilegeRepository = hmeFreezePrivilegeRepository;
        this.domainService = domainService;
    }

    @ApiOperation(value = "条码冻结权限 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeFreezePrivilegeVO>> list(@PathVariable("organizationId") Long tenantId,
                                                           HmeFreezePrivilegeQueryDTO dto,
                                                           @ApiIgnore PageRequest pageRequest) {
        Page<HmeFreezePrivilegeVO> list = hmeFreezePrivilegeRepository.byCondition(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "条码冻结权限 明细查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{privilegeId}")
    public ResponseEntity<HmeFreezePrivilegeVO> detail(@PathVariable("organizationId") Long tenantId,
                                                       @PathVariable String privilegeId) {
        HmeFreezePrivilegeVO hmeFreezePrivilege = hmeFreezePrivilegeRepository.byId(tenantId, privilegeId);
        return Results.success(hmeFreezePrivilege);
    }

    @ApiOperation(value = "条码冻结权限 保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeFreezePrivilegeVO> save(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody HmeFreezePrivilegeSaveCommandDTO dto) {
        validObject(dto);
        dto.setTenantId(tenantId);
        return Results.success(domainService.save(dto));
    }

}
