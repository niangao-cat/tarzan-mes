package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.app.service.WmsTransactionTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;

/**
 * 事务类型维护 管理 API
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/30 09:15:39
 */
@RestController("wmsTransactionTypeController.v1")
@RequestMapping("/v1/{organizationId}/transaction-type")
@Api(tags = "wmsTransactionType")
public class WmsTransactionTypeController extends BaseController {
    private final WmsTransactionTypeService service;

    public WmsTransactionTypeController(WmsTransactionTypeService service) {
        this.service = service;
    }

    @ApiOperation(value = "事务类型查询")
    @GetMapping(value = "/query/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsTransactionTypeDTO>> queryList(@PathVariable("organizationId") Long tenantId,
                                                                 String transactionTypeCode,
                                                                 String description,
                                                                 PageRequest pageRequest) {
        return Results.success(service.queryList(tenantId, pageRequest, transactionTypeCode, description));
    }

    @ApiOperation(value = "事务类型保存")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<String> save(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody WmsTransactionTypeDTO dto) {
        service.insertOrUpdate(tenantId, dto);
        return Results.success();
    }
}
