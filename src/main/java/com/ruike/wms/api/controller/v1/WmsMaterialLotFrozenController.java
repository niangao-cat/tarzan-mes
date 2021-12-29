package com.ruike.wms.api.controller.v1;

import java.util.List;

import com.ruike.wms.api.dto.WmsMaterialLotFrozenDTO;
import com.ruike.wms.domain.repository.WmsMaterialLotFrozenRepository;
import com.ruike.wms.domain.vo.WmsMaterialLotVO2;
import com.ruike.wms.domain.vo.WmsMaterialLotVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;
import tarzan.config.SwaggerApiConfig;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WmsMaterialLotFrozenController
 *
 * @author liyuan.lv@hand-china.com 2020/04/28 10:55
 */
@RestController("wmsMaterialLotFrozenController.v1")
@RequestMapping("/v1/{organizationId}/wms-material-lot-frozen")
@Api(tags = SwaggerApiConfig.WMS_MATERIAL_LOT_FROZEN)
@Slf4j
public class WmsMaterialLotFrozenController extends BaseController {

    @Autowired
    private WmsMaterialLotFrozenRepository repository;

    @ApiOperation(value = "获取符合条件的条码信息")
    @GetMapping(value = "/ui-query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsMaterialLotVO2>> uiQuery(
            @PathVariable("organizationId") Long tenantId, WmsMaterialLotFrozenDTO dto, PageRequest pageRequest) {
        log.info("<====WmsMaterialLotFrozenController-uiQuery:{}，{}", tenantId, dto);
        return Results.success(repository.queryForMaterialLot(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "执行冻结解冻")
    @PostMapping(value = "/execute", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsMaterialLotVO2>> execute(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody WmsMaterialLotVO3 dto) {
        log.info("<====WmsMaterialLotFrozenController-execute:{}，{}", tenantId, dto);
        return Results.success(repository.executeFreeze(tenantId, dto));
    }
}
