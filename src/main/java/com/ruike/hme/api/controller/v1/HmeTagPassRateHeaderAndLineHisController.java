package com.ruike.hme.api.controller.v1;

/**
 * 偏振度和发散角良率维护历史表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021/09/14 14:26
 */

import com.ruike.hme.app.service.HmeTagPassRateHeaderAndLineHisService;
import com.ruike.hme.domain.vo.HmeTagPassRateHeaderAndLineHisVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("HmeTagPassRateHeaderAndLineHisController.v1")
@RequestMapping("/v1/{organizationId}/hme-tag-pass-rate-header-and-line-hiss")
public class HmeTagPassRateHeaderAndLineHisController extends BaseController {

    private final HmeTagPassRateHeaderAndLineHisService hmeTagPassRateHeaderAndLineHisService;

    public HmeTagPassRateHeaderAndLineHisController(HmeTagPassRateHeaderAndLineHisService hmeTagPassRateHeaderAndLineHisService) {
        this.hmeTagPassRateHeaderAndLineHisService = hmeTagPassRateHeaderAndLineHisService;
    }

    @ApiOperation(value = "偏振度和发散角良率维护头表列表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-header-line-his")
    public ResponseEntity<Page<HmeTagPassRateHeaderAndLineHisVO>> queryTagPassRateHeader(@PathVariable("organizationId") Long organizationId,
                                                                                         PageRequest pageRequest,
                                                                                         String heardId) {
        return Results.success(hmeTagPassRateHeaderAndLineHisService.queryTagPassRateHeaderAndLineHis(organizationId, pageRequest, heardId));
    }
}
