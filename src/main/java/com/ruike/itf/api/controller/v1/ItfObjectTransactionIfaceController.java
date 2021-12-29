package com.ruike.itf.api.controller.v1;

import com.ruike.hme.domain.entity.HmeProductionVersion;
import com.ruike.itf.api.dto.ItfObjectTransactionResultQueryDTO;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.domain.vo.ItfObjectTransactionIfaceVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfObjectTransactionIface;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * 事务汇总接口表 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2020-08-11 09:44:09
 */
@RestController("itfObjectTransactionIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-object-transaction-ifaces")
public class ItfObjectTransactionIfaceController extends BaseController {

    private final ItfObjectTransactionIfaceService service;

    public ItfObjectTransactionIfaceController(ItfObjectTransactionIfaceService service) {
        this.service = service;
    }

//    @ApiOperation(value = "创建事务汇总测试用")
//    @Permission(level = ResourceLevel.ORGANIZATION)
//    @PostMapping("/summary")
//    public ResponseEntity<List<ItfObjectTransactionIface>> summary(@PathVariable("organizationId") Long tenantId) {
//        List<ItfObjectTransactionIface> list = service.processSummary(tenantId);
//        return Results.success(list);
//    }
//
//    @ApiOperation(value = "回传事务接口测试用")
//    @Permission(level = ResourceLevel.ORGANIZATION)
//    @PostMapping("/material-move")
//    public ResponseEntity<List<ItfObjectTransactionIface>> materialMoveInvoke(@PathVariable("organizationId") Long tenantId) throws Exception {
//        service.materialMove(tenantId);
//        return Results.success();
//    }
//
//    @ApiOperation(value = "回传生产报工测试用")
//    @Permission(level = ResourceLevel.ORGANIZATION)
//    @PostMapping("/production-statement-erp")
//    public ResponseEntity<List<ItfObjectTransactionIface>> productionStatementInvoke(@PathVariable("organizationId") Long tenantId) throws Exception {
//        service.productionStatementInvoke(tenantId, wmsObjectTransactionResponseVOS);
//        return Results.success();
//    }

     @ApiOperation(value = "查询事务数据")
     @Permission(level = ResourceLevel.ORGANIZATION)
     @GetMapping("/list")
     public  ResponseEntity<Page<ItfObjectTransactionIfaceVO>> list(@PathVariable("organizationId") Long tenantId, ItfObjectTransactionResultQueryDTO dto, PageRequest pageRequest){
         return Results.success(service.list(tenantId, dto, pageRequest));
     }

    @ApiOperation(value = "事务数据更新")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/update"} , produces = "application/json;charset=UTF-8")
    public ResponseEntity<ItfObjectTransactionIface> update(@PathVariable("organizationId") Long tenantId, @RequestBody ItfObjectTransactionIfaceVO itfObjectTransactionIface) {
        return Results.success(service.update(tenantId, itfObjectTransactionIface));
    }



}
