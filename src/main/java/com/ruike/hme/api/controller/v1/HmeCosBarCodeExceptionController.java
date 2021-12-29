package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosBarCodeExceptionDTO;
import com.ruike.hme.app.service.HmeCosBarCodeExceptionService;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.config.SwaggerApiConfig;

/**
 * COS条码加工异常汇总报表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:26
 */
@Slf4j
@RestController("hmeCosBarCodeExceptionController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-barcode-exception")
@Api(tags = SwaggerApiConfig.HME_COS_BARCODE_EXCEPTION)
public class HmeCosBarCodeExceptionController {

    @Autowired
    private HmeCosBarCodeExceptionService hmeCosBarCodeExceptionService;

    @ApiOperation(value = "表查询逻辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/list")
    public ResponseEntity<Page<HmeCosBarCodeExceptionVO>> queryList(@PathVariable(value = "organizationId") Long tenantId,
                                                                    HmeCosBarCodeExceptionDTO dto, PageRequest pageRequest) {
        return Results.success(hmeCosBarCodeExceptionService.queryList(tenantId,dto,pageRequest));
    }

}
