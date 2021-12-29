package com.ruike.itf.api.controller.v1;

import com.ruike.itf.app.service.ItfDeleteCollectTableDataService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 设备数据采集接口表数据删除 管理 API
 *
 * @author penglin.sui@hand-china.com 2021-07-25 18:27
 */
@RestController("itfDeleteCollectTableDataController.v1")
@RequestMapping("/v1/{organizationId}/itf-delete-collect-table-data")
public class ItfDeleteCollectTableDataController {

    @Autowired
    private ItfDeleteCollectTableDataService itfDeleteCollectTableDataService;

    /**
     * 设备数据采集接口表数据删除
     *
     * @param tenantId 租户ID
     * @return
     * @author penglin.sui@hand-china.com
     */
    @ApiOperation(value = "设备数据采集接口表数据删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/invoke-del")
    public ResponseEntity<?> invokeDel(@PathVariable("organizationId") Long tenantId) {
        itfDeleteCollectTableDataService.invokeDel(tenantId);
        return Results.success();
    }
}
