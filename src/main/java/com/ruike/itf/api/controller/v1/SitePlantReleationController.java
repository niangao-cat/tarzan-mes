package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.SitePlantReleationDTO;
import com.ruike.itf.domain.repository.SitePlantReleationRepository;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * WCS与WMS成品SN核对接口 管理 API
 *
 * @author taowen.wang@hand-china.com 2021-07-06 14:14:34
 */
@RestController("sitePlantReleationController.v1" )
@RequestMapping("/v1/{organizationId}/site-plant-releations" )
@Slf4j
public class SitePlantReleationController extends BaseController {
    @Autowired
    private SitePlantReleationRepository sitePlantReleationRepository;

    @ApiOperation(value = "WCS与WMS成品SN核对接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/sn")
    public ResponseEntity<SitePlantReleationDTO> sitePlantReleationByPlantCode(@PathVariable Long organizationId, @RequestBody String plantCode){
        long startTime = System.currentTimeMillis();
        log.info("<====【SitePlantReleationController-select】WCS与WMS成品SN核对接口列表开始时间: {},单位毫秒", startTime);
        SitePlantReleationDTO sitePlantReleationDTO = sitePlantReleationRepository.sitePlantReleationByPlantCode(organizationId,plantCode);
        log.info("<====【SitePlantReleationController-select】WCS与WMS成品SN核对接口列表结束时间: {},用时：{}单位毫秒", System.currentTimeMillis(), System.currentTimeMillis() - startTime);
        return Results.success(sitePlantReleationDTO);
    }

}
