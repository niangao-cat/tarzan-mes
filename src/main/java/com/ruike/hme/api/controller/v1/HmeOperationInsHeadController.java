package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeOperationInsHeadService;
import com.ruike.hme.domain.vo.HmeOperationInsHeadVO;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeOperationInsHead;
import com.ruike.hme.domain.repository.HmeOperationInsHeadRepository;
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

/**
 * 作业指导头表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-11-09 19:09:39
 */
@RestController("hmeOperationInsHeadController.v1")
@RequestMapping("/v1/{organizationId}/hme-operation-ins-heads")
@Api(tags = SwaggerApiConfig.HME_OPERATION_INS_HEAD)
public class HmeOperationInsHeadController extends BaseController {

    @Autowired
    private HmeOperationInsHeadRepository hmeOperationInsHeadRepository;

    @Autowired
    private HmeOperationInsHeadService hmeOperationInsHeadService;

    @ApiOperation(value = "作业指导头表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeOperationInsHeadDTO2>> list(HmeOperationInsHeadDTO dto,
                                                          @PathVariable(value = "organizationId") Long tenantId,
                                                              PageRequest pageRequest) {
        Page<HmeOperationInsHeadDTO2> list = hmeOperationInsHeadService.listForUi(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "作业指导头表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/detail")
    public ResponseEntity<HmeOperationInsHeadDTO2> detail(HmeOperationInsHeadDTO dto,
                                                      @PathVariable(value = "organizationId") Long tenantId) {
        return Results.success(hmeOperationInsHeadService.detailListForUi(tenantId, dto));
    }

    @ApiOperation(value = "创建作业指导头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save")
    public ResponseEntity<HmeOperationInsHead> saveForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                         @RequestBody HmeOperationInsHead hmeOperationInsHead) {
        validObject(hmeOperationInsHead);
        hmeOperationInsHead.setTenantId(tenantId);
        return Results.success(hmeOperationInsHeadService.saveForUi(tenantId, hmeOperationInsHead));
    }

    @ApiOperation(value = "作业指导头表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/esop-query")
    public ResponseEntity<Page<HmeOperationInsHeadVO>> eSopQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                 HmeOperationInsHeadDTO3 dto, @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeOperationInsHeadService.eSopQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工位未出站eo信息查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/no-out-eo")
    public ResponseEntity<Page<HmeEoJobSnDTO5>> noSiteOutEoQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                 HmeEoJobSnDTO4 dto, PageRequest pageRequest) {
        return Results.success(hmeOperationInsHeadService.noSiteOutEoQuery(tenantId, dto, pageRequest));
    }
}
