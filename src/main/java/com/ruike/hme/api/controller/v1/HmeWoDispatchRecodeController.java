package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeWoDispatchCompSuiteQueryDTO;
import com.ruike.hme.api.dto.HmeWoDispatchDTO;
import com.ruike.hme.api.dto.HmeWoDispatchSuiteQueryDTO;
import com.ruike.hme.app.service.HmeWoDispatchRecodeService;
import com.ruike.hme.domain.vo.HmeModAreaVO;
import com.ruike.hme.domain.vo.HmeWoDispatchComponentSuiteVO;
import com.ruike.hme.domain.vo.HmeWoDispatchSuiteVO;
import com.ruike.hme.domain.vo.HmeWoDispatchVO;
import com.ruike.hme.domain.vo.HmeWoDispatchVO6;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.swagger.annotations.Api;
import org.hzero.core.base.BaseController;

import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 工单派工记录表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
@RestController("hmeWoDispatchRecodeController.v1")
@RequestMapping("/v1/{organizationId}/hme-wo-dispatch-recodes")
@Api(tags = SwaggerApiConfig.HME_WO_DISPATCH_RECODE)
public class HmeWoDispatchRecodeController extends BaseController {

    private final HmeWoDispatchRecodeService hmeWoDispatchRecodeService;

    public HmeWoDispatchRecodeController(HmeWoDispatchRecodeService hmeWoDispatchRecodeService) {
        this.hmeWoDispatchRecodeService = hmeWoDispatchRecodeService;
    }

    @ApiOperation(value = "产线负荷统计查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/prod-line/list")
    public ResponseEntity<List<HmeWoDispatchVO6>> woProdLineListQuery(@PathVariable(value = "organizationId") Long tenantId
            , @RequestParam String prodLineId) {
        return Results.success(hmeWoDispatchRecodeService.woProdLineListQuery(tenantId, prodLineId));
    }

    @ApiOperation(value = "工单派工组件齐套明细分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/suite/detail")
    public ResponseEntity<Page<HmeWoDispatchComponentSuiteVO>> suiteComponentQuery(@PathVariable(value = "organizationId") Long tenantId
            , HmeWoDispatchCompSuiteQueryDTO dto
            , PageRequest pageRequest) {
        this.validObject(dto);
        return Results.success(hmeWoDispatchRecodeService.suiteComponentQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工单派工记录查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list")
    public ResponseEntity<List<HmeWoDispatchVO>> woDispatchListQuery(@PathVariable(value = "organizationId") Long tenantId
            , @RequestParam String prodLineId, @RequestBody List<String> workOrderIdList) {
        return Results.success(hmeWoDispatchRecodeService.woDispatchListQuery(tenantId, prodLineId, workOrderIdList));
    }

    @ApiOperation(value = "工单派工齐套列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/suite")
    public ResponseEntity<List<HmeWoDispatchSuiteVO>> suiteQuery(@PathVariable(value = "organizationId") Long tenantId
            , @RequestParam("siteId") String siteId
            , @RequestBody List<HmeWoDispatchSuiteQueryDTO> queryList) {
        this.validList(queryList);
        return Results.success(hmeWoDispatchRecodeService.suiteQuery(tenantId, queryList, siteId));
    }

    @ApiOperation(value = "UI批量保存派工记录")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/save/batch/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> create(@RequestBody List<HmeWoDispatchDTO> dtoList, @PathVariable(value = "organizationId") Long tenantId) {
        hmeWoDispatchRecodeService.saveDispatchRecodeBatchForUi(tenantId, dtoList);
        return Results.success();
    }
}
