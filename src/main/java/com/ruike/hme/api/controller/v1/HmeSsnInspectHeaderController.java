package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeSsnInspectHeaderService;
import com.ruike.hme.domain.entity.HmeSsnInspectHeader;
import com.ruike.hme.domain.repository.HmeSsnInspectDetailRepository;
import com.ruike.hme.domain.repository.HmeSsnInspectHeaderRepository;
import com.ruike.hme.domain.repository.HmeSsnInspectLineRepository;
import com.ruike.hme.domain.vo.HmeSsnInspectExportVO;
import com.ruike.hme.domain.vo.HmeSsnInspectHeaderVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 标准件检验标准头 管理 API
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:11
 */
@RestController("hmeSsnInspectHeaderController.v1")
@RequestMapping("/v1/{organizationId}/hme-ssn-inspect-headers")
@Api(tags = SwaggerApiConfig.HME_SSN_INSPECT_HEADER)
public class HmeSsnInspectHeaderController extends BaseController {

    @Autowired
    private HmeSsnInspectHeaderRepository hmeSsnInspectHeaderRepository;
    @Autowired
    private HmeSsnInspectHeaderService hmeSsnInspectHeaderService;
    @Autowired
    private HmeSsnInspectLineRepository hmeSsnInspectLineRepository;
    @Autowired
    private HmeSsnInspectDetailRepository hmeSsnInspectDetailRepository;

    @ApiOperation(value = "标准件检验标准头列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeSsnInspectHeaderVO>> list(@PathVariable("organizationId") Long tenantId,
                                                            HmeSsnInspectHeader hmeSsnInspectHeader,
                                                            PageRequest pageRequest) {
        Page<HmeSsnInspectHeaderVO> list = hmeSsnInspectHeaderRepository.selectHeader(tenantId,pageRequest, hmeSsnInspectHeader);
        return Results.success(list);
    }

    @ApiOperation(value = "创建标准件检验标准头")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/insert")
    public ResponseEntity<HmeSsnInspectHeader> create(@PathVariable("organizationId") Long tenantId,
                                                      HmeSsnInspectHeader hmeSsnInspectHeader) {
        hmeSsnInspectHeaderService.createHeader(tenantId,hmeSsnInspectHeader);
        return Results.success(hmeSsnInspectHeader);
    }

    @ApiOperation(value = "修改标准件检验标准头")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update")
    public ResponseEntity<HmeSsnInspectHeader> update(@PathVariable("organizationId") Long tenantId,
                                                      HmeSsnInspectHeader hmeSsnInspectHeader) {
        hmeSsnInspectHeaderService.updateHeader(tenantId,hmeSsnInspectHeader);
        return Results.success(hmeSsnInspectHeader);
    }

    @ApiOperation(value = "删除标准件检验标准头")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> remove(@PathVariable("organizationId") Long tenantId,
                                    HmeSsnInspectHeader hmeSsnInspectHeader) {
        hmeSsnInspectDetailRepository.deleteDetailByHeader(tenantId, hmeSsnInspectHeader.getSsnInspectHeaderId());
        hmeSsnInspectLineRepository.deleteLineByHeade(tenantId, hmeSsnInspectHeader.getSsnInspectHeaderId());
        hmeSsnInspectHeaderRepository.deleteByPrimaryKey(hmeSsnInspectHeader.getSsnInspectHeaderId());
        return Results.success();
    }

    @ApiOperation(value = "标准件检验标准头历史列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/ssn-inspect-header-his-query")
    public ResponseEntity<Page<HmeSsnInspectHeaderVO>> ssnInspectHeaderHisQuery(@PathVariable("organizationId") Long tenantId,
                                                                                String ssnInspectHeaderId,
                                                                                @ApiIgnore PageRequest pageRequest) {
        Page<HmeSsnInspectHeaderVO> list = hmeSsnInspectHeaderRepository.ssnInspectHeaderHisQuery(tenantId, ssnInspectHeaderId, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "标准件检验标准导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/ssn-inspect-export")
    @ExcelExport(HmeSsnInspectExportVO.class)
    public ResponseEntity<List<HmeSsnInspectExportVO>> ssnInspectExport(@PathVariable("organizationId") Long tenantId,
                                                                        HmeSsnInspectHeader hmeSsnInspectHeader,
                                                                        HttpServletResponse response,
                                                                        ExportParam exportParam) {
        return Results.success(hmeSsnInspectHeaderRepository.ssnInspectExport(tenantId, hmeSsnInspectHeader));
    }

}
