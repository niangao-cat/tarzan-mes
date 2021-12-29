package com.ruike.wms.api.controller.v1;

import com.ruike.wms.app.service.WmsCheckedWaitGroudingService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import static tarzan.config.SwaggerApiConfig.WMS_CHECKED_WAIT_GROUDING;

/**
 * @Description 已检待上架看板
 * @Author tong.li
 * @Date 2020/4/24 12:46
 * @Version 1.0
 */

@RestController("wmsCheckedWaitGroudingController.v1")
@RequestMapping("/v1/{organizationId}/wms-checked-wait-grouding")
@Api(tags = WMS_CHECKED_WAIT_GROUDING)
@Slf4j
public class WmsCheckedWaitGroudingController extends BaseController {
    @Autowired
    private WmsCheckedWaitGroudingService wmsCheckedWaitGroudingService;

    /**
    * @param tenantId 1
    * @param pageRequest 2
    * @return : org.springframework.http.ResponseEntity<?>
    * @Description: 已收待上架看板 任务区域数据查询
    * @author: tong.li
    * @date 2020/4/24 12:59
    * @version 1.0
    */
    @ApiOperation(value = "任务区域数据查询")
    @GetMapping(value = {"/task/data/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> taskDataQueryForUiUi(@PathVariable("organizationId") Long tenantId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsCheckedWaitGroudingService.queryTaskData(tenantId, pageRequest));
    }

    @ApiOperation(value = "30天物料上架量")
    @GetMapping(value = {"/material/storaged/num"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> materialStoragedNumQuery(@PathVariable("organizationId") Long tenantId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsCheckedWaitGroudingService.materialStoragedNumQuery(tenantId, pageRequest));
    }

    @ApiOperation(value = "趋势图数据查询")
    @GetMapping(value = {"/trend/data/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> trendDataQuery(@PathVariable("organizationId") Long tenantId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsCheckedWaitGroudingService.trendDataQuery(tenantId, pageRequest));
    }
}
