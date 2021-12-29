package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.api.dto.HmeObjectRecordLockParamDTO;
import com.ruike.hme.api.dto.HmeObjectRecordLockReturnDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.infra.constant.HmeConstants;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
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

/**
 * 记录锁定表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-09-12 11:33:17
 */
@RestController("hmeObjectRecordLockController.v1")
@RequestMapping("/v1/{organizationId}/hme-object-record-locks")
@Api(tags = SwaggerApiConfig.HME_OBJECT_RECORD_LOCK)
public class HmeObjectRecordLockController extends BaseController {

    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;
    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @ApiOperation(value = "记录锁定表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeObjectRecordLockReturnDTO>> listForUi(@PathVariable("organizationId") Long tenantId,
                                                                        HmeObjectRecordLockParamDTO dto,
                                                                        @ApiIgnore PageRequest pageRequest) {
        Page<HmeObjectRecordLockReturnDTO> list = hmeObjectRecordLockRepository.listForUi(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "锁定")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/lock")
    public ResponseEntity<HmeObjectRecordLock> create(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody HmeObjectRecordLockDTO dto) {
        validObject(dto);
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, dto);
        hmeObjectRecordLockRepository.lock(hmeObjectRecordLock);
        return Results.success(hmeObjectRecordLock);
    }

    @ApiOperation(value = "解除锁定")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-lock")
    public ResponseEntity<HmeObjectRecordLock> releaseLock(@PathVariable("organizationId") Long tenantId,
                                    @RequestBody HmeObjectRecordLockDTO dto) {
        validObject(dto);
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, dto);
        hmeObjectRecordLockRepository.releaseLock(hmeObjectRecordLock, HmeConstants.ConstantValue.NO);
        return Results.success(hmeObjectRecordLock);
    }

    @ApiOperation(value = "管理员前台解除锁定")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/admin/release-lock")
    public ResponseEntity<HmeObjectRecordLock> adminReleaseLock(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody HmeObjectRecordLockDTO dto) {
        validObject(dto);
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, dto);
        hmeObjectRecordLockRepository.releaseLock(hmeObjectRecordLock, HmeConstants.ConstantValue.YES);
        return Results.success(hmeObjectRecordLock);
    }

}
