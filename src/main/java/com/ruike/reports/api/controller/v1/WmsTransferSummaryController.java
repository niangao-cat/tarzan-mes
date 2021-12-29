package com.ruike.reports.api.controller.v1;

import com.ruike.reports.api.dto.WmsTransferSummaryQueryDTO;
import com.ruike.reports.api.dto.WmsTransferSummaryQueryDTO2;
import com.ruike.reports.app.service.WmsTransferSummaryService;
import com.ruike.reports.domain.repository.WmsTransferSummaryRepository;
import com.ruike.reports.domain.vo.WmsTransferSummaryVO;
import com.ruike.reports.domain.vo.WmsTransferSummaryVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

/**
 * <p>
 * 调拨汇总报表 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/23 15:28
 */
@RestController("wmsTransferSummaryController.v1")
@RequestMapping("/v1/{organizationId}/transfer-summary")
@Api(tags = "WmsTransferSummary")
public class WmsTransferSummaryController {
    private final WmsTransferSummaryRepository wmsTransferSummaryRepository;
    private final WmsTransferSummaryService wmsTransferSummaryService;

    public WmsTransferSummaryController(WmsTransferSummaryRepository wmsTransferSummaryRepository, WmsTransferSummaryService wmsTransferSummaryService) {
        this.wmsTransferSummaryRepository = wmsTransferSummaryRepository;
        this.wmsTransferSummaryService = wmsTransferSummaryService;
    }

    @ApiOperation(value = "调拨汇总报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsTransferSummaryVO>> list(@PathVariable("organizationId") Long tenantId,
                                                           WmsTransferSummaryQueryDTO dto,
                                                           @ApiIgnore PageRequest pageRequest) {
        Page<WmsTransferSummaryVO> list = wmsTransferSummaryRepository.pageList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "调拨汇总报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(WmsTransferSummaryVO.class)
    public ResponseEntity<Page<WmsTransferSummaryVO>> export(@PathVariable("organizationId") Long tenantId,
                                                             WmsTransferSummaryQueryDTO dto,
                                                             ExportParam exportParam,
                                                             HttpServletResponse response,
                                                             @ApiIgnore PageRequest pageRequest) {
        Page<WmsTransferSummaryVO> list = wmsTransferSummaryService.export(tenantId, dto, exportParam, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "调拨详情报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/detail")
    public ResponseEntity<Page<WmsTransferSummaryVO2>> selectDetailList(@PathVariable("organizationId") Long tenantId,
                                                            WmsTransferSummaryQueryDTO2 dto,
                                                            @ApiIgnore PageRequest pageRequest) {
        Page<WmsTransferSummaryVO2> list = wmsTransferSummaryRepository.selectDetailList(tenantId, dto, pageRequest);
        return Results.success(list);
    }
}
