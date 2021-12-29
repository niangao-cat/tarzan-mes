package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsDistDemandQueryDTO;
import com.ruike.wms.api.dto.WmsDistributionDocCreateDTO;
import com.ruike.wms.app.service.WmsDistributionDemandService;
import com.ruike.wms.domain.service.WmsDistributionDemandExportService;
import com.ruike.wms.domain.vo.WmsDistributionDemandVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsDistributionDemand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static tarzan.config.SwaggerApiConfig.WMS_DISTRIBUTION_DEMAND;

import java.util.List;

/**
 * 配送需求表 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2020-08-31 11:09:51
 */
@RestController("wmsDistributionDemandController.v1")
@RequestMapping("/v1/{organizationId}/wms-distribution-demands")
@Api(tags = WMS_DISTRIBUTION_DEMAND)
public class WmsDistributionDemandController extends BaseController {

    private final WmsDistributionDemandService wmsDistributionDemandService;
    private final WmsDistributionDemandExportService wmsDistributionDemandExportService;

    public WmsDistributionDemandController(WmsDistributionDemandService wmsDistributionDemandService, WmsDistributionDemandExportService wmsDistributionDemandExportService) {
        this.wmsDistributionDemandService = wmsDistributionDemandService;
        this.wmsDistributionDemandExportService = wmsDistributionDemandExportService;
    }

    @ApiOperation(value = "配送需求表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsDistributionDemandVO>> list(@PathVariable("organizationId") Long tenantId, WmsDistDemandQueryDTO dto, @ApiIgnore @SortDefault(value = WmsDistributionDemand.FIELD_DIST_DEMAND_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<WmsDistributionDemandVO> list = wmsDistributionDemandService.selectListByCondition(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "配送需求导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    public ResponseEntity<Void> export(@PathVariable("organizationId") Long tenantId,
                                       WmsDistDemandQueryDTO dto,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        wmsDistributionDemandExportService.export(request, response, tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "配送单生成")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/doc")
    public ResponseEntity<?> docCreate(@PathVariable("organizationId") Long tenantId, @RequestBody WmsDistributionDocCreateDTO dto) {
        wmsDistributionDemandService.distributionDocCreate(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "配送单生成方式校验")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/check")
    public ResponseEntity<WmsDistributionDocCreateDTO> check(@PathVariable("organizationId") Long tenantId, @RequestBody List<WmsDistributionDemandVO> dto) {
        return Results.success(wmsDistributionDemandService.distributionDocCheck(tenantId, dto));
    }
}
