package com.ruike.itf.api.controller.v1;

import com.alibaba.fastjson.JSONArray;
import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfSupplierDTO;
import com.ruike.itf.app.service.ItfSupplierIfaceService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 供应商数据接口表 管理 API
 *
 * @author yapeng.yao@hand-china.com 2020-08-19 18:49:46
 */
@RestController("itfSupplierIfaceSiteController.v1")
@RequestMapping("/v1/itf-supplier-ifaces")
@Slf4j
public class ItfSupplierIfaceController extends BaseController {

    @Autowired
    private ItfSupplierIfaceService itfSupplierIfaceService;

    @ApiOperation(value = "供应商数据同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/supplier")
    public ResponseEntity<?> supplierSync(@RequestBody String dataStr) {
        log.info("<==== supplier-sync Success requestPayload: {}", dataStr);
        List<ItfSupplierDTO> itfSupplierDTOList = JSONArray.parseArray(dataStr, ItfSupplierDTO.class);
        List<ItfCommonReturnDTO> itfCommonReturnDTOList = itfSupplierIfaceService.invoke(itfSupplierDTOList);
        return Results.success(itfCommonReturnDTOList);
    }
}
