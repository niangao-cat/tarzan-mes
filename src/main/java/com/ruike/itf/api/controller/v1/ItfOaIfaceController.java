package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.IftSendOAExceptionStatusDTO;
import com.ruike.itf.app.service.ItfExceptionWkcRecordService;
import com.ruike.itf.domain.vo.IftSendOAExceptionMsgVO;
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

import java.text.ParseException;
import java.util.List;

@RestController("ItfOaIfaceController.v1")
@RequestMapping("/v1/itf-oa-iface")
@Slf4j
public class ItfOaIfaceController extends BaseController {

    @Autowired
    private ItfExceptionWkcRecordService itfExceptionWkcRecordService;

    @ApiOperation(value = "OA回传异常信息工单状态接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/exception-wkc-record-status")
    public ResponseEntity<IftSendOAExceptionStatusDTO> exceptionWkcRecordStatus(@RequestBody IftSendOAExceptionStatusDTO iftSendOAExceptionMsgVO) throws ParseException {
        log.info("<==== exception-wkc-record-status Success requestPayload: {}", iftSendOAExceptionMsgVO);
        IftSendOAExceptionStatusDTO vo = itfExceptionWkcRecordService.exceptionWkcRecordStatus(iftSendOAExceptionMsgVO);
        return Results.success(vo);
    }
}
