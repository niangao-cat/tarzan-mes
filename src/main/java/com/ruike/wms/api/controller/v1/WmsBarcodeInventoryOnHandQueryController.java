package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsBarcodeInventoryOnHandDetailQueryDTO;
import com.ruike.wms.api.dto.WmsBarcodeInventoryOnHandQueryDTO;
import com.ruike.wms.app.service.WmsBarcodeInventoryOnHandQueryService;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandDetailVO;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandQueryExportVO;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * @description 条码库存现有量查询
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/13
 * @time 10:56
 * @version 0.0.1
 * @return
 */
@RestController("WmsBarcodeInventoryOnHandQueryController.v1")
@RequestMapping("/v1/{organizationId}/barcode-inventory-on-hand-query")
@Slf4j
public class WmsBarcodeInventoryOnHandQueryController extends BaseController {

    @Autowired
    private WmsBarcodeInventoryOnHandQueryService service;


    @ApiOperation(value = "条码库存现有量查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/list"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<WmsBarcodeInventoryOnHandVO>> list(@PathVariable("organizationId") Long tenantId,
                                                                  WmsBarcodeInventoryOnHandQueryDTO dto, PageRequest pageRequest){
        return  Results.success(service.list(tenantId, pageRequest, dto));
    }

    @ApiOperation(value = "条码库存现有量明细查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/listDetail"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<WmsBarcodeInventoryOnHandDetailVO>> listDetail(@PathVariable("organizationId") Long tenantId,
                                                                              WmsBarcodeInventoryOnHandDetailQueryDTO dto, PageRequest pageRequest){
        return  Results.success(service.listDetail(tenantId, pageRequest, dto));
    }

    @ApiOperation(value = "条码库存现有量查询-导出")
    @GetMapping(value = "/excel-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(WmsBarcodeInventoryOnHandQueryExportVO.class)
    public ResponseEntity<List<WmsBarcodeInventoryOnHandQueryExportVO>> inventoryExcelExport(@PathVariable("organizationId") Long tenantId,
                                                                           WmsBarcodeInventoryOnHandQueryDTO dto,
                                                                           HttpServletResponse response,
                                                                           ExportParam exportParam) {
        return Results.success(service.excelExport(tenantId, dto));
    }

}
