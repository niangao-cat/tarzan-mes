package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosWorkcellExceptionDTO;
import com.ruike.hme.app.service.HmeCosWorkcellExceptionService;
import com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.config.SwaggerApiConfig;

/**
 * COS工位加工异常汇总表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/13 12:35
 */
@Slf4j
@RestController("hmeCosWorkcellExceptionController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-workcell-exception")
@Api(tags = SwaggerApiConfig.HME_COS_WORKCELL_EXCEPTION)
public class HmeCosWorkcellExceptionController extends BaseController {

    @Autowired
    private HmeCosWorkcellExceptionService hmeCosWorkcellExceptionService;

    @ApiOperation(value = "表查询逻辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/list")
    public ResponseEntity<Page<HmeCosWorkcellExceptionVO>> queryList(@PathVariable(value = "organizationId") Long tenantId,
                                                                     HmeCosWorkcellExceptionDTO dto, PageRequest pageRequest) {
        return Results.success(hmeCosWorkcellExceptionService.queryList(tenantId,dto,pageRequest));
    }

}
