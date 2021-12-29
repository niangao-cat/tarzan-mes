package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeRepairLimitCountDTO;
import com.ruike.hme.app.service.HmeRepairLimitCountService;
import com.ruike.hme.domain.vo.HmeRepairLimitCountVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/7 10:02
 */

@RestController("hmeRepairLimitCountController.v1")
@RequestMapping("/v1/{organizationId}/hme-repair-limit-count")
@Api(tags = "返修进站次数维护")
@Slf4j
public class HmeRepairLimitCountController extends BaseController {
    @Autowired
    private HmeRepairLimitCountService service;

    @ApiOperation("查询返修进站次数限制列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeRepairLimitCountVO>> list(@PathVariable("organizationId") Long tenantId, PageRequest pageRequest, HmeRepairLimitCountDTO dto){
        return Results.success(service.queryRepairLimitCountList(tenantId, pageRequest, dto));
    }

    @ApiOperation("删除返修进站次数限制")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> list) {
        service.deleteRepairLimitCountByIds(tenantId, list);
        return Results.success();
    }

    @ApiOperation("新增&更新返修进站次数限制")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/createOrUpdate")
    public ResponseEntity<List<HmeRepairLimitCountDTO>> createOrUpdate (
            @PathVariable("organizationId") Long tenantId, @RequestBody List<HmeRepairLimitCountDTO> dto){
        return Results.success(service.createOrUpdateRepairLimitCount(tenantId, dto));
    }

}
