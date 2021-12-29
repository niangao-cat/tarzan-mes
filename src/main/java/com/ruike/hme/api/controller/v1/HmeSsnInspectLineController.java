package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeSsnInspectLineService;
import com.ruike.hme.domain.repository.HmeSsnInspectDetailRepository;
import com.ruike.hme.domain.vo.HmeSsnInspectLineVO;
import com.ruike.hme.infra.mapper.HmeSsnInspectDetailMapper;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeSsnInspectLine;
import com.ruike.hme.domain.repository.HmeSsnInspectLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
 * 标准件检验标准行 管理 API
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:10
 */
@RestController("hmeSsnInspectLineController.v1")
@RequestMapping("/v1/{organizationId}/hme-ssn-inspect-lines")
@Api(tags = SwaggerApiConfig.HME_SSN_INSPECT_LINE)
public class HmeSsnInspectLineController extends BaseController {

    @Autowired
    private HmeSsnInspectLineRepository hmeSsnInspectLineRepository;
    @Autowired
    private HmeSsnInspectLineService hmeSsnInspectLineService;
    @Autowired
    private HmeSsnInspectDetailRepository hmeSsnInspectDetailRepository;

    @ApiOperation(value = "标准件检验标准行列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeSsnInspectLineVO>> list(@PathVariable("organizationId") Long tenantId,
                                                          String ssnInspectHeaderId,
                                                          PageRequest pageRequest) {
        Page<HmeSsnInspectLineVO> list = hmeSsnInspectLineRepository.selectLine(tenantId, pageRequest, ssnInspectHeaderId);
        return Results.success(list);
    }

    @ApiOperation(value = "创建标准件检验标准行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/insert")
    public ResponseEntity<List<HmeSsnInspectLine>> create(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody List<HmeSsnInspectLine> hmeSsnInspectLine) {
        hmeSsnInspectLineService.createLine(tenantId,hmeSsnInspectLine);
        return Results.success(hmeSsnInspectLine);
    }

    @ApiOperation(value = "修改标准件检验标准行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update")
    public ResponseEntity<List<HmeSsnInspectLine>> update(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody List<HmeSsnInspectLine> hmeSsnInspectLine) {
        hmeSsnInspectLineService.updateLine(tenantId,hmeSsnInspectLine);
        return Results.success(hmeSsnInspectLine);
    }

    @ApiOperation(value = "删除标准件检验标准行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> remove(@PathVariable("organizationId") Long tenantId,
                                    HmeSsnInspectLine hmeSsnInspectLine) {
        hmeSsnInspectDetailRepository.deleteByLine(tenantId, hmeSsnInspectLine.getSsnInspectLineId());
        hmeSsnInspectLineRepository.deleteByPrimaryKey(hmeSsnInspectLine.getSsnInspectLineId());
        return Results.success();
    }

    @ApiOperation(value = "标准件检验标准历史列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/ssn-inspect-line-his-query")
    public ResponseEntity<Page<HmeSsnInspectLineVO>> ssnInspectLineHisQuery(@PathVariable("organizationId") Long tenantId,
                                                                            String ssnInspectLineId,
                                                                            @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeSsnInspectLineRepository.ssnInspectLineHisQuery(tenantId, ssnInspectLineId, pageRequest));
    }

}
