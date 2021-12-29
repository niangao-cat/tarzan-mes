package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfExceptionWkcRecordDTO;
import com.ruike.itf.app.service.ItfExceptionWkcRecordService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @auther:lkj
 * @Date:2020/8/3 18:41
 * @E-mail:kejin.liu@hand-china.com
 * @Description: 异常信息接口
 */

@RestController("itfExceptionWkcRecordController.v1")
@RequestMapping("/v1/{organizationId}/itf-exception-wkc-record")
@Slf4j
public class ItfExceptionWkcRecordController extends BaseController {

    @Autowired
    private ItfExceptionWkcRecordService itfExceptionWkcRecordService;

    @ApiOperation(value = "发送异常信息至ESB")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/send/ebs/exception/info")
    public ResponseData<Void> sendExceptionInfo(@PathVariable("organizationId") Long tenantId, @RequestBody ItfExceptionWkcRecordDTO dto) {
        /**
         *
         * 功能描述: 发送异常信息至EBS
         *
         * @auther:lkj
         * @date:2020/8/4 下午1:25
         * @param itfBomComponentIface
         * @return:void
         *
         */
        ResponseData<Void> voidResponseData = new ResponseData<Void>();
        try {
            itfExceptionWkcRecordService.sendExceptionInfoWX(tenantId, dto);
            return voidResponseData;
        } catch (Exception e) {
            log.error("<====ItfExceptionWkcRecordController-sendExceptionInfo:{}，{}", tenantId, dto);
            voidResponseData.setSuccess(false);
            voidResponseData.setMessage(e.getMessage());
            return voidResponseData;
        }
    }
}
