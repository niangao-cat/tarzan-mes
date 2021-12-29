package com.ruike.reports.api.controller.v1;

import com.ruike.reports.api.dto.HmeCosWorkcellSummaryQueryDTO;
import com.ruike.reports.app.service.HmeCosWorkcellSummaryService;
import com.ruike.reports.domain.repository.HmeCosWorkcellSummaryRepository;
import com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * COS工位加工汇总 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 16:26
 */
@RestController("hmeCosWorkcellSummaryController.v1")
@RequestMapping("/v1/{organizationId}/cos-workcell-summary")
@Api(tags = "HmeCosWorkcellSummary")
public class HmeCosWorkcellSummaryController {
    private final HmeCosWorkcellSummaryRepository repository;
    private final HmeCosWorkcellSummaryService service;
    private final LovAdapter lovAdapter;

    public HmeCosWorkcellSummaryController(HmeCosWorkcellSummaryRepository repository, HmeCosWorkcellSummaryService service, LovAdapter lovAdapter) {
        this.repository = repository;
        this.service = service;
        this.lovAdapter = lovAdapter;
    }

    @ApiOperation(value = "COS工位加工汇总 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCosWorkcellSummaryVO>> list(@PathVariable("organizationId") Long tenantId,
                                                              HmeCosWorkcellSummaryQueryDTO dto,
                                                              @ApiIgnore PageRequest pageRequest) {
        dto.initParam(tenantId, lovAdapter);
        Page<HmeCosWorkcellSummaryVO> list = repository.pageList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "COS工位加工汇总 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeCosWorkcellSummaryVO.class)
    public ResponseEntity<List<HmeCosWorkcellSummaryVO>> export(@PathVariable("organizationId") Long tenantId,
                                                                HmeCosWorkcellSummaryQueryDTO dto,
                                                                ExportParam exportParam,
                                                                HttpServletResponse response) {
        dto.initParam(tenantId, lovAdapter);
        List<HmeCosWorkcellSummaryVO> list = service.export(tenantId, dto, exportParam);
        return Results.success(list);
    }
}
