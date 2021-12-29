package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsSoTransferDTO;
import com.ruike.wms.api.dto.WmsSoTransferDTO2;
import com.ruike.wms.api.dto.WmsSoTransferReturnDTO;
import com.ruike.wms.app.service.WmsSoTransferService;
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

import java.util.List;

/**
 * @ClassName WmsSoTransferController
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/22 10:25
 * @Version 1.0
 **/
@RestController("wmsSoTransferController.v1")
@RequestMapping("/v1/{organizationId}/wms-so-transfer")
@Api(tags = SwaggerApiConfig.WMS_SO_TRANSFER)
@Slf4j
public class WmsSoTransferController extends BaseController {

    @Autowired
    private WmsSoTransferService wmsSoTransferService;

    @ApiOperation(value = "销售订单查询")
    @GetMapping(value = "/so-query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsSoTransferReturnDTO>> querySo(@PathVariable("organizationId") Long tenantId,
                                                                WmsSoTransferDTO dto,
                                                                @ApiIgnore PageRequest pageRequest) {
        log.info("<====WmsSoTransferController-querySo:{},{}",tenantId, dto);
        Page<WmsSoTransferReturnDTO> wmsSoTransferReturnDTOs=wmsSoTransferService.querySo(tenantId,pageRequest,dto);
        return Results.success(wmsSoTransferReturnDTOs);
    }

    @ApiOperation(value = "销售订单保存")
    @PostMapping(value = "/so-confirm", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsSoTransferDTO2>> confirmSo(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody List<WmsSoTransferDTO2> dtos) {
        log.info("<====WmsSoTransferController-confirmSo:{},{}",tenantId, dtos);
        wmsSoTransferService.confirmSo(tenantId,dtos);
        return Results.success(dtos);
    }
}
