package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeAfterSaleQuotationHeaderDto;
import com.ruike.hme.app.service.HmeAfterSaleQuotationHeaderService;
import com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO13;
import com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO3;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeAfterSaleQuotationHeader;
import com.ruike.hme.domain.repository.HmeAfterSaleQuotationHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import java.text.ParseException;

/**
 * 售后报价单头表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-09-26 11:07:31
 */
@RestController("hmeAfterSaleQuotationHeaderController.v1")
@RequestMapping("/v1/{organizationId}/hme-after-sale-quotation-headers")
public class HmeAfterSaleQuotationHeaderController extends BaseController {

    @Autowired
    private HmeAfterSaleQuotationHeaderService hmeAfterSaleQuotationHeaderService;

    @ApiOperation(value = "SN扫描")
    @GetMapping(value = "/scan-sn/{snNum}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeAfterSaleQuotationHeaderVO3> scanSn(@PathVariable("organizationId") Long tenantId,
                                                                 @PathVariable("snNum") String snNum) throws ParseException {
        return Results.success(hmeAfterSaleQuotationHeaderService.scanSn(tenantId, snNum));
    }

    @ApiOperation(value = "点击新建按钮时的查询")
    @GetMapping(value = "/create-query/{snNum}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeAfterSaleQuotationHeaderVO3> createQuery(@PathVariable("organizationId") Long tenantId,
                                                                 @PathVariable("snNum") String snNum) throws ParseException {
        return Results.success(hmeAfterSaleQuotationHeaderService.createQuery(tenantId, snNum));
    }

    @ApiOperation(value = "自动查询质保内发货日期")
    @GetMapping(value = "/send-date-query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<String> sendDateQueryByMaterial(@PathVariable("organizationId") Long tenantId,
                                                          HmeAfterSaleQuotationHeaderDto dto) throws ParseException {
        return Results.success(hmeAfterSaleQuotationHeaderService.sendDateQueryByMaterial(tenantId, dto));
    }

    @ApiOperation(value = "保存")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> save(@PathVariable("organizationId") Long tenantId,
                                  @RequestBody HmeAfterSaleQuotationHeaderVO3 dto){
        hmeAfterSaleQuotationHeaderService.save(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "提交")
    @PostMapping(value = "/submit", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeAfterSaleQuotationHeaderVO13> submit(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody HmeAfterSaleQuotationHeaderVO3 dto){
        return Results.success(hmeAfterSaleQuotationHeaderService.submit(tenantId, dto));
    }

    @ApiOperation(value = "修改")
    @PostMapping(value = "/edit", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeAfterSaleQuotationHeaderVO13> edit(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody HmeAfterSaleQuotationHeaderVO3 dto){
        return Results.success(hmeAfterSaleQuotationHeaderService.edit(tenantId, dto));
    }

    @ApiOperation(value = "取消")
    @PostMapping(value = "/cancel", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeAfterSaleQuotationHeaderVO13> cancel(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeAfterSaleQuotationHeaderVO3 dto){
        return Results.success(hmeAfterSaleQuotationHeaderService.cancel(tenantId, dto));
    }
}
