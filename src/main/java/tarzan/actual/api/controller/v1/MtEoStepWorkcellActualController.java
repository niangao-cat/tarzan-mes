package tarzan.actual.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.actual.domain.entity.MtEoStepWorkcellActual;
import tarzan.actual.domain.repository.MtEoStepWorkcellActualRepository;
import tarzan.actual.domain.vo.*;

/**
 * 执行作业-工艺路线步骤执行明细 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtEoStepWorkcellActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-step-workcell-actual")
@Api(tags = "MtEoStepWorkcellActual")
public class MtEoStepWorkcellActualController extends BaseController {

    @Autowired
    private MtEoStepWorkcellActualRepository repository;

    @ApiOperation(value = "eoWkcProductionResultAndHisUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepWorkcellActualVO6> eoWkcProductionResultAndHisUpdate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoStepWorkcellActualVO1 vo) {
        ResponseData<MtEoStepWorkcellActualVO6> responseData = new ResponseData<MtEoStepWorkcellActualVO6>();
        try {
            responseData.setRows(this.repository.eoWkcProductionResultAndHisUpdate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcActualAndHisCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepWorkcellActualVO5> eoWkcActualAndHisCreate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoStepWorkcellActualVO2 vo) {
        ResponseData<MtEoStepWorkcellActualVO5> responseData = new ResponseData<MtEoStepWorkcellActualVO5>();
        try {
            responseData.setRows(this.repository.eoWkcActualAndHisCreate(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcProductionResultGet")
    @PostMapping(value = {"/wkc/production"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWorkcellActual>> eoWkcProductionResultGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoStepWorkcellActualVO3 vo) {
        ResponseData<List<MtEoStepWorkcellActual>> responseData = new ResponseData<List<MtEoStepWorkcellActual>>();
        try {
            responseData.setRows(this.repository.eoWkcProductionResultGet(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcPeriodGet")
    @PostMapping(value = {"/wkc/period"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoWkcPeriodGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepWorkcellActualVO4 condition) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoWkcPeriodGet(tenantId, condition));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitEoStepWkcActualPropertyQuery")
    @PostMapping(value = {"/wkc/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWorkcellActualVO8>> propertyLimitEoStepWkcActualPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoStepWorkcellActualVO7 condition) {
        ResponseData<List<MtEoStepWorkcellActualVO8>> responseData =
                        new ResponseData<List<MtEoStepWorkcellActualVO8>>();
        try {
            responseData.setRows(this.repository.propertyLimitEoStepWkcActualPropertyQuery(tenantId, condition));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "propertyLimitEoStepWkcActualPropertyBatchQuery")
    @PostMapping(value = {"/wkc/property/batch/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWorkcellActualVO14>> propertyLimitEoStepWkcActualPropertyBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoStepWorkcellActualVO13 condition) {
        ResponseData<List<MtEoStepWorkcellActualVO14>> responseData =
                        new ResponseData<List<MtEoStepWorkcellActualVO14>>();
        try {
            responseData.setRows(this.repository.propertyLimitEoStepWkcActualPropertyBatchQuery(tenantId, condition));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcProductionResultBatchGet")
    @PostMapping(value = {"/wkc/production/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWorkcellActual>> eoWkcProductionResultBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<MtEoStepWorkcellActualVO3> vo) {
        ResponseData<List<MtEoStepWorkcellActual>> responseData = new ResponseData<List<MtEoStepWorkcellActual>>();
        try {
            responseData.setRows(this.repository.eoWkcProductionResultBatchGet(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcProductionResultAndHisBatchUpdate")
    @PostMapping(value = {"/batch/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWorkcellActualVO15>> eoWkcProductionResultAndHisBatchUpdate(
                    @PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtEoStepWorkcellActualVO1> actualList) {
        ResponseData<List<MtEoStepWorkcellActualVO15>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoWkcProductionResultAndHisBatchUpdate(tenantId, actualList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
