package tarzan.method.api.controller.v1;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
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
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.method.api.dto.MtNcCodeDTO;
import tarzan.method.api.dto.MtNcCodeDTO2;
import tarzan.method.api.dto.MtNcCodeDTO3;
import tarzan.method.api.dto.MtNcCodeDTO5;
import tarzan.method.app.service.MtNcCodeService;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.method.domain.vo.MtNcCodeVO;
import tarzan.method.domain.vo.MtNcCodeVO1;

/**
 * 不良代码数据 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@RestController("mtNcCodeController.v1")
@RequestMapping("/v1/{organizationId}/mt-nc-code")
@Api(tags = "MtNcCode")
public class MtNcCodeController extends BaseController {

    @Autowired
    private MtNcCodeService service;

    @ApiOperation(value = "UI查询不良代码")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtNcCodeDTO2>> queryNcCodeListForUi(@PathVariable("organizationId") Long tenantId,
                                                                 MtNcCodeDTO2 dto, @ApiIgnore @SortDefault(value = MtNcCode.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtNcCodeDTO2>> responseData = new ResponseData<Page<MtNcCodeDTO2>>();
        try {
            responseData.setRows(service.queryNcCodeListForUi(tenantId, dto, pageRequest));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI根据不良代码Id查询不良代码详细信息")
    @GetMapping(value = {"/detail/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcCodeDTO3> queryNcCodeDetailForUi(@PathVariable("organizationId") Long tenantId,
                                                             String ncCodeId) {
        ResponseData<MtNcCodeDTO3> responseData = new ResponseData<MtNcCodeDTO3>();
        try {
            responseData.setRows(service.queryNcCodeDetailForUi(tenantId, ncCodeId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存不良代码")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveNcCodeForUi(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody MtNcCodeDTO5 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto.getMtNcCode());
        if (CollectionUtils.isNotEmpty(dto.getMtNcSecondaryCodeList())) {
            validObject(dto.getMtNcSecondaryCodeList());
        }
        if (CollectionUtils.isNotEmpty(dto.getMtNcValidOperList())) {
            validObject(dto.getMtNcValidOperList());
        }
        try {
            responseData.setRows(service.saveNcCodeForUi(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @Autowired
    private MtNcCodeRepository repository;

    @ApiOperation(value = "ncCodeLimitNcGroupGet")
    @PostMapping(value = {"/group"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncCodeLimitNcGroupGet(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody String ncCodeId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.ncCodeLimitNcGroupGet(tenantId, ncCodeId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitNcCodeQuery")
    @PostMapping(value = {"/limit-property/code"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcCode>> propertyLimitNcCodeQuery(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtNcCodeDTO dto) {
        ResponseData<List<MtNcCode>> responseData = new ResponseData<List<MtNcCode>>();
        try {
            MtNcCode param = new MtNcCode();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.propertyLimitNcCodeQuery(tenantId, param));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncCodePropertyGet")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcCode> ncCodePropertyGet(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody String ncCodeId) {
        ResponseData<MtNcCode> responseData = new ResponseData<MtNcCode>();
        try {
            responseData.setRows(repository.ncCodePropertyGet(tenantId, ncCodeId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncCodeGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcCode> ncCodeGet(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody String ncCodeId) {

        ResponseData<MtNcCode> responseData = new ResponseData<MtNcCode>();
        try {
            responseData.setRows(repository.ncCodeGet(tenantId, ncCodeId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncCodeGroupMemberQuery")
    @PostMapping(value = {"/limit-group/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcCode>> ncCodeGroupMemberQuery(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody String ncGroupId) {
        ResponseData<List<MtNcCode>> responseData = new ResponseData<List<MtNcCode>>();
        try {
            responseData.setRows(repository.ncCodeGroupMemberQuery(tenantId, ncGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;

    }

    @ApiOperation(value = "ncCodeClosedRequiredValidate")
    @PostMapping(value = {"/closed-required/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncCodeClosedRequiredValidate(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody String ncCodeId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.ncCodeClosedRequiredValidate(tenantId, ncCodeId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncCodeEnableValidate")
    @PostMapping(value = {"/available/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncCodeEnableValidate(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody String ncCodeId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.ncCodeEnableValidate(tenantId, ncCodeId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncCodeComponentRequiredValidate")
    @PostMapping(value = {"/component-required/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncCodeComponentRequiredValidate(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody String ncCodeId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.ncCodeComponentRequiredValidate(tenantId, ncCodeId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncCodeConfirmRequiredValidate")
    @PostMapping(value = {"/confirm-required/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncCodeConfirmRequiredValidate(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody String ncCodeId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.ncCodeConfirmRequiredValidate(tenantId, ncCodeId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncCodeIncidentAutoCloseValidate")
    @PostMapping(value = {"/incident-auto-close/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncCodeIncidentAutoCloseValidate(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody String ncCodeId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.ncCodeIncidentAutoCloseValidate(tenantId, ncCodeId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncCodeAllowNoDispositionValidate")
    @PostMapping(value = {"/allow-none-disposition/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncCodeAllowNoDispositionValidate(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody String ncCodeId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.ncCodeAllowNoDispositionValidate(tenantId, ncCodeId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncCodePrimaryCodeAutoCloseValidate")
    @PostMapping(value = {"/primary-code/auto-close/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncCodePrimaryCodeAutoCloseValidate(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody String ncCodeId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.ncCodePrimaryCodeAutoCloseValidate(tenantId, ncCodeId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncCodeSecondaryCodeRequiredValidate")
    @PostMapping(value = {"/secondary-code/required/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncCodeSecondaryCodeRequiredValidate(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody String ncCodeId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.ncCodeSecondaryCodeRequiredValidate(tenantId, ncCodeId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncCodeValidAtAllOperationValidate")
    @PostMapping(value = {"/all-operation/available/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncCodeValidAtAllOperationValidate(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody String ncCodeId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.ncCodeValidAtAllOperationValidate(tenantId, ncCodeId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitNcCodePropertyQuery")
    @PostMapping(value = {"/nc/code/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcCodeVO1>> propertyLimitNcCodePropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtNcCodeVO dto) {
        ResponseData<List<MtNcCodeVO1>> result = new ResponseData<List<MtNcCodeVO1>>();
        try {
            result.setRows(repository.propertyLimitNcCodePropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "ncCodeAttrPropertyUpdate")
    @PostMapping(value = {"/property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> ncCodeAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.ncCodeAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
