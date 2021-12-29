package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeMtEoService;
import com.ruike.hme.domain.vo.HmeEoVO3;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname HmeMtEoController
 * @Description 执行作业管理-打印
 * @Date 2020/9/22 15:35
 * @Created by yaoyapeng
 */
@RestController("hmeMtEoController.v1")
@RequestMapping("/v1/{organizationId}/hme-mt-eo")
@Api(tags = "hmeMtEo")
@Slf4j
public class HmeMtEoController extends BaseController {

    @Autowired
    private HmeMtEoService hmeMtEoService;

    /**
     * @param tenantId/print
     * @param type
     * @param hmeEoVO3List
     * @param response
     * @Description 打印
     * @Date 2020/09/22 14:36
     * @Created by yaoyapeng
     */
    @ApiOperation(value = "print")
    @PostMapping(value = {"/print/{type}"})
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> print(@PathVariable("organizationId") Long tenantId, @PathVariable("type") String type,
                                   @RequestBody List<HmeEoVO3> hmeEoVO3List, HttpServletResponse response) {
        log.info("<==== HmeMtEoController-print info:{},{},{}", tenantId, type, hmeEoVO3List);

        try {
            hmeMtEoService.print(tenantId, type, hmeEoVO3List, response);
        } catch (Exception ex) {
            log.error("<==== HmeMtEoController-print error: {}:{}", ex.getMessage(), ex);
        }
        return Results.success();
    }

    @ApiOperation(value = "打印检验")
    @PostMapping(value = {"/print-check"})
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeEoVO3>> printCheck(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody List<HmeEoVO3> hmeEoVO3List) {
        return Results.success(hmeMtEoService.printCheck(tenantId, hmeEoVO3List));
    }
}
