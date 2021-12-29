package com.ruike.itf.api.controller.v1;

import com.ruike.itf.domain.repository.ItfStocktakeIfaceRepository;
import com.ruike.itf.domain.vo.ItfStocktakeVO;
import com.ruike.itf.domain.vo.ItfStocktakeVO3;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 盘点单接口
 *
 * @author sanfeng.zhang@hand-china.com 2021/7/6 17:45
 */
@RestController("itfStocktakeIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-stocktake-ifaces")
public class ItfStocktakeIfaceController {

    @Autowired
    private ItfStocktakeIfaceRepository itfStocktakeIfaceRepository;

    @ApiOperation(value = "盘点单-查询")
    @Permission(permissionPublic = true)
    @GetMapping("/query-stocktake-list")
    public ResponseEntity<?> queryStocktakeList(@PathVariable("organizationId") Long tenantId) {
        List<ItfStocktakeVO> itfSupplierDTOList = itfStocktakeIfaceRepository.queryStocktakeList(tenantId);
        return Results.success(itfSupplierDTOList);
    }

    @ApiOperation(value = "盘点单-更新")
    @Permission(permissionPublic = true)
    @PostMapping("/update-stocktake-list")
    public ResponseEntity<?> updateStocktakeList(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody List<ItfStocktakeVO> stocktakeVOList) {
        List<ItfStocktakeVO3> itfStocktakeVO3List = itfStocktakeIfaceRepository.updateStocktakeList(stocktakeVOList);
        return Results.success(itfStocktakeVO3List);
    }

}
