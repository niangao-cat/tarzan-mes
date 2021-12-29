package com.ruike.hme.api.controller.v1;

import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobLotMaterialDTO;
import com.ruike.hme.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tarzan.config.SwaggerApiConfig;
import com.ruike.hme.domain.repository.HmeEoJobLotMaterialRepository;

/**
 * 工序作业平台-投料 管理 API
 *
 * @author liyuan.lv@hand-china.com 2020-03-21 17:55:01
 */
@Slf4j
@RestController("hmeEoJobLotMaterialController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-lot-material")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_LOT_MATERIAL)
public class HmeEoJobLotMaterialController extends BaseController {

    @Autowired
    private HmeEoJobLotMaterialRepository repository;

    @ApiOperation(value = "批次物料投料扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-scan")
    public ResponseEntity<List<HmeEoJobLotMaterialVO>> releaseScan(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobLotMaterialController.releaseScan tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.releaseScan(tenantId, dto));
    }

    @ApiOperation(value = "删除批次物料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete-lot-material")
    public ResponseEntity<List<HmeEoJobLotMaterialVO>> deleteJobSnLotMaterial(@PathVariable("organizationId") Long tenantId,
                                                                              @RequestBody HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobLotMaterialController.deleteJobSnLotMaterial tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.deleteLotMaterial(tenantId, dto));
    }

    @ApiOperation(value = "更新是否投料标识")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update-is-released")
    public ResponseEntity<HmeEoJobLotMaterialVO> updateIsReleased(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody HmeEoJobLotMaterialVO dto) {
        log.info("<====== HmeEoJobLotMaterialController.updateIsReleased tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.updateIsReleased(tenantId, dto));
    }

    @ApiOperation(value = "批量更新是否投料标识")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-update-is-released")
    public ResponseEntity<List<HmeEoJobLotMaterialVO>> batchUpdateIsReleased(@PathVariable("organizationId") Long tenantId,
                                                                             @RequestBody List<HmeEoJobLotMaterialVO> dtoList) {
        log.info("<====== HmeEoJobLotMaterialController.batchUpdateIsReleased tenantId={},dtoList={}", tenantId, dtoList);
        return Results.success(this.repository.batchUpdateIsReleased(tenantId, dtoList));
    }

    @ApiOperation(value = "更新条码投料量")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update-release-qty")
    public ResponseEntity<HmeEoJobLotMaterialVO> updateRleaseQty(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody HmeEoJobLotMaterialVO dto) {
        log.info("<====== HmeEoJobLotMaterialController.updateRleaseQty tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.updateReleaseQty(tenantId, dto));
    }

    @ApiOperation(value = "批次物料查询")
    @PostMapping(value = {"/job-lot-material-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEoJobLotMaterialVO>> jobLotMaterialQuery(@PathVariable("organizationId") Long tenantId,  @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnController.jobLotMaterialQuery tenantId={},dto={}", tenantId, dto);
        HmeEoJobMaterialVO jobLotCondition = new HmeEoJobMaterialVO();
        jobLotCondition.setWorkcellId(dto.getWorkcellId());
        jobLotCondition.setJobId(dto.getJobId());
        jobLotCondition.setEoId(dto.getEoId());
        jobLotCondition.setOperationId(dto.getOperationId());
        jobLotCondition.setJobType(dto.getJobType());
        jobLotCondition.setEoStepId(dto.getEoStepId());
        jobLotCondition.setWorkOrderId(dto.getWorkOrderId());
        jobLotCondition.setMaterialCode(dto.getMaterialCode());
        jobLotCondition.setMaterialId(dto.getMaterialId());
        jobLotCondition.setSiteId(dto.getSiteId());
        jobLotCondition.setPrepareQty(dto.getPrepareQty());
        jobLotCondition.setReworkFlag(dto.getReworkFlag());
        return Results.success(repository.matchedJobLotMaterialQuery(tenantId, jobLotCondition,null));
    }
}
