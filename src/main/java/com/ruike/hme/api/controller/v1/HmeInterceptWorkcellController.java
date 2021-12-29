package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeInterceptWorkcellService;
import com.ruike.hme.domain.vo.HmeInterceptWorkcellVO;
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
 * 拦截工序表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:11
 */
@RestController("hmeInterceptWorkcellController.v1")
@RequestMapping("/v1/{organizationId}/hme-intercept-workcells")
public class HmeInterceptWorkcellController extends BaseController {

    private final HmeInterceptWorkcellService hmeInterceptWorkcellService;

    public HmeInterceptWorkcellController(HmeInterceptWorkcellService hmeInterceptWorkcellService) {
        this.hmeInterceptWorkcellService = hmeInterceptWorkcellService;
    }

    @ApiOperation(value = "拦截工序表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-Intercept-workcell")

    public ResponseEntity<Page<HmeInterceptWorkcellVO>> queryInterceptWorkcell(@PathVariable("organizationId") Long organizationId,
                                                                               String interceptId,
                                                                               PageRequest pageRequest) {
        return Results.success(hmeInterceptWorkcellService.queryInterceptWorkcell(organizationId, pageRequest, interceptId));
    }

    @ApiOperation(value = "拦截工序表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-intercept-workcell")
    public ResponseEntity<HmeInterceptWorkcellVO> saveInterceptWorkcell(@PathVariable("organizationId") Long organizationId,
                                                                        @RequestBody List<HmeInterceptWorkcellVO> hmeInterceptWorkcellVOList,
                                                                        String interceptId) {
        hmeInterceptWorkcellService.saveInterceptWorkcell(organizationId, hmeInterceptWorkcellVOList, interceptId);
        return Results.success();
    }

    @ApiOperation(value = "放行拦截工序表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pass-intercept-workcell")
    public ResponseEntity<HmeInterceptWorkcellVO> passInterceptWorkcell(@PathVariable("organizationId") Long organizationId,
                                                                        String interceptId,
                                                                        @RequestBody List<HmeInterceptWorkcellVO> hmeInterceptWorkcellVOList) {
        hmeInterceptWorkcellService.passInterceptWorkcell(organizationId, interceptId, hmeInterceptWorkcellVOList);
        return Results.success();
    }


}
