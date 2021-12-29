package tarzan.actual.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.actual.api.dto.MtInstructionActualDTO;
import tarzan.actual.api.dto.MtInstructionActualDTO2;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.*;

/**
 * 指令实绩表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtInstructionActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-instruction-actual")
@Api(tags = "MtInstructionActual")
public class MtInstructionActualController extends BaseController {
    @Autowired
    private MtInstructionActualRepository repository;

    @ApiOperation(value = "instructionActualUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionActualVO1> instructionActualUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInstructionActualDTO dto) {
        ResponseData<MtInstructionActualVO1> responseData = new ResponseData<MtInstructionActualVO1>();
        try {
            MtInstructionActualVO param = new MtInstructionActualVO();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.instructionActualUpdate(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "instructionActualPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionActual> instructionActualPropertyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String actualId) {
        ResponseData<MtInstructionActual> responseData = new ResponseData<MtInstructionActual>();
        try {
            responseData.setRows(repository.instructionActualPropertyGet(tenantId, actualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "instructionLimitActualBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionActual>> instructionLimitActualBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> instructionIdList) {
        ResponseData<List<MtInstructionActual>> responseData = new ResponseData<List<MtInstructionActual>>();
        try {
            responseData.setRows(repository.instructionLimitActualBatchGet(tenantId, instructionIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitInstructionActualQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitInstructionActualQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInstructionActualDTO2 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtInstructionActual param = new MtInstructionActual();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.propertyLimitInstructionActualQuery(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "instructionLimitActualPropertyGet")
    @PostMapping(value = {"/limit-instruction"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionActual>> instructionLimitActualPropertyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String instructionId) {
        ResponseData<List<MtInstructionActual>> responseData = new ResponseData<List<MtInstructionActual>>();
        try {
            responseData.setRows(repository.instructionLimitActualPropertyGet(tenantId, instructionId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "instructionActualAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property-update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> instructionActualAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendVO10 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            this.repository.instructionActualAttrPropertyUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionLimitActualPropertyBatchGet")
    @PostMapping("/limit/batch/get")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionActualVO2>> instructionLimitActualPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody List<String> instructionIdList) {
        ResponseData<List<MtInstructionActualVO2>> responseData = new ResponseData<List<MtInstructionActualVO2>>();
        try {
            responseData.setRows(repository.instructionLimitActualPropertyBatchGet(tenantId, instructionIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("instructionActualPropertyBatchGet")
    @PostMapping("/batch/get")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionActualVO3>> instructionActualPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> actualIdList) {
        ResponseData<List<MtInstructionActualVO3>> responseData = new ResponseData<>();
        try {
            responseData.setRows(repository.instructionActualPropertyBatchGet(tenantId, actualIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("instructionActualBatchUpdate")
    @PostMapping("/batch/update")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionActualVO5>> instructionActualBatchUpdate(
                    @PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody List<MtInstructionActualVO4> dtos) {
        ResponseData<List<MtInstructionActualVO5>> responseData = new ResponseData<>();
        try {
            responseData.setRows(repository.instructionActualBatchUpdate(tenantId, dtos));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
