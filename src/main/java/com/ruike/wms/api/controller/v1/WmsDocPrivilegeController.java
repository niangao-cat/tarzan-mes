package com.ruike.wms.api.controller.v1;

import com.ruike.wms.domain.vo.WmsDocPrivilegeVO;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsDocPrivilege;
import com.ruike.wms.domain.repository.WmsDocPrivilegeRepository;
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
 * 单据授权表 管理 API
 *
 * @author junfeng.chen@hand-china.com 2021-01-19 20:21:30
 */
@RestController("wmsDocPrivilegeController.v1")
@RequestMapping("/v1/{organizationId}/wms-doc-privileges")
@Api(tags = SwaggerApiConfig.WMS_DOC_PRIVILEGE)
public class WmsDocPrivilegeController extends BaseController {

    @Autowired
    private WmsDocPrivilegeRepository wmsDocPrivilegeRepository;

    @ApiOperation(value = "用户权限信息分配查询")
    @GetMapping(value = "/query")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsDocPrivilegeVO>> userPrivilegeForUi(
            @PathVariable("organizationId") Long tenantId, WmsDocPrivilegeVO dto,
            PageRequest pageRequest) {
        return Results.success(wmsDocPrivilegeRepository.userPrivilegeForUi(tenantId, pageRequest, dto));
    }

    @ApiOperation(value = "保存单据授权表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/save"},produces = "application/json;charset=UTF-8")
    public ResponseEntity<WmsDocPrivilegeVO> save(@PathVariable("organizationId") Long tenantId,@RequestBody WmsDocPrivilegeVO wmsDocPrivilegeVO) {
        return Results.success(wmsDocPrivilegeRepository.save(tenantId,wmsDocPrivilegeVO));
    }
}
