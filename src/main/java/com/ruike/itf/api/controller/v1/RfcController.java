package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.RfcParamDTO;
import com.ruike.itf.app.service.ISapRfcService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zhenyu.zhou
 * @Date: 2020/1/16 09:41
 * @Description:
 */
@RestController("rfcController.v1")
@RequestMapping("/v1/rfc")
public class RfcController  extends BaseController {
    @Autowired
    private ISapRfcService iSapRfcService;

    /**
     * 物料接口
     *
     * @author jiangling.zheng@hand-china.com 2020/7/17 11:07
     * @return
     */
    @PostMapping(value = {"/material-rfc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.SITE)
    @ResponseBody
    public ResponseEntity<?> materialRfc(RfcParamDTO dto) {
        this.iSapRfcService.materialRfc(dto);
        return Results.success();
    }

    /**
     * 工单接口
     *
     * @author jiangling.zheng@hand-china.com 2020/7/17 11:07
     * @return
     */
    @PostMapping(value = {"/wo-rfc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.SITE)
    @ResponseBody
    public ResponseEntity<?> workOrderRfc(RfcParamDTO dto) {
        this.iSapRfcService.workOrderRfc(dto);
        return Results.success();
    }

    /**
     * 物料组接口
     *
     * @author jiangling.zheng@hand-china.com 2020/7/17 11:08
     * @return
     */
    @PostMapping(value = {"/item-group-rfc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.SITE)
    @ResponseBody
    public ResponseEntity<?> itemGroupRfc() {
        this.iSapRfcService.itemGroupRfc();
        return Results.success();
    }

}
