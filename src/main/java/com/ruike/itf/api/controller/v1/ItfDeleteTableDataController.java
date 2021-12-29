package com.ruike.itf.api.controller.v1;

import com.ruike.itf.app.service.ItfDeleteTableDataService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 删除接口表数据 管理 API
 *
 * @author yapeng.yao@hand-china.com 2020-08-21 11:19:59
 */
@RestController("itfDeleteTableDataController.v1")
@RequestMapping("/v1/{organizationId}/itf-delete-table-data")
public class ItfDeleteTableDataController {

    @Autowired
    private ItfDeleteTableDataService itfDeleteTableDataService;

    /**
     * 接口表数据删除
     *
     * @param tenantId 租户ID
     * @return
     * @author penglin.sui@hand-china.com
     */
    @ApiOperation(value = "接口表数据删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/invoke-del")
    public ResponseEntity<?> invokeDel(@PathVariable("organizationId") Long tenantId) {
        itfDeleteTableDataService.invokeDel(tenantId);
        return Results.success();
    }
}
