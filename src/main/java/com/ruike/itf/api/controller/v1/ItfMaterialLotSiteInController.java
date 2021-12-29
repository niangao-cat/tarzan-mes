package com.ruike.itf.api.controller.v1;

import com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO;
import com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO3;
import com.ruike.itf.api.dto.ItfMaterialLotSiteDTO;
import com.ruike.itf.api.dto.ItfMaterialLotSiteDTO2;
import com.ruike.itf.app.service.ItfMaterialLotSiteInService;
import com.ruike.itf.infra.constant.ItfConstant;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ItfMaterialLotSiteInController
 * 盒子进站、出站管理 API
 * @author: chaonan.hu@hand-china.com 2021-10-12 13:53:12
 **/
@RestController("itfMaterialLotSiteInController.v1")
@RequestMapping("/v1/{organizationId}/itf-material-lot-sitein")
@Slf4j
public class ItfMaterialLotSiteInController {

    @Autowired
    private ItfMaterialLotSiteInService itfMaterialLotSiteInService;

    @ApiOperation(value = "盒子进站接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/site-in")
    public ResponseEntity<ItfMaterialLotSiteInVO> materialLotSiteIn(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody ItfMaterialLotSiteDTO dto) {
        log.info("<====ItfMaterialLotSiteInController-materialLotSiteIn.start:dto={}", dto);
        long startDate = System.currentTimeMillis();
        ItfMaterialLotSiteInVO result = new ItfMaterialLotSiteInVO();
        try {
            result = itfMaterialLotSiteInService.materialLotSiteIn(tenantId, dto);
            result.setProcessStatus(ItfConstant.ProcessStatus.SUCCESS);
            result.setProcessMessage("成功");
        }catch (MtException ex){
            result.setProcessStatus(ItfConstant.ProcessStatus.ERROR);
            result.setProcessMessage(ex.getMessage());
        }
        long endDate = System.currentTimeMillis();
        log.info("<====ItfMaterialLotSiteInController-materialLotSiteIn.end:result{}。总耗时：{}毫秒", result, (endDate - startDate));
        return Results.success(result);
    }

    @ApiOperation(value = "盒子出站接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/site-out")
    public ResponseEntity<ItfMaterialLotSiteInVO3> materialLotSiteOut(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody ItfMaterialLotSiteDTO2 dto) {
        log.info("<====ItfMaterialLotSiteInController-materialLotSiteOut.start:dto={}", dto);
        long startDate = System.currentTimeMillis();
        ItfMaterialLotSiteInVO3 result = new ItfMaterialLotSiteInVO3();
        try {
            result = itfMaterialLotSiteInService.materialLotSiteOut(tenantId, dto);
        }catch (Exception ex){
            result.setProcessStatus(ItfConstant.ProcessStatus.ERROR);
            result.setProcessMessage(ex.getMessage());
        }
        long endDate = System.currentTimeMillis();
        log.info("<====ItfMaterialLotSiteInController-materialLotSiteOut.end:result{}。总耗时：{}毫秒", result, (endDate - startDate));
        return Results.success(result);
    }
}
