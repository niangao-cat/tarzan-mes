package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeLogisticsInfoDTO;
import com.ruike.hme.app.service.HmeLogisticsInfoService;
import com.ruike.hme.domain.vo.HmeLogisticsInfoVO;
import io.swagger.annotations.Api;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeLogisticsInfo;
import com.ruike.hme.domain.repository.HmeLogisticsInfoRepository;
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

import java.util.List;

/**
 * 物流信息表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-08-31 11:06:23
 */
@RestController("hmeLogisticsInfoController.v1")
@RequestMapping("/v1/{organizationId}/hme-logistics-infos")
@Api(tags = SwaggerApiConfig.HME_LOGISTICS_INFO)
public class HmeLogisticsInfoController extends BaseController {

    @Autowired
    private HmeLogisticsInfoService hmeLogisticsInfoService;

    @ApiOperation(value = "物流公司查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/logistics/query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<LovValueDTO>> logisticsCompanyQuery(@PathVariable("organizationId") Long tenantId) {
        return Results.success(hmeLogisticsInfoService.logisticsCompanyQuery(tenantId));
    }

    @ApiOperation(value = "扫描物流单号")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/scan/logistics/number", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeLogisticsInfoVO> scanLogisticsNum(@PathVariable("organizationId") Long tenantId, String logisticsNumber) {
        return Results.success(hmeLogisticsInfoService.scanLogisticsNum(tenantId, logisticsNumber));
    }

    @ApiOperation(value = "确认接收")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/confirm/receive"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeLogisticsInfoDTO>> confirmReceive(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody List<HmeLogisticsInfoDTO> dtoList) {
        return Results.success(hmeLogisticsInfoService.confirmReceive(tenantId, dtoList));
    }
}
