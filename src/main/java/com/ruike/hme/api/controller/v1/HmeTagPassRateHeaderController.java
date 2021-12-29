package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeTagPassRateHeaderDTO;
import com.ruike.hme.app.service.HmeTagPassRateHeaderService;
import com.ruike.hme.domain.vo.HmeTagPassRateHeaderVO;
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
 * 偏振度和发散角良率维护头表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:35
 */
@RestController("hmeTagPassRateHeaderController.v1")
@RequestMapping("/v1/{organizationId}/hme-tag-pass-rate-headers")
public class HmeTagPassRateHeaderController extends BaseController {

    private final HmeTagPassRateHeaderService hmeTagPassRateHeaderService;

    public HmeTagPassRateHeaderController(HmeTagPassRateHeaderService hmeTagPassRateHeaderService) {
        this.hmeTagPassRateHeaderService = hmeTagPassRateHeaderService;
    }

    @ApiOperation(value = "偏振度和发散角良率维护头表列表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-pass-rate-header")
    public ResponseEntity<Page<HmeTagPassRateHeaderVO>> queryTagPassRateHeader(@PathVariable("organizationId") Long organizationId,
                                                                               HmeTagPassRateHeaderDTO dto,
                                                                               PageRequest pageRequest) {
        return Results.success(hmeTagPassRateHeaderService.queryTagPassRateHeader(organizationId, dto, pageRequest));
    }

    @ApiOperation(value = "创建偏振度和发散角良率维护头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-pass-rate-header")
    public ResponseEntity<HmeTagPassRateHeaderVO> savePassRateHeader(@PathVariable("organizationId") Long organizationId,
                                                                     @RequestBody HmeTagPassRateHeaderVO hmeTagPassRateHeaderVO) {
        hmeTagPassRateHeaderService.savePassRateHeader(organizationId, hmeTagPassRateHeaderVO);
        return Results.success();
    }


}
