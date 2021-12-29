package com.ruike.hme.api.controller.v1;

import java.util.List;

import com.ruike.hme.api.dto.HmeEoWorkingDTO;
import com.ruike.hme.domain.repository.HmeEoWorkingRepository;
import com.ruike.hme.domain.vo.HmeEoWorkingVO;
import com.ruike.hme.domain.vo.HmeModAreaVO2;
import com.ruike.hme.domain.vo.HmeProductionLineVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tarzan.config.SwaggerApiConfig;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工序在制查询 管理 API
 *
 * @author liyuan.lv@hand-china.com 2020/04/24 15:54
 */
@RestController("hmeEoWorkingController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-working")
@Api(tags = SwaggerApiConfig.HME_EO_WORKING)
public class HmeEoWorkingController extends BaseController {
    @Autowired
    private HmeEoWorkingRepository repository;

    @ApiOperation(value = "工序在制查询")
    @GetMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEoWorkingVO>> queryForEoWorking(@PathVariable("organizationId") Long tenantId,
                                                                HmeEoWorkingDTO dto) {
        return Results.success(repository.queryForEoWorkingNew(tenantId, dto));
    }

    @ApiOperation(value = "获取车间")
    @GetMapping(value = "/workshop")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeModAreaVO2>> queryForWorkshop(@PathVariable("organizationId") Long tenantId,
                                                               HmeEoWorkingDTO dto) {
        return Results.success(repository.queryForWorkshop(tenantId, dto));
    }

    @ApiOperation(value = "获取生产线")
    @GetMapping(value = "/production-line")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeProductionLineVO>> queryForProductionLine(@PathVariable("organizationId") Long tenantId,
                                                                            HmeEoWorkingDTO dto) {
        return Results.success(repository.queryForProductionLine(tenantId, dto));
    }

}
