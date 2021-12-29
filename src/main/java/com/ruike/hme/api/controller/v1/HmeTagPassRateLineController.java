package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeTagPassRateLineService;
import com.ruike.hme.domain.vo.HmeTagPassRateLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 偏振度和发散角良率维护行表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:37
 */
@RestController("hmeTagPassRateLineController.v1")
@RequestMapping("/v1/{organizationId}/hme-tag-pass-rate-lines")
public class HmeTagPassRateLineController extends BaseController {

    private final HmeTagPassRateLineService hmeTagPassRateLineService;

    public HmeTagPassRateLineController(HmeTagPassRateLineService hmeTagPassRateLineService) {
        this.hmeTagPassRateLineService = hmeTagPassRateLineService;
    }

    @ApiOperation(value = "偏振度和发散角良率维护行表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-pass-rate-line")
    public ResponseEntity<Page<HmeTagPassRateLineVO>> queryTagPassRateLine(@PathVariable("organizationId") Long organizationId, String heardId,
                                                                           PageRequest pageRequest) {
        return Results.success(hmeTagPassRateLineService.queryTagPassRateLine(organizationId, heardId, pageRequest));
    }


    @ApiOperation(value = "创建偏振度和发散角良率维护行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-pass-rate-line")
    public ResponseEntity<HmeTagPassRateLineVO> saveTagPassRateLine(@PathVariable("organizationId") Long organizationId,
                                                                    @RequestBody HmeTagPassRateLineVO hmeTagPassRateLineVO) {
        hmeTagPassRateLineService.saveTagPassRateLine(organizationId, hmeTagPassRateLineVO);
        return Results.success();
    }


}
