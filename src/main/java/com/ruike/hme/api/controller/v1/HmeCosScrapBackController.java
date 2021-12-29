package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.repository.HmeCosScrapBackRepository;
import com.ruike.hme.domain.vo.HmeCosScrapBackVO;
import com.ruike.hme.domain.vo.HmeCosScrapBackVO2;
import com.ruike.hme.domain.vo.HmeCosScrapBackVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * COS报废撤回
 *
 * @author sanfeng.zhang@hand-china.com 2021/1/26 9:29
 */
@RestController("HmeCosScrapBackController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-scrap-back")
//@Api(tags = SwaggerApiConfig.HME_COS_SCRAP_BACK)
public class HmeCosScrapBackController {

    @Autowired
    private HmeCosScrapBackRepository hmeCosScrapBackRepository;


    @ApiOperation(value = "COS报废撤回-报废数据查询")
    @GetMapping(value = "/cos-scrap-query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeCosScrapBackVO2>> queryCosScrap(@PathVariable("organizationId") Long tenantId,
                                                                  HmeCosScrapBackVO backVO,
                                                                  @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeCosScrapBackRepository.queryCosScrap(tenantId, backVO, pageRequest));
    }

    @ApiOperation(value = "COS报废撤回-装入(报废撤回)")
    @PostMapping(value = "/cos-scrap-back-execute", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> cosScrapBackExecute(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody HmeCosScrapBackVO3 backVO) {
        hmeCosScrapBackRepository.cosScrapBackExecute(tenantId, backVO);
        return Results.success();
    }

    @ApiOperation(value = "COS报废撤回-验证WAFER")
    @PostMapping(value = "/cos-scrap-verify-wafer", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> cosScrapVerifyWafer(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody HmeCosScrapBackVO3 backVO) {
        return Results.success(hmeCosScrapBackRepository.cosScrapVerifyWafer(tenantId, backVO));
    }
}
