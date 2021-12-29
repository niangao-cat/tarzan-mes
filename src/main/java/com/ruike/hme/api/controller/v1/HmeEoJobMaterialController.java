package com.ruike.hme.api.controller.v1;

import java.util.List;

import com.ruike.hme.domain.vo.HmeEoJobMaterialVO2;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ruike.hme.domain.repository.HmeEoJobMaterialRepository;
import com.ruike.hme.domain.vo.HmeEoJobMaterialVO;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tarzan.config.SwaggerApiConfig;

/**
 * 工序作业平台-投料 管理 API
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 21:41:23
 */
@Slf4j
@RestController("hmeEoJobMaterialController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-material")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_MATERIAL)
public class HmeEoJobMaterialController extends BaseController {

    @Autowired
    private HmeEoJobMaterialRepository repository;

    @ApiOperation(value = "序列物料投料扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-scan")
    public ResponseEntity<List<HmeEoJobMaterialVO>> releaseScan(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobMaterialController.releaseScan tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.releaseScan(tenantId, dto));
    }

    @ApiOperation(value = "删除序列物料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete-material")
    public ResponseEntity<List<HmeEoJobMaterialVO>> deleteMaterial(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobMaterialController.deleteMaterial tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.deleteMaterial(tenantId, dto));
    }

    @ApiOperation(value = "更新是否投料标识")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update-is-released")
    public ResponseEntity<HmeEoJobMaterialVO> updateIsReleased(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobMaterialController.updateIsReleased tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.updateIsReleased(tenantId, dto));
    }

    @ApiOperation(value = "批量更新是否投料标识")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-update-is-released")
    public ResponseEntity<List<HmeEoJobMaterialVO>> batchUpdateIsReleased(@PathVariable("organizationId") Long tenantId,
                                                                          @RequestBody List<HmeEoJobMaterialVO> dtoList) {
        log.info("<====== HmeEoJobMaterialController.batchUpdateIsReleased tenantId={},dto={}", tenantId, dtoList);
        return Results.success(this.repository.batchUpdateIsReleased(tenantId, dtoList));
    }

    @ApiOperation(value = "序列物料查询")
    @PostMapping(value = {"/job-material-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEoJobMaterialVO>> jobMaterialQuery(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnController.jobMaterialQuery tenantId={},dto={}", tenantId, dto);
        HmeEoJobMaterialVO2 jobMaterialCondition = new HmeEoJobMaterialVO2();
        jobMaterialCondition.setJobId(dto.getJobId());
        jobMaterialCondition.setWorkcellId(dto.getWorkcellId());
        jobMaterialCondition.setMaterialId(dto.getMaterialId());
        jobMaterialCondition.setSiteId(dto.getSiteId());
        jobMaterialCondition.setWorkOrderId(dto.getWorkOrderId());
        jobMaterialCondition.setJobType(dto.getJobType());
        jobMaterialCondition.setEoId(dto.getEoId());
        jobMaterialCondition.setOperationId(dto.getOperationId());
        jobMaterialCondition.setEoStepId(dto.getEoStepId());
        return Results.success(repository.jobSnLimitJobMaterialQuery(tenantId, jobMaterialCondition));
    }

    @ApiOperation(value = "解绑后绑定")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete-release-material")
    public ResponseEntity<List<HmeEoJobMaterialVO>> deleteReleaseMaterial(@PathVariable("organizationId") Long tenantId,
                                                                          @RequestBody HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobMaterialController.deleteReleaseMaterial tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.deleteReleaseMaterial(tenantId, dto));
    }
}
