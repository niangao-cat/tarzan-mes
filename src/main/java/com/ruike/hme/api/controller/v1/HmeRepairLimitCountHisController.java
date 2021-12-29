package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeRepairLimitCountHisService;
import com.ruike.hme.domain.vo.HmeRepairLimitCountHisVO;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;

/**
 * 返修进站限制次数历史表表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2021-09-13 16:41:22
 */
@RestController("hmeRepairLimitCountHisController.v1")
@RequestMapping("/v1/{organizationId}/hme-repair-limit-count-his")
public class HmeRepairLimitCountHisController extends BaseController {

    @Autowired
    private HmeRepairLimitCountHisService hmeRepairLimitCountHisService;

    @ApiOperation(value = "返修进站限制次数历史表表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list")
    public ResponseEntity<Page<HmeRepairLimitCountHisVO>> list(
            @PathVariable("organizationId") Long tenantId, PageRequest pageRequest, String repairLimitCountId) {
        Page<HmeRepairLimitCountHisVO> list = hmeRepairLimitCountHisService.list(tenantId, pageRequest, repairLimitCountId);
        return Results.success(list);
    }

}
