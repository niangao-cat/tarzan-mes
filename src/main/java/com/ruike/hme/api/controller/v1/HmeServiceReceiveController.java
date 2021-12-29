package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeServiceReceiveDTO;
import com.ruike.hme.api.dto.HmeServiceReceiveDTO2;
import com.ruike.hme.api.dto.HmeServiceReceiveDTO4;
import com.ruike.hme.app.service.HmeServiceReceiveService;
import com.ruike.hme.domain.entity.HmeServiceReceive;
import com.ruike.hme.domain.repository.HmeServiceReceiveRepository;
import com.ruike.hme.domain.vo.HmeServiceReceiveVO;
import com.ruike.hme.domain.vo.HmeServiceReceiveVO2;
import com.ruike.hme.domain.vo.HmeServiceReceiveVO3;
import io.swagger.annotations.Api;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
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
 * 营销服务部接收拆箱登记表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-09-01 14:14:21
 */
@RestController("hmeServiceReceiveController.v1")
@RequestMapping("/v1/{organizationId}/hme-service-receives")
@Api(tags = SwaggerApiConfig.HME_SERVICE_RECEIVE)
public class HmeServiceReceiveController extends BaseController {

    @Autowired
    private HmeServiceReceiveService hmeServiceReceiveService;

    @ApiOperation(value = "扫描物流单号")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scan/logistics/number")
    public ResponseEntity<HmeServiceReceiveVO> scanlogisticsNumber(@PathVariable(value = "organizationId") Long tenantId,
                                                                   String logisticsNumber) {
        return Results.success(hmeServiceReceiveService.scanlogisticsNumber(tenantId, logisticsNumber));
    }

    @ApiOperation(value = "售后接收部门下拉框")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/receive/department")
    public ResponseEntity<List<LovValueDTO>> receiveDepartment(@PathVariable(value = "organizationId") Long tenantId) {
        return Results.success(hmeServiceReceiveService.receiveDepartment(tenantId));
    }

    @ApiOperation(value = "扫描SN")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scan/sn")
    public ResponseEntity<HmeServiceReceiveVO2> scanSn(@PathVariable(value = "organizationId") Long tenantId,
                                                       HmeServiceReceiveDTO4 dto) {
        return Results.success(hmeServiceReceiveService.scanSn(tenantId, dto));
    }

    @ApiOperation(value = "物料LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/material")
    public ResponseEntity<Page<HmeServiceReceiveVO3>> materialLovQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                       HmeServiceReceiveDTO dto, PageRequest pageRequest) {
        return Results.success(hmeServiceReceiveService.materialLovQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "确认保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/confirm"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeServiceReceiveDTO2> confirm(@PathVariable(value = "organizationId") Long tenantId,
                                                         @RequestBody HmeServiceReceiveDTO2 dto) {
        return Results.success(hmeServiceReceiveService.confirm(tenantId, dto));
    }
}
