package com.ruike.itf.api.controller.v1;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfCustomerDTO;
import com.ruike.itf.app.service.ItfCustomerIfaceService;
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
import java.util.Map;

/**
 * 客户数据表 管理 API
 *
 * @author yapeng.yao@hand-china.com 2020-08-21 11:19:59
 */
@RestController("itfCustomerIfaceSiteController.v1")
@RequestMapping("/v1/itf-customer-ifaces")
@Slf4j
public class ItfCustomerIfaceController extends BaseController {

    @Autowired
    private ItfCustomerIfaceService itfCustomerIfaceService;

    /**
     * 客户数据同步接口
     *
     * @param dataStr
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @ApiOperation(value = "客户数据同步")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/customer")
    public ResponseEntity<?> customerSync(@RequestBody String dataStr) {
        log.info("<==== customer Success requestPayload: {}", dataStr);
        Map dataMap = JSON.parseObject(dataStr, Map.class);
        List<ItfCommonReturnDTO> itfCommonReturnDTOList = itfCustomerIfaceService.invoke(dataMap);
        return Results.success(itfCommonReturnDTOList);
    }

}