package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsMaterialLotQryResultDTO;
import com.ruike.wms.app.service.WmsMaterialLotPrintService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import static tarzan.config.SwaggerApiConfig.WMS_MATERIAL_LOT_PRINT;

/**
 * 条码打印 管理 API
 *
 * @author penglin.sui@hand-china.com 2020-07-30 10:00
 */
@RestController("wmsMaterialLotPrintController.v1")
@RequestMapping("/v1/{organizationId}/wms-material-log-print")
@Api(tags = WMS_MATERIAL_LOT_PRINT)
@Slf4j
public class WmsMaterialLotPrintController {
    @Autowired
    private WmsMaterialLotPrintService service;

    @PostMapping("/pdf")
    @ApiOperation("条码PDF打印")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> materialLotPrintPdf(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody List<String> materialLotIds,
                                                 HttpServletResponse response) {
        log.info("<==== WmsMaterialLotPrintController-materialLotPrintPdf info:{},{},{}", tenantId, materialLotIds);

        /*ResponseData<Map<String, List<String>>> responseData = new ResponseData<Map<String, List<String>>>();
        responseData.setSuccess(false);
        if(materialLotIds != null) {
            if(materialLotIds.size() > 0) {
                Map<String, List<String>> urlMap = new HashMap<>();

            }
        }*/
        try {
            service.multiplePrint(tenantId, materialLotIds, response);
                    /*responseData.setSuccess(true);
                    urlMap.put("url", urls);
                    responseData.setRows(urlMap);*/
        } catch (Exception ex) {
            log.error("<==== WmsMaterialLotPrintController-materialLotPrintPdf error: {}:{}", ex.getMessage(), ex);
        }
        return Results.success();
    }

    /**
     * @param tenantId
     * @param type
     * @param wmsMaterialLotQryResultDTOList
     * @param response
     * @Description 打印
     * @Date 2020/09/22 14:36
     * @Created by yaoyapeng
     */
    @ApiOperation(value = "print")
    @PostMapping(value = {"/print/{type}"})
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> print(@PathVariable("organizationId") Long tenantId, @PathVariable("type") String type,
                                   @RequestBody List<WmsMaterialLotQryResultDTO> wmsMaterialLotQryResultDTOList, HttpServletResponse response) {
        log.info("<==== WmsMaterialLotPrintController-print info:{},{},{}", tenantId, type, wmsMaterialLotQryResultDTOList);

        try {
            service.print(tenantId, type, wmsMaterialLotQryResultDTOList, response);
        } catch (Exception ex) {
            log.error("<==== WmsMaterialLotPrintController-print error: {}:{}", ex.getMessage(), ex);
        }
        return Results.success();
    }
}
