package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsDistributionBasicDataDTO;
import com.ruike.wms.api.dto.WmsDistributionBasicDataDTO2;
import com.ruike.wms.app.service.WmsDistributionBasicDataService;
import com.ruike.wms.domain.vo.WmsDistributionBasicDataVO;
import com.ruike.wms.domain.vo.WmsDistributionBasicDataVO1;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsDistributionBasicData;
import com.ruike.wms.domain.repository.WmsDistributionBasicDataRepository;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 配送基础数据表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 21:05:25
 */
@RestController("wmsDistributionBasicDataController.v1")
@RequestMapping("/v1/{organizationId}/wms-distribution-basic-datas")
@Api(tags = SwaggerApiConfig.WMS_DISTRIBUTION_BASIC_DATA)
public class WmsDistributionBasicDataController extends BaseController {

    @Autowired
    private WmsDistributionBasicDataService wmsDistributionBasicDataService;

    @ApiOperation(value = "查询配送基础数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/query"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<WmsDistributionBasicDataVO>> query(@PathVariable("organizationId") Long tenantId,
                                                                  WmsDistributionBasicDataDTO2 dto, PageRequest pageRequest){
        return Results.success(wmsDistributionBasicDataService.query(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "导出配送基础数据")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(WmsDistributionBasicDataVO.class)
    @GetMapping(value = {"/data-export"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<WmsDistributionBasicDataVO>> dataExport(@PathVariable("organizationId") Long tenantId,
                                                                       WmsDistributionBasicDataDTO2 dto,
                                                                       HttpServletResponse response,
                                                                       ExportParam exportParam){
        return Results.success(wmsDistributionBasicDataService.dataExport(tenantId, dto));
    }

    @ApiOperation(value = "创建配送基础数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/create"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> createOrUpdate(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody WmsDistributionBasicDataDTO dto) {
        wmsDistributionBasicDataService.create(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "更新配送基础数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/update"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> update(@PathVariable("organizationId") Long tenantId,
                                    @RequestBody WmsDistributionBasicDataVO dto){
        wmsDistributionBasicDataService.update(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "工段Lov-限定产线")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/process")
    public ResponseEntity<Page<WmsDistributionBasicDataVO1>> processLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                             WmsDistributionBasicDataVO1 dto, PageRequest pageRequest){
        return Results.success(wmsDistributionBasicDataService.processLovQuery(tenantId, dto, pageRequest));
    }
}