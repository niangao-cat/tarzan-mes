package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEoJobDataCalculationResultDTO;
import com.ruike.hme.api.dto.HmeEoJobDataRecordDto;
import com.ruike.hme.api.dto.HmeEoJobDataRecordReturnDTO;
import com.ruike.hme.app.service.HmeEoJobDataRecordService;
import com.ruike.hme.domain.repository.HmeEoJobDataRecordRepository;
import com.ruike.hme.domain.vo.HmeEoJobDataRecordDetailVO;
import com.ruike.hme.domain.vo.HmeEoJobDataRecordVO;
import com.ruike.hme.domain.vo.HmeEoJobMaterialVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工序作业平台-数据采集 管理 API
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 21:48:09
 */
@Slf4j
@RestController("hmeEoJobDataRecordController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-data-record")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_DATA_RECORD)
public class HmeEoJobDataRecordController extends BaseController {

    @Autowired
    private HmeEoJobDataRecordRepository repository;
    @Autowired
    private HmeEoJobDataRecordService service;

    @ApiOperation(value = "采集数据扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/material-scan")
    public ResponseEntity<HmeEoJobDataRecordVO> materialScan(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmeEoJobDataRecordVO dto) {
        log.info("<====== HmeEoJobDataRecordController.materialScan tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.materialScan(tenantId, dto));
    }

    @ApiOperation(value = "单件工序作业平台-数据采集项结果保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/result-save-single")
    public ResponseEntity<HmeEoJobDataRecordVO> resultSaveForSingleProcess(@PathVariable("organizationId") Long tenantId,
                                                                           @RequestBody HmeEoJobDataRecordVO dto) {
        log.info("<====== HmeEoJobDataRecordController.resultSaveForSingleProcess tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.resultSaveForSingleProcess(tenantId, dto));
    }

    @ApiOperation(value = "补充采集数据保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-save")
    public ResponseEntity<List<HmeEoJobDataRecordVO>> batchSave(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody List<HmeEoJobDataRecordVO> dtoList) {
        log.info("<====== HmeEoJobDataRecordController.batchSave tenantId={},dtoList={}", tenantId, dtoList);
        return Results.success(this.repository.batchSave(tenantId, dtoList));
    }

    /**
     * 补充数据采集记录查询
     *
     * @author: penglin.sui@hand-china.com
     */
    @ApiOperation(value = "补充数据采集记录查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-supplement-record")
    public ResponseEntity<Page<HmeEoJobDataRecordVO>> supplementRecordQuery(@PathVariable("organizationId") Long tenantId,
                                                                            HmeEoJobDataRecordDto dto,
                                                                            @ApiIgnore PageRequest pageRequest) {
        log.info("<====== HmeEoJobDataRecordController.supplementRecordQuery tenantId={},dto={}", tenantId, dto);
        return Results.success(this.service.supplementRecordQuery(tenantId, pageRequest, dto));
    }

    /**
     * 删除补充数据采集记录
     *
     * @author: penglin.sui@hand-china.com
     */
    @ApiOperation(value = "删除补充数据采集记录")
    @PostMapping(value = {"/delete-supplement-record"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> deleteSupplementRecord(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody List<HmeEoJobDataRecordVO> deleteVO) {
        log.info("<====== HmeEoJobDataRecordController.deleteSupplementRecord tenantId={},deleteVO={}", tenantId, deleteVO);
        this.service.deleteSupplementRecord(tenantId, deleteVO);
        return Results.success();
    }

    @ApiOperation(value = "计算公式型数据项")
    @PostMapping(value = {"/calculation-formula-data"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> calculationFormulaData(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody HmeEoJobDataRecordVO dto) {
        log.info("<====== HmeEoJobDataRecordController.calculationFormulaData tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.calculationFormulaData(tenantId, dto));
    }

    @ApiOperation(value = "计算公式型数据项")
    @PostMapping(value = {"/batch-calculation-formula-data"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> batchCalculationFormulaData(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody List<HmeEoJobDataRecordVO> dtoList) {
        log.info("<====== HmeEoJobDataRecordController.batchCalculationFormulaData tenantId={},dtoList={}", tenantId, dtoList);
        return Results.success(this.repository.batchCalculationFormulaData(tenantId, dtoList));
    }

    @ApiOperation(value = "计算公式型数据项")
    @PostMapping(value = {"/batch-calculation-formula-data-for-repair"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> batchCalculationFormulaDataForRepair(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody List<HmeEoJobDataRecordVO> dtoList) {
        log.info("<====== HmeEoJobDataRecordController.batchCalculationFormulaData tenantId={},dtoList={}", tenantId, dtoList);
        return Results.success(this.repository.batchCalculationFormulaDataForRepair(tenantId, dtoList));
    }

    @ApiOperation(value = "首序作业平台数据项结果计算")
    @PostMapping(value = {"/first-process/calculation-result"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> calculationResultForFirstProcess(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody HmeEoJobDataCalculationResultDTO dto) {
        log.info("<====== HmeEoJobDataRecordController.calculationResultForFirstProcess tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.queryResultForFirstProcess(tenantId, dto));
    }

    /**
     * 数据采集记录查询
     *
     * @author: penglin.sui@hand-china.com
     */
    @ApiOperation(value = "数据采集记录查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-data-record")
    public ResponseEntity<List<HmeEoJobDataRecordVO>> queryDataRecord(@PathVariable("organizationId") Long tenantId,
                                                                      HmeEoJobMaterialVO2 jobDataRecordCondition) {
        log.info("<====== HmeEoJobDataRecordController.queryDataRecord tenantId={},dto={}", tenantId, jobDataRecordCondition);
        return Results.success(this.repository.eoJobDataRecordQuery(tenantId, jobDataRecordCondition));
    }

    @ApiOperation(value = "数据采集记录删除")
    @PostMapping(value = {"/delete-data-record"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> deleteDataRecord(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody List<String> jobDataRecordList) {
        log.info("<====== HmeEoJobDataRecordController.deleteDataRecord tenantId={},jobDataRecordList={}", tenantId, jobDataRecordList);
        this.repository.deleteDataRecordById(tenantId, jobDataRecordList);
        return Results.success();
    }

    @ApiOperation(value = "应用检机汇总报表 查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/mes-report/summary")
    public ResponseEntity<Page<HmeEoJobDataRecordReturnDTO>> summaryReport(@PathVariable(value = "organizationId") Long tenantId,
                                                                           String materialId,
                                                                           String flag,
                                                                           String operationCode,
                                                                           PageRequest pageRequest) {
        Page<HmeEoJobDataRecordReturnDTO> result = service.summaryReport(tenantId, materialId, flag, operationCode, pageRequest);
        return Results.success(result);
    }

    @ApiOperation(value = "应用检机汇总报表明细 查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/mes-report/detail")
    public ResponseEntity<Page<HmeEoJobDataRecordDetailVO>> detailReport(@PathVariable(value = "organizationId") Long tenantId,
                                                                         @RequestParam("snMaterialId") String snMaterialId,
                                                                         @RequestParam("operationId") String operationId,
                                                                         PageRequest pageRequest) {
        Page<HmeEoJobDataRecordDetailVO> result = repository.detailListGet(tenantId, snMaterialId, operationId, pageRequest);
        return Results.success(result);
    }

    @ApiOperation(value = "应用检机汇总报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/mes-report/summary-export")
    public ResponseEntity<?> summaryReportExport(@PathVariable(value = "organizationId") Long tenantId,
                                                 String materialId,
                                                 String flag,
                                                 String operationCode,
                                                 HttpServletResponse response) {
        service.summaryReportExport(tenantId, materialId, flag, operationCode, response);
        return Results.success();
    }
}


