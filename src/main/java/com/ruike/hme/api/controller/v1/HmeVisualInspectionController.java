package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeVisualInspectionDTO;
import com.ruike.hme.api.dto.HmeVisualInspectionDTO2;
import com.ruike.hme.api.dto.HmeVisualInspectionDTO3;
import com.ruike.hme.app.service.HmeVisualInspectionService;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO2;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 目检完工 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-01-20 15:02:45
 */
@RestController("hmeVisualInspectionController.v1")
@RequestMapping("/v1/{organizationId}/hme-visual-inspection")
@Slf4j
public class HmeVisualInspectionController extends BaseController {

    @Autowired
    private HmeVisualInspectionService hmeVisualInspectionService;

    @ApiOperation(value = "进站条码数据查询")
    @GetMapping(value = "/material-lot-query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeVisualInspectionVO>> materialLotQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                        HmeVisualInspectionDTO dto){
        return Results.success(hmeVisualInspectionService.materialLotQuery(tenantId, dto, "COS_MJ_COMPLETED"));
    }

    @ApiOperation(value = "条码扫描")
    @PostMapping(value = "/material-lot-scan", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeVisualInspectionDTO2> scanMaterialLot(@PathVariable(value = "organizationId") Long tenantId,
                                                                   @RequestBody HmeVisualInspectionDTO2 dto){
        long startDate = System.currentTimeMillis();
        HmeVisualInspectionDTO2 result = hmeVisualInspectionService.scanMaterialLot(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 总耗时：{}毫秒", dto.getMaterialLotCode(), (endDate - startDate));
        return Results.success(result);
    }

    @ApiOperation(value = "容器扫描")
    @GetMapping(value = "/container-scan/{containerCode}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeVisualInspectionVO2> scanContainer(@PathVariable(value = "organizationId") Long tenantId,
                                                                @PathVariable(value = "containerCode") String containerCode){
        return Results.success(hmeVisualInspectionService.scanContainer(tenantId, containerCode));
    }

    @ApiOperation(value = "完工")
    @PostMapping(value = "/complete", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeVisualInspectionDTO3> materialLotComplete(@PathVariable(value = "organizationId") Long tenantId,
                                                                       @RequestBody HmeVisualInspectionDTO3 dto){
        log.info("<====目检完工 完工出站开始啦");
        long startDate = System.currentTimeMillis();
        HmeVisualInspectionDTO3 hmeVisualInspectionDTO3 = hmeVisualInspectionService.materialLotComplete(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("<====目检完工 完工出站 总耗时：{}毫秒", (endDate - startDate));
        return Results.success(hmeVisualInspectionDTO3);
    }

    @ApiOperation(value = "进站取消")
    @PostMapping(value = "/site-in-cancel", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeVisualInspectionDTO3> siteInCancel(@PathVariable(value = "organizationId") Long tenantId,
                                                                       @RequestBody HmeVisualInspectionDTO3 dto){
        return Results.success(hmeVisualInspectionService.siteInCancel(tenantId, dto));
    }
}
