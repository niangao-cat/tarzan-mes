package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeSsnInspectDetailService;
import com.ruike.hme.domain.vo.HmeSsnInspectDetailVO;
import com.ruike.hme.infra.mapper.HmeSsnInspectDetailMapper;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeSsnInspectDetail;
import com.ruike.hme.domain.repository.HmeSsnInspectDetailRepository;
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
 * 标准件检验标准明细 管理 API
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:11
 */
@RestController("hmeSsnInspectDetailController.v1")
@RequestMapping("/v1/{organizationId}/hme-ssn-inspect-details")
@Api(tags = SwaggerApiConfig.HME_SSN_INSPECT_DETAIL)
public class HmeSsnInspectDetailController extends BaseController {

    @Autowired
    private HmeSsnInspectDetailRepository hmeSsnInspectDetailRepository;
    @Autowired
    private HmeSsnInspectDetailService hmeSsnInspectDetailService;

    @ApiOperation(value = "标准件检验标准明细列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeSsnInspectDetailVO>> list(@PathVariable("organizationId") Long tenantId,
                                                            String ssnInspectLineId,
                                                            PageRequest pageRequest) {
        Page<HmeSsnInspectDetailVO> list = hmeSsnInspectDetailRepository.selectDetail(tenantId, pageRequest, ssnInspectLineId);
        return Results.success(list);
    }

    @ApiOperation(value = "创建标准件检验标准明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/insert")
    public ResponseEntity<List<HmeSsnInspectDetail>> create(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody List<HmeSsnInspectDetail> hmeSsnInspectDetail) {
        hmeSsnInspectDetailService.insertDetail(tenantId, hmeSsnInspectDetail);
        return Results.success(hmeSsnInspectDetail);
    }

    @ApiOperation(value = "修改标准件检验标准明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update")
    public ResponseEntity<List<HmeSsnInspectDetail>> update(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody List<HmeSsnInspectDetail> hmeSsnInspectDetail) {
        hmeSsnInspectDetailService.updateDetail(tenantId,hmeSsnInspectDetail);
        return Results.success(hmeSsnInspectDetail);
    }

    @ApiOperation(value = "删除标准件检验标准明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete")
    public ResponseEntity<?> remove(HmeSsnInspectDetail hmeSsnInspectDetail) {
        hmeSsnInspectDetailRepository.deleteByPrimaryKey(hmeSsnInspectDetail.getSsnInspectDetailId());
        return Results.success();
    }

}
