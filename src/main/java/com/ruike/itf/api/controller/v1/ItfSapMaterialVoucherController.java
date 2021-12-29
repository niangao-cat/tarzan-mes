package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.SapMaterialVoucherDTO;
import com.ruike.itf.app.service.ItfSapMaterialVoucherService;
import com.ruike.itf.domain.entity.ItfAfterSalesRepairIfaces;
import com.ruike.itf.domain.repository.ItfAfterSalesRepairIfacesRepository;
import com.ruike.itf.domain.vo.ItfMaterialVoucherVO;
import com.sap.conn.jco.JCoException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

/**
 * 查询SAP物料凭证
 *
 * @author kejin.liu01@hand-china.com 2020-09-02 09:59:41
 */
@Api("查询SAP物料凭证")
@RestController("ItfSapMaterialVoucherController.v1")
@RequestMapping("/v1/{organizationId}/itf-sap-material-voucher")
@Slf4j
public class ItfSapMaterialVoucherController extends BaseController {

    private final ItfSapMaterialVoucherService itfSapMaterialVoucherService;

    public ItfSapMaterialVoucherController(ItfSapMaterialVoucherService itfSapMaterialVoucherService) {
        this.itfSapMaterialVoucherService = itfSapMaterialVoucherService;
    }

    @ApiOperation(value = "SAP和MES凭证差异")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/material-voucher-difference")
    public ResponseEntity<List<ItfMaterialVoucherVO>> materialVoucherList(@PathVariable("organizationId") Long tenantId, @RequestBody SapMaterialVoucherDTO dto) throws JCoException, ParseException {
        log.info("dto:{}", dto);
        return Results.success(itfSapMaterialVoucherService.materialVoucherList(tenantId, dto));
    }

    @ApiOperation(value = "导出SAP和MES凭证差异")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(ItfMaterialVoucherVO.class)
    public ResponseEntity<List<ItfMaterialVoucherVO>> export(@PathVariable("organizationId") Long tenantId,
                                                             SapMaterialVoucherDTO dto,
                                                             ExportParam exportParam,
                                                             HttpServletResponse response) throws JCoException, ParseException {
        return Results.success(itfSapMaterialVoucherService.materialVoucherList(tenantId, dto));
    }


}
