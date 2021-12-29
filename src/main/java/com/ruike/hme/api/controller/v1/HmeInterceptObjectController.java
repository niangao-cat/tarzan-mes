package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeInterceptObjectService;
import com.ruike.hme.domain.vo.HmeInterceptObjectVO;
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
 * 拦截对象表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:08
 */
@RestController("hmeInterceptObjectController.v1")
@RequestMapping("/v1/{organizationId}/hme-intercept-objects")
public class HmeInterceptObjectController extends BaseController {

    private final HmeInterceptObjectService hmeInterceptObjectService;

    public HmeInterceptObjectController(HmeInterceptObjectService hmeInterceptObjectService) {
        this.hmeInterceptObjectService = hmeInterceptObjectService;
    }

    @ApiOperation(value = "拦截对象表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-intercept-object")
    public ResponseEntity<Page<HmeInterceptObjectVO>> queryInterceptObject(@PathVariable("organizationId") Long organizationId, String interceptId,
                                                                           PageRequest pageRequest) {
        return Results.success(hmeInterceptObjectService.queryInterceptObject(organizationId, interceptId, pageRequest));
    }


    @ApiOperation(value = "创建拦截对象表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-intercept-object")
    public ResponseEntity<HmeInterceptObjectVO> saveInterceptObject(@PathVariable("organizationId") Long organizationId,
                                                                    @RequestBody List<HmeInterceptObjectVO> hmeInterceptObjectVOList,
                                                                    String interceptId) {
        hmeInterceptObjectService.saveInterceptObject(organizationId, hmeInterceptObjectVOList, interceptId);
        return Results.success();
    }

    @ApiOperation(value = "放行拦截对象表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pass-intercept-object")
    public ResponseEntity<HmeInterceptObjectVO> passInterceptObject(@PathVariable("organizationId") Long organizationId,
                                                                    String interceptId,
                                                                    @RequestBody List<HmeInterceptObjectVO> hmeInterceptObjectVOList) {
        hmeInterceptObjectService.passInterceptObject(organizationId, interceptId, hmeInterceptObjectVOList);
        return Results.success();
    }


}
