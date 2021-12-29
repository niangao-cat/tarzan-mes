package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeRepairPermitJudgeDTO;
import com.ruike.hme.app.service.HmeRepairRecordService;
import com.ruike.hme.domain.entity.HmeRepairRecord;
import com.ruike.hme.domain.vo.HmeRepairPermitJudgeVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/13 17:32
 */

@RestController("hmeRepairRecordController.v1")
@RequestMapping("/v1/{organizationId}/hme-repair-judge")
@Api(tags = "返修放行判定")
@Slf4j
public class HmeRepairRecordController extends BaseController {

    @Autowired
    private HmeRepairRecordService repairService;

    @ApiOperation(value = "查询返修记录")
    @GetMapping(value = {"/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeRepairPermitJudgeVO>> list (
            @PathVariable("organizationId") Long tenantId, PageRequest pageRequest, HmeRepairPermitJudgeDTO dto){
        if (StringUtils.isNotBlank(dto.getSnNumList())) {
            List<String> snNumArray = Arrays.asList(org.apache.commons.lang.StringUtils.split(dto.getSnNumList(), ","));
            dto.setSnNumArray(snNumArray);
        }
        return Results.success(repairService.queryList(tenantId, pageRequest, dto));
    }

    @ApiOperation(value = "继续返修")
    @PostMapping(value = {"/continueRepair"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeRepairRecord> continueRepair(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody HmeRepairRecord dto){
        dto.setTenantId(tenantId);
        return Results.success(repairService.continueRepair(tenantId, dto));

    }

    @ApiOperation(value = "停止返修")
    @PostMapping(value = {"/stopRepair"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeRepairRecord> stopRepair(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody HmeRepairRecord dto){
        dto.setTenantId(tenantId);
        return Results.success(repairService.stopRepair(tenantId, dto));

    }
}
