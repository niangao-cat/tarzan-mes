package io.tarzan.common.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.api.dto.MtErrorMessageDTO;
import io.tarzan.common.api.dto.MtErrorMessageDTO2;
import io.tarzan.common.api.dto.MtErrorMessageDTO3;
import io.tarzan.common.api.dto.MtErrorMessageDTO4;
import io.tarzan.common.app.service.MtErrorMessageService;
import io.tarzan.common.domain.entity.MtErrorMessage;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtErrorMessageVO;
import io.tarzan.common.domain.vo.MtErrorMessageVO2;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 管理 API
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Api(tags = "MtErrorMessage")
@RestController("mtErrorMessageController.v1")
@RequestMapping("/v1/{organizationId}/mt-error-message")
public class MtErrorMessageController extends BaseController {

    @Autowired
    private MtErrorMessageRepository repository;

    @Autowired
    private MtErrorMessageService service;

    @ApiOperation(value = "获取消息列表（前台）")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtErrorMessageVO>> listErrorMessageForUi(@PathVariable("organizationId") Long tenantId,
                                                                      MtErrorMessageDTO3 condition, @ApiIgnore @SortDefault(value = MtErrorMessage.FIELD_MESSAGE_CODE,
                                    direction = Sort.Direction.ASC) PageRequest pageRequest) {
        ResponseData<Page<MtErrorMessageVO>> responseData = new ResponseData<Page<MtErrorMessageVO>>();
        try {
            responseData.setRows(service.listErrorMessageForUi(tenantId, condition, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "新增&更新消息（前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveErrorMessageForUi(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody MtErrorMessageDTO4 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            validObject(dto);
            responseData.setRows(service.saveErrorMessageForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "批量删除消息（前台）")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> deleteErrorMessageForUi(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody List<MtErrorMessageVO2> list) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.deleteErrorMessageForUi(tenantId, list);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "messageCodeLimitMessageGet")
    @PostMapping(value = {"/limit-code"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> messageCodeLimitMessageGet(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtErrorMessageDTO2 condition) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.messageCodeLimitMessageGet(tenantId, condition.getModule(),
                            condition.getMessageCode()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "messageLimitMessageCodeQuery")
    @PostMapping(value = {"/limit-message"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> messageLimitMessageCodeQuery(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody MtErrorMessageDTO condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.messageLimitMessageCodeQuery(tenantId, condition.getModule(),
                            condition.getMessage()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
