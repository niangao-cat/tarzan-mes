package io.tarzan.common.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.api.dto.*;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 管理 API
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Api(tags = "MtExtendSettings")
@RestController("mtExtendSettingsController.v1")
@RequestMapping("/v1/{organizationId}/mt-extend-setting")
public class MtExtendSettingsController extends BaseController {

    @Autowired
    private MtExtendSettingsRepository repository;

    @Autowired
    private MtExtendSettingsService service;

    @ApiOperation(value = "customAttrQuery")
    @PostMapping(value = {"/attr/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendSettingsVO2>> customAttrQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendDTO5 dto) {
        ResponseData<List<MtExtendSettingsVO2>> result = new ResponseData<List<MtExtendSettingsVO2>>();
        try {
            result.setRows(repository.customAttrQuery(tenantId, dto.getAttrTable(), dto.getEnableFlag()));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "attrPropertyQuery")
    @PostMapping(value = {"/attr/value/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendAttrVO>> attrPropertyQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendVO dto) {
        ResponseData<List<MtExtendAttrVO>> result = new ResponseData<List<MtExtendAttrVO>>();
        try {
            result.setRows(repository.attrPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "attrPropertyBatchQuery")
    @PostMapping(value = {"/attr/value/batch-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendAttrVO1>> attrPropertyBatchQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendVO1 dto) {
        ResponseData<List<MtExtendAttrVO1>> result = new ResponseData<List<MtExtendAttrVO1>>();
        try {
            result.setRows(repository.attrPropertyBatchQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "attrTableWithLangQuery")
    @PostMapping(value = {"/attr/lang-value/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendAttrVO2>> attrTableWithLangQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendVO dto) {
        ResponseData<List<MtExtendAttrVO2>> result = new ResponseData<List<MtExtendAttrVO2>>();
        try {
            result.setRows(repository.attrTableWithLangQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "attrPropertyLimitKidQuery")
    @PostMapping(value = {"/attr/limit-property/key"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> attrPropertyLimitKidQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendVO2 dto) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.attrPropertyLimitKidQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "attrBatchPropertyLimitKidQuery")
    @PostMapping(value = {"/attr/limit-batch-property/key"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> attrBatchPropertyLimitKidQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendDTO2 dto) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.attrBatchPropertyLimitKidQuery(tenantId, dto.getTableName(), dto.getAttrs()));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "attrPropertyUpdate")
    @PostMapping(value = {"/attr/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> attrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendDTO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.attrPropertyUpdate(tenantId, dto.getTableName(), dto.getKeyId(), dto.getEventId(),
                            dto.getAttrs());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "attrPropertyBatchUpdate")
    @PostMapping(value = {"/attr/batch/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> attrPropertyBatchUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCommonExtendDTO4 dto) {
        ResponseData<List<String>> responseData = new ResponseData<>();
        try {
            responseData.setRows(repository.attrPropertyBatchUpdate(tenantId, dto.getTableName(), dto.getEventId(),
                            dto.getAttrPropertyList()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "attrHisBatchQuery")
    @PostMapping(value = {"/attr/limit-event/his"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendAttrHisVO>> attrHisBatchQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendAttrHisDTO dto) {
        ResponseData<List<MtExtendAttrHisVO>> result = new ResponseData<List<MtExtendAttrHisVO>>();
        try {
            result.setRows(repository.attrHisBatchQuery(tenantId, dto.getEventIds(), dto.getTableName()));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "attrHisQuery")
    @PostMapping(value = {"/limit-property/his"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendAttrHisVO>> attrHisQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendAttrHisDTO2 dto) {
        ResponseData<List<MtExtendAttrHisVO>> result = new ResponseData<List<MtExtendAttrHisVO>>();
        try {
            result.setRows(repository.attrHisQuery(tenantId, dto.getCondition(), dto.getTableName()));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "拓展属性查询(前台)")
    @GetMapping(value = {"/attr/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendAttrDTO>> attrQueryUi(@PathVariable("organizationId") Long tenantId,
                    MtExtendDTO3 dto) {
        ResponseData<List<MtExtendAttrDTO>> result = new ResponseData<List<MtExtendAttrDTO>>();
        try {
            result.setRows(service.attrQuery(tenantId, dto.getKid(), dto.getTableName()));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "拓展属性保存(前台)")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> attrSave(@PathVariable("organizationId") Long tenantId, @RequestBody MtExtendDTO4 dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            service.attrSave(tenantId, dto.getTableName(), dto.getKid(), null, dto.getAttrs());
            result.setRows(dto.getKid());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "拓展属性多语言查询(前台)")
    @GetMapping(value = {"/lang/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<MtMultiLanguageDTO2>> getLangValue(@PathVariable("organizationId") Long tenantId,
                    MtMultiLanguageDTO dto) {
        return Results.success(service.getLangValue(tenantId, dto));
    }

    @ApiOperation(value = "UI根据拓展表名查询拓展字段")
    @GetMapping(value = {"/limit-table/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendAttrDTO2>> queryExtendSettingsForUi(@PathVariable("organizationId") Long tenantId,
                    MtExtendAttrDTO4 dto, @ApiIgnore @SortDefault(value = MtExtendSettings.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<List<MtExtendAttrDTO2>> result = new ResponseData<List<MtExtendAttrDTO2>>();
        try {
            result.setRows(service.queryExtendSettingsForUi(tenantId, dto, pageRequest));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "UI查询扩展表下的扩展字段")
    @GetMapping(value = {"/extends/limit-table/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendSettings>> queryExtendsLimitTableForUi(
                    @PathVariable("organizationId") Long tenantId, String extendTableDescId) {
        ResponseData<List<MtExtendSettings>> result = new ResponseData<>();
        try {
            result.setRows(service.queryExtendsLimitTableForUi(tenantId, extendTableDescId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "UI批量保存拓展字段")
    @PostMapping(value = {"/save/property/batch/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> saveExtendSettingsBatchForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtExtendSettings> dto) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            service.saveExtendSettingsBatchForUi(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "UI保存拓展字段")
    @PostMapping(value = {"/save/property/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtExtendAttrDTO2> saveExtendSettingsForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendAttrDTO2 dto) {
        validObject(dto);
        ResponseData<MtExtendAttrDTO2> result = new ResponseData<MtExtendAttrDTO2>();
        try {
            result.setRows(service.saveExtendSettingsForUi(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "propertyLimitAttrPropertyQuery")
    @PostMapping(value = {"/property-limit/attr-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendVO9>> propertyLimitAttrPropertyQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendVO8 dto) {
        ResponseData<List<MtExtendVO9>> result = new ResponseData<List<MtExtendVO9>>();
        try {
            result.setRows(repository.propertyLimitAttrPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "attrPropertyBatchUpdateNew")
    @PostMapping(value = {"/attr/batch/save-new"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> attrPropertyBatchUpdateNew(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCommonExtendDTO5 dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            repository.attrPropertyBatchUpdateNew(tenantId, dto.getTableName(), dto.getEventId(),
                            dto.getAttrPropertyList());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


}
