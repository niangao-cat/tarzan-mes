package com.ruike.hme.api.controller.v1;

import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobTimeMaterialDTO;
import com.ruike.hme.domain.vo.HmeEoJobLotMaterialVO;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
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
import com.ruike.hme.domain.repository.HmeEoJobTimeMaterialRepository;
import com.ruike.hme.domain.vo.HmeEoJobTimeMaterialVO;
import com.ruike.hme.domain.vo.HmeEoJobMaterialVO;

/**
 * 工序作业平台-时效投料 管理 API
 *
 * @author liyuan.lv@hand-china.com 2020-03-22 17:08:55
 */
@Slf4j
@RestController("hmeEoJobTimeMaterialController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-time-material")
@Api(tags = SwaggerApiConfig.HME_EO_JOB_TIME_MATERIAL)
public class HmeEoJobTimeMaterialController extends BaseController {

    @Autowired
    private HmeEoJobTimeMaterialRepository repository;

    @ApiOperation(value = "时效投料扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/release-scan")
    public ResponseEntity<List<HmeEoJobTimeMaterialVO>> releaseScan(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobTimeMaterialController.releaseScan tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.releaseScan(tenantId, dto));
    }

    @ApiOperation(value = "删除时效物料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete-time-material")
    public ResponseEntity<List<HmeEoJobTimeMaterialVO>> deleteJobSnTimeMaterial(@PathVariable("organizationId") Long tenantId,
                                                                              @RequestBody HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobTimeMaterialController.deleteJobSnTimeMaterial tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.deleteTimeMaterial(tenantId, dto));
    }

    @ApiOperation(value = "更新是否投料标识")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update-is-released")
    public ResponseEntity<HmeEoJobTimeMaterialVO> updateIsReleased(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestBody HmeEoJobTimeMaterialVO dto) {
        log.info("<====== HmeEoJobTimeMaterialController.updateIsReleased tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.updateIsReleased(tenantId, dto));
    }

    @ApiOperation(value = "批量更新是否投料标识")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/batch-update-is-released")
    public ResponseEntity<List<HmeEoJobTimeMaterialVO>> updateIsReleased(@PathVariable("organizationId") Long tenantId,
                                                                         @RequestBody List<HmeEoJobTimeMaterialVO> dtoList) {
        log.info("<====== HmeEoJobTimeMaterialController.updateIsReleased tenantId={},dtoList={}", tenantId, dtoList);
        return Results.success(this.repository.batchUpdateIsReleased(tenantId, dtoList));
    }

    @ApiOperation(value = "更新条码投料量")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update-release-qty")
    public ResponseEntity<HmeEoJobTimeMaterialVO> updateRleaseQty(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody HmeEoJobTimeMaterialVO dto) {
        log.info("<====== HmeEoJobTimeMaterialController.updateRleaseQty tenantId={},dto={}", tenantId, dto);
        return Results.success(this.repository.updateReleaseQty(tenantId, dto));
    }

    @ApiOperation(value = "批次物料查询")
    @PostMapping(value = {"/job-time-material-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEoJobTimeMaterialVO>> jobTimeMaterialQuery(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnController.jobTimeMaterialQuery tenantId={},dto={}", tenantId, dto);
        HmeEoJobMaterialVO jobTimeCondition = new HmeEoJobMaterialVO();
        jobTimeCondition.setWorkcellId(dto.getWorkcellId());
        jobTimeCondition.setJobId(dto.getJobId());
        jobTimeCondition.setEoId(dto.getEoId());
        jobTimeCondition.setOperationId(dto.getOperationId());
        jobTimeCondition.setJobType(dto.getJobType());
        jobTimeCondition.setEoStepId(dto.getEoStepId());
        jobTimeCondition.setWorkOrderId(dto.getWorkOrderId());
        jobTimeCondition.setMaterialCode(dto.getMaterialCode());
        jobTimeCondition.setMaterialId(dto.getMaterialId());
        jobTimeCondition.setSiteId(dto.getSiteId());
        jobTimeCondition.setPrepareQty(dto.getPrepareQty());
        jobTimeCondition.setReworkFlag(dto.getReworkFlag());
        return Results.success(repository.matchedJobTimeMaterialQuery(tenantId, jobTimeCondition,null));
    }
}
