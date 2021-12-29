package com.ruike.mdm.api.controller.v1;

import com.ruike.mdm.api.dto.query.ModSiteQuery;
import com.ruike.mdm.api.dto.representation.ModSiteRept;
import com.ruike.mdm.domain.repository.ModSiteRepository;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 站点API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/12 15:22
 */
@RestController("modSiteController.v1")
@RequestMapping("/v1/{organizationId}/site")
@Api("ModSite")
public class ModSiteController extends BaseController {
    private final ModSiteRepository siteRepository;

    public ModSiteController(ModSiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @ApiOperation("站点信息 查询列表")
    @GetMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<ModSiteRept>> list(@PathVariable(value = "organizationId") Long tenantId,
                                                  ModSiteQuery query) {
        query.setTenantId(tenantId);
        List<ModSiteRept> list = siteRepository.list(query);
        return Results.success(list);
    }
}
