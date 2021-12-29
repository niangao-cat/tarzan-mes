package com.ruike.wms.api.controller.v1;

import com.ruike.wms.app.service.WmsSupplierService;
import com.ruike.wms.domain.vo.WmsSupplierVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;
import tarzan.modeling.domain.entity.MtSupplier;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WmsSupplierController
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 16:30
 */
@RestController("wmsSupplierController.v1")
@RequestMapping("/v1/{organizationId}/wms-supplier")
@Api(tags = SwaggerApiConfig.WMS_SUPPLIER)
@Slf4j
public class WmsSupplierController extends BaseController {
    @Autowired
    private WmsSupplierService service;

    @ApiOperation(value = "查询供应商(前台)")
    @GetMapping(value = "/ui-query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtSupplier>> uiQuery(@PathVariable("organizationId") Long tenantId,
                                                    WmsSupplierVO dto,
                                                    @ApiIgnore PageRequest pageRequest) {
        return Results.success(service.uiQuery(tenantId, dto, pageRequest));
    }
}
