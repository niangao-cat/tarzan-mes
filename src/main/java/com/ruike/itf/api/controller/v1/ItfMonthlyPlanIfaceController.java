package com.ruike.itf.api.controller.v1;

import com.alibaba.fastjson.JSONArray;
import com.ruike.itf.api.dto.ItfMonthlyPlanIfaceDTO2;
import com.ruike.itf.app.service.ItfMonthlyPlanIfaceService;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfMonthlyPlanIface;
import com.ruike.itf.domain.repository.ItfMonthlyPlanIfaceRepository;
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

import java.util.List;
import java.util.Map;

/**
 * 月度计划接口表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-06-01 14:21:59
 */
@RestController("itfMonthlyPlanIfaceController.v1")
@RequestMapping("/v1/itf-monthly-plan-ifaces")
@Slf4j
public class ItfMonthlyPlanIfaceController extends BaseController {

    @Autowired
    private ItfMonthlyPlanIfaceService itfMonthlyPlanIfaceService;

    @ApiOperation(value = "月度计划接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity monthlyPlanSync(@RequestBody String dataStr) {
        log.info("<==== month-plan-sync Success requestPayload: {}", dataStr);
        try {
            Map<String, Object> itemMap = JSONArray.parseObject(dataStr, Map.class);
            List<ItfMonthlyPlanIfaceDTO2> list = itfMonthlyPlanIfaceService.invoke(itemMap);
            return Results.success(list);
        }catch (Exception e){
            return Results.error(e.getMessage());
        }
    }

}
