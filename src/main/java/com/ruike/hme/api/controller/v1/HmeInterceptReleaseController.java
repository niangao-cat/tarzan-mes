package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeInterceptReleaseService;
import com.ruike.hme.domain.entity.HmeInterceptRelease;
import com.ruike.hme.domain.vo.HmeInterceptReleaseVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 拦截例外放行表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:10
 */
@RestController("hmeInterceptReleaseController.v1")
@RequestMapping("/v1/{organizationId}/hme-intercept-releases")
public class HmeInterceptReleaseController extends BaseController {

    private final HmeInterceptReleaseService hmeInterceptReleaseService;

    public HmeInterceptReleaseController(HmeInterceptReleaseService hmeInterceptReleaseService) {
        this.hmeInterceptReleaseService = hmeInterceptReleaseService;
    }

    @ApiOperation(value = "拦截例外放行表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-intercept-release")
    public ResponseEntity<Page<HmeInterceptReleaseVO>> queryInterceptRelease(@PathVariable("organizationId") Long organizationId, String interceptId, PageRequest pageRequest) {
        return Results.success(hmeInterceptReleaseService.queryInterceptRelease(organizationId, interceptId, pageRequest));
    }


    @ApiOperation(value = "创建拦截例外放行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-intercept-release")
    public ResponseEntity<HmeInterceptRelease> saveInterceptRelease(@PathVariable("organizationId") Long organizationId, String interceptId,
                                                                    @RequestBody List<HmeInterceptReleaseVO> hmeInterceptReleaseVOList) {
        hmeInterceptReleaseService.saveInterceptRelease(organizationId, interceptId, hmeInterceptReleaseVOList);
        return Results.success();
    }


}
