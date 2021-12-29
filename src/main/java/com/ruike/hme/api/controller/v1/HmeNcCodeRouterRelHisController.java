package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeNcCodeRouterRelHisService;
import com.ruike.hme.domain.vo.HmeNcCodeRouterRelHisVO;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeNcCodeRouterRelHis;
import com.ruike.hme.domain.repository.HmeNcCodeRouterRelHisRepository;
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
 * 不良代码工艺路线关系历史表 管理 API
 *
 * @author penglin.sui@hand-china.com 2021-03-30 15:50:41
 */
@RestController("hmeNcCodeRouterRelHisController.v1")
@RequestMapping("/v1/{organizationId}/hme-nc-code-router-rel-hiss")
@Api(tags = SwaggerApiConfig.HME_NC_CODE_ROUTER_REL_HIS)
public class HmeNcCodeRouterRelHisController extends BaseController {

    @Autowired
    private HmeNcCodeRouterRelHisService hmeNcCodeRouterRelHisService;

    @ApiOperation("不良代码指定工艺路线历史查询")
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNcCodeRouterRelHisVO>> list(@PathVariable(value = "organizationId") Long tenantId,
                                                              String ncCodeRouterRelId , PageRequest pageRequest) {
        return Results.success(this.hmeNcCodeRouterRelHisService.ncCodeRouterRelHisList(tenantId, ncCodeRouterRelId, pageRequest));
    }
}
