package com.ruike.reports.api.controller.v1;

import com.ruike.reports.api.dto.HmeSsnInspectResultDTO;
import com.ruike.reports.domain.repository.HmeSsnInspectResultRepository;
import com.ruike.reports.domain.vo.HmeSsnInspectResultHeaderLinesVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 标准件检验结果查询 API管理
 *
 * @author wenqiang.yin@hand-china.com 2021/02/04 8:36
 */
@RestController("hmeSsnInspectResultColltroller.v1")
@RequestMapping("/v1/{organizationId}/ssn-inspect-result")
@Api(tags = "HmeSsnInspectResult")
public class HmeSsnInspectResultColltroller extends BaseController {

    @Autowired
    private HmeSsnInspectResultRepository hmeSsnInspectResultRepository;

    @ApiOperation(value = "标准件检验结果头-行数据查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue
    @PostMapping("/header-lines")
    public ResponseEntity<Page<HmeSsnInspectResultHeaderLinesVO>> listHeaderLines(@PathVariable("organizationId") Long tenantId,
                                                                                    @RequestBody HmeSsnInspectResultDTO dto,
                                                                                  PageRequest pageRequest) {
        Page<HmeSsnInspectResultHeaderLinesVO> listHeaderLines = hmeSsnInspectResultRepository.pageHeaderLinesList(tenantId, dto, pageRequest);
        return Results.success(listHeaderLines);
    }

    @ApiOperation(value = "标准件检验结果头-行数据查询导出")
    @GetMapping(value = "/header-lines/export")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeSsnInspectResultHeaderLinesVO.class)
    public ResponseEntity<List<HmeSsnInspectResultHeaderLinesVO>> listExport(@PathVariable("organizationId") Long tenantId,
                                                                             HmeSsnInspectResultDTO dto,
                                                                             ExportParam exportParam,
                                                                             HttpServletResponse response) {
        this.validObject(dto);
        return Results.success(hmeSsnInspectResultRepository.listExport(tenantId, dto));
    }
}
