package com.ruike.hme.api.controller.v1;


import com.ruike.hme.api.dto.HmeProductionLineDetailsDTO;
import com.ruike.hme.api.dto.HmeProductionQueryDTO;
import com.ruike.hme.app.service.HmeProLineDetailsService;
import com.ruike.hme.domain.vo.HmeProcessReportVo;
import com.ruike.hme.domain.vo.HmeProductEoInfoVO;
import com.ruike.hme.domain.vo.HmeProductionLineDetailsVO;
import com.ruike.hme.domain.vo.HmeProductionQueryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModWorkcell;

import javax.servlet.http.HttpServletResponse;

/**
 * 产线日明细报表 管理 API
 *
 * @author bao.xu@hand-china.com 2020-07-07 11:40:20
 */

@RestController("hmeProLineDetailsController.v1")
@RequestMapping("/v1/{organizationId}/hme-pro-line-details")
@Api(tags = SwaggerApiConfig.HME_PRO_LINE_DETAILS)
public class HmeProLineDetailsController {

    @Autowired
    private HmeProLineDetailsService hmeProLineDetailsService;

    @ApiModelProperty(value = "车间类型信息分页查询LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/queryModAreaList", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<MtModArea>> queryModAreaList(@PathVariable("organizationId") Long tenantId,
                                                            @ApiIgnore PageRequest pageRequest){
        return Results.success(hmeProLineDetailsService.queryModArea(tenantId, pageRequest));
    }

    @ApiModelProperty(value = "产线信息分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/queryProductionLineDetails")
    public ResponseEntity<Page<HmeProductionLineDetailsDTO>> queryProductionLineDetails(@PathVariable("organizationId") Long tenantId,
                                                                @ApiIgnore PageRequest pageRequest, HmeProductionLineDetailsVO params) throws Exception {
        return Results.success(hmeProLineDetailsService.queryProductionLineDetails(tenantId, pageRequest, params));
    }

    @ApiModelProperty(value = "工序信息查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/queryWorkcells")
    public ResponseEntity<Page<MtModWorkcell>> queryWorkcells(@PathVariable("organizationId") Long tenantId,
                                                                @ApiIgnore PageRequest pageRequest, HmeProductionQueryVO params){
        return Results.success(hmeProLineDetailsService.selectWorkcells(tenantId, pageRequest, params));
    }

    @ApiModelProperty(value = "在制查询报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/queryProductDetails")
    public ResponseEntity<Page<HmeProductionQueryDTO>> queryProductDetails(@PathVariable("organizationId") Long tenantId,
                                                                           PageRequest pageRequest, HmeProductionQueryVO params){
        return Results.success(hmeProLineDetailsService.queryProductDetails(tenantId, pageRequest, params));
    }

    @ApiModelProperty(value = "在制报表-运行/库存EO信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query-product-eo-list")
    public ResponseEntity<Page<HmeProductEoInfoVO>> queryProductEoList(@PathVariable("organizationId") Long tenantId,
                                                                          @ApiIgnore PageRequest pageRequest, HmeProductEoInfoVO params){
        return Results.success(hmeProLineDetailsService.queryProductEoList(tenantId, pageRequest, params));
    }


    @ApiModelProperty(value = "产量日明细-班次信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query-product-shift-list")
    public ResponseEntity<Page<HmeProductionLineDetailsDTO>> queryProductShiftList(@PathVariable("organizationId") Long tenantId,
                                                                                        @ApiIgnore PageRequest pageRequest, HmeProductionLineDetailsVO params) throws Exception {
        return Results.success(hmeProLineDetailsService.queryProductShiftList(tenantId, pageRequest, params));
    }

    @ApiModelProperty(value = "产量日明细-投产信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query-product-process-eo-list")
    public ResponseEntity<Page<HmeProductEoInfoVO>> queryProductProcessEoList(@PathVariable("organizationId") Long tenantId,
                                                                              @ApiIgnore PageRequest pageRequest, HmeProductEoInfoVO params) {
        return Results.success(hmeProLineDetailsService.queryProductProcessEoList(tenantId, pageRequest, params));
    }

    @ApiOperation(value = "在制查询导出")
    @GetMapping(value = "/online-report-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> onlineReportExport(@PathVariable("organizationId") Long tenantId,
                                                HmeProductionQueryVO params, HttpServletResponse response) {
        try {
            hmeProLineDetailsService.onlineReportExport(tenantId, params, response);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
        return Results.success();
    }
}
