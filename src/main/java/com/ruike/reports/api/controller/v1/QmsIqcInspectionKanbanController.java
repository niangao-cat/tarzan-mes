package com.ruike.reports.api.controller.v1;

import com.ruike.reports.api.dto.QmsIqcInspectionKanbanQueryDTO;
import com.ruike.reports.api.dto.QmsSupplierQualityQueryDTO;
import com.ruike.reports.domain.repository.QmsIqcInspectionKanbanRepository;
import com.ruike.reports.domain.vo.ChartsSquareResultVO;
import com.ruike.reports.domain.vo.QmsIqcInspectionKanbanVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

import static tarzan.config.SwaggerApiConfig.QMS_IQC_INSPECTION_KANBAN;

/**
 * <p>
 * IQC检验看板 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 11:05
 */
@RestController("qmsIqcInspectionKanbanController.v1")
@RequestMapping("/v1/{organizationId}/iqc-inspection-kanban")
@Api(tags = QMS_IQC_INSPECTION_KANBAN)
public class QmsIqcInspectionKanbanController {
    private final QmsIqcInspectionKanbanRepository qmsIqcInspectionKanbanRepository;

    public QmsIqcInspectionKanbanController(QmsIqcInspectionKanbanRepository qmsIqcInspectionKanbanRepository) {
        this.qmsIqcInspectionKanbanRepository = qmsIqcInspectionKanbanRepository;
    }

    @ApiOperation(value = "IQC日常工作计划报表 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/kanban")
    public ResponseEntity<Page<QmsIqcInspectionKanbanVO>> kanbanPage(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody QmsIqcInspectionKanbanQueryDTO dto,
                                                                     @ApiIgnore PageRequest pageRequest) {
        Page<QmsIqcInspectionKanbanVO> list = qmsIqcInspectionKanbanRepository.pagedKanbanList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "IQC日常工作计划报表 列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/kanban/list")
    public ResponseEntity<List<QmsIqcInspectionKanbanVO>> kanbanList(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody QmsIqcInspectionKanbanQueryDTO dto) {
        List<QmsIqcInspectionKanbanVO> list = qmsIqcInspectionKanbanRepository.kanbanListGet(tenantId, dto);
        return Results.success(list);
    }

    @ApiOperation(value = "供应商来料在线质量 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/quality")
    public ResponseEntity<Page<QmsIqcInspectionKanbanVO>> qualityPage(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody QmsSupplierQualityQueryDTO dto,
                                                                      @ApiIgnore PageRequest pageRequest) {
        Page<QmsIqcInspectionKanbanVO> list = qmsIqcInspectionKanbanRepository.pagedQualityList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "供应商来料在线质量 列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/quality/list")
    public ResponseEntity<List<QmsIqcInspectionKanbanVO>> qualityList(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody QmsSupplierQualityQueryDTO dto) {
        List<QmsIqcInspectionKanbanVO> list = qmsIqcInspectionKanbanRepository.qualityListGet(tenantId, dto);
        return Results.success(list);
    }

    @ApiOperation(value = "供应商来料在线质量 图表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/quality/chart")
    public ResponseEntity<ChartsSquareResultVO> qualityChartMap(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody QmsSupplierQualityQueryDTO dto) {
        ChartsSquareResultVO result = qmsIqcInspectionKanbanRepository.qualityChartMapGet(tenantId, dto);
        return Results.success(result);
    }
}
