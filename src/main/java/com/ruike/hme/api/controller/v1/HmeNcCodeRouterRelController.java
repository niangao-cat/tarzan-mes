package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeNcCodeRouterRelDTO;
import com.ruike.hme.app.service.HmeNcCodeRouterRelService;
import com.ruike.hme.domain.vo.HmeNcCodeRouterRelVO;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeNcCodeRouterRel;
import com.ruike.hme.domain.repository.HmeNcCodeRouterRelRepository;
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
 * 不良代码工艺路线关系表 管理 API
 *
 * @author penglin.sui@hand-china.com 2021-03-30 15:50:41
 */
@RestController("hmeNcCodeRouterRelController.v1")
@RequestMapping("/v1/{organizationId}/hme-nc-code-router-rels")
@Api(tags = SwaggerApiConfig.HME_NC_CODE_ROUTER_REL)
public class HmeNcCodeRouterRelController extends BaseController {

    @Autowired
    private HmeNcCodeRouterRelService hmeNcCodeRouterRelService;

    @ApiOperation("不良代码指定工艺路线查询")
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNcCodeRouterRelVO>> list(@PathVariable(value = "organizationId") Long tenantId,
                                                           HmeNcCodeRouterRelDTO dto, PageRequest pageRequest) {
        dto.initParam();
        return Results.success(this.hmeNcCodeRouterRelService.ncCodeRouterRelList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "创建不良代码工艺路线关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeNcCodeRouterRel> save(@PathVariable(value = "organizationId") Long tenantId,
                                                   @RequestBody HmeNcCodeRouterRel dto) {
        hmeNcCodeRouterRelService.ncCodeRouterRelSave(tenantId,dto);
        return Results.success(dto);
    }
}
