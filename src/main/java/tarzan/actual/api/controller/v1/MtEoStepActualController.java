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
import tarzan.actual.api.dto.MtEoStepActualDTO;
import tarzan.actual.api.dto.MtEoStepActualDTO2;
import tarzan.actual.api.dto.MtEoStepActualDTO3;
import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.domain.entity.MtEoStepActualHis;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.actual.domain.vo.*;

/**
 * 执行作业-工艺路线步骤执行实绩 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtEoStepActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-step-actual")
@Api(tags = "MtEoStepActual")
public class MtEoStepActualController extends BaseController {

    @Autowired
    private MtEoStepActualRepository repository;

    @ApiOperation(value = "eoStepRelaxedFlowValidate")
    @PostMapping(value = {"/relaxed-flow/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStepRelaxedFlowValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoStepActualId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoStepRelaxedFlowValidate(tenantId, eoStepActualId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepRelaxedFlowBatchValidate")
    @PostMapping(value = {"/relaxed-flow/batch/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO41>> eoStepRelaxedFlowBatchValidate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eoStepActualIds) {
        ResponseData<List<MtEoStepActualVO41>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoStepRelaxedFlowBatchValidate(tenantId, eoStepActualIds));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepProductionResultAndHisUpdate")
    @PostMapping(value = {"/production/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStepProductionResultAndHisUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtEoStepActualHis param = new MtEoStepActualHis();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.eoStepProductionResultAndHisUpdate(tenantId, param));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "eoStepActualProcessedTimesUpdate")
    @PostMapping(value = {"/process-times/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO28> eoStepActualProcessedTimesUpdate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoStepActualVO15 vo) {
        ResponseData<MtEoStepActualVO28> responseData = new ResponseData<MtEoStepActualVO28>();
        try {
            responseData.setRows(this.repository.eoStepActualProcessedTimesUpdate(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepActualHoldTimesUpdate")
    @PostMapping(value = {"/hold-times/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO28> eoStepActualHoldTimesUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO9 vo) {
        ResponseData<MtEoStepActualVO28> responseData = new ResponseData<MtEoStepActualVO28>();
        try {
            responseData.setRows(this.repository.eoStepActualHoldTimesUpdate(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepActualAndHisCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO28> eoStepActualAndHisCreate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualDTO2 dto) {

        ResponseData<MtEoStepActualVO28> responseData = new ResponseData<MtEoStepActualVO28>();
        try {
            MtEoStepActualHis param = new MtEoStepActualHis();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.eoStepActualAndHisCreate(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stepActualLimitEoAndRouterGet")
    @PostMapping(value = {"/eo-router"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO1> stepActualLimitEoAndRouterGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoStepActualId) {
        ResponseData<MtEoStepActualVO1> responseData = new ResponseData<MtEoStepActualVO1>();
        try {
            responseData.setRows(this.repository.stepActualLimitEoAndRouterGet(tenantId, eoStepActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "eoWkcAndStepWipUpdate")
    @PostMapping(value = {"/wip/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO29> eoWkcAndStepWipUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO2 vo) {
        ResponseData<MtEoStepActualVO29> responseData = new ResponseData<MtEoStepActualVO29>();
        try {
            responseData.setRows(this.repository.eoWkcAndStepWipUpdate(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationLimitEoStepActualQuery")
    @PostMapping(value = {"/limit-operation/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO4>> operationLimitEoStepActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoStepActualVO3 vo) {
        ResponseData<List<MtEoStepActualVO4>> responseData = new ResponseData<List<MtEoStepActualVO4>>();
        try {
            responseData.setRows(this.repository.operationLimitEoStepActualQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepActualProcessedGet")
    @PostMapping(value = {"/processed"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO5> eoStepActualProcessedGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoStepActualId) {
        ResponseData<MtEoStepActualVO5> responseData = new ResponseData<MtEoStepActualVO5>();
        try {
            responseData.setRows(this.repository.eoStepActualProcessedGet(tenantId, eoStepActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepPeriodGet")
    @PostMapping(value = {"/period"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStepPeriodGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoStepActualId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoStepPeriodGet(tenantId, eoStepActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAllStepActualIsBypassedValidate")
    @PostMapping(value = {"/passed/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Boolean> eoAllStepActualIsBypassedValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoId) {
        ResponseData<Boolean> responseData = new ResponseData<Boolean>();
        try {
            responseData.setSuccess(this.repository.eoAllStepActualIsBypassedValidate(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepIsAnyPreVerify")
    @PostMapping(value = {"/pre/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Boolean> eoStepIsAnyPreVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO6 condition) {

        ResponseData<Boolean> responseData = new ResponseData<Boolean>();
        try {
            responseData.setSuccess(this.repository.eoStepIsAnyPreVerify(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoPreviousStepQuery")
    @PostMapping(value = {"/previous-step"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO7> eoPreviousStepQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoStepActualId) {
        ResponseData<MtEoStepActualVO7> responseData = new ResponseData<MtEoStepActualVO7>();
        try {
            responseData.setRows(this.repository.eoPreviousStepQuery(tenantId, eoStepActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "eoStepActualHoldTimesGet")
    @PostMapping(value = {"/hold-times"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO8> eoStepActualHoldTimesGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoStepActualId) {
        ResponseData<MtEoStepActualVO8> responseData = new ResponseData<MtEoStepActualVO8>();
        try {
            responseData.setRows(this.repository.eoStepActualHoldTimesGet(tenantId, eoStepActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActual> eoStepPropertyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoStepActualId) {
        ResponseData<MtEoStepActual> responseData = new ResponseData<MtEoStepActual>();
        try {
            responseData.setRows(this.repository.eoStepPropertyGet(tenantId, eoStepActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepPropertyBatchGet")
    @PostMapping(value = {"/property/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActual>> eoStepPropertyBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> eoStepActualId) {
        ResponseData<List<MtEoStepActual>> responseData = new ResponseData<List<MtEoStepActual>>();
        try {
            responseData.setRows(this.repository.eoStepPropertyBatchGet(tenantId, eoStepActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepProductionResultGet")
    @PostMapping(value = {"/production"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO5> eoStepProductionResultGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoStepActualId) {

        ResponseData<MtEoStepActualVO5> responseData = new ResponseData<MtEoStepActualVO5>();
        try {
            responseData.setRows(this.repository.eoStepProductionResultGet(tenantId, eoStepActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepBypassedFlagUpdate")
    @PostMapping(value = {"/bypass-flag/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO28> eoStepBypassedFlagUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO9 dto) {

        ResponseData<MtEoStepActualVO28> responseData = new ResponseData<MtEoStepActualVO28>();
        try {
            responseData.setRows(this.repository.eoStepBypassedFlagUpdate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoSubRouterReturnTypeGet")
    @PostMapping(value = {"/sub/return/type"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO11> eoSubRouterReturnTypeGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO10 dto) {
        ResponseData<MtEoStepActualVO11> responseData = new ResponseData<MtEoStepActualVO11>();
        try {
            responseData.setRows(this.repository.eoSubRouterReturnTypeGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepActualStatusGenerate")
    @PostMapping(value = {"/status"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStepActualStatusGenerate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoStepActualId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoStepActualStatusGenerate(tenantId, eoStepActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepActualExcessMaxProcessTimesValidate")
    @PostMapping(value = {"/max-process-times/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStepActualExcessMaxProcessTimesValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoStepActualId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoStepActualExcessMaxProcessTimesValidate(tenantId, eoStepActualId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "movingEventCreate")
    @PostMapping(value = {"/event/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> movingEventCreate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO16 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.movingEventCreate(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitStepActualQuery")
    @PostMapping(value = {"/limit-eo/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> eoLimitStepActualQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.eoLimitStepActualQuery(tenantId, eoId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAllStepActualIsCompletedValidate")
    @PostMapping(value = {"/all/completed/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO17> eoAllStepActualIsCompletedValidate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<MtEoStepActualVO17> responseData = new ResponseData<MtEoStepActualVO17>();
        try {
            responseData.setRows(this.repository.eoAllStepActualIsCompletedValidate(tenantId, eoId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepMoveIn")
    @PostMapping(value = {"/move-in"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStepMoveIn(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO20 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            this.repository.eoStepMoveIn(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepMoveInCancel")
    @PostMapping(value = {"/move-in-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepMoveInCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO20 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoStepMoveInCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepQueue")
    @PostMapping(value = {"/queue"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStepQueue(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO19 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoStepQueue(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepQueueCancel")
    @PostMapping(value = {"/queue-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepQueueCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO20 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoStepQueueCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoQueueProcess")
    @PostMapping(value = {"/queue/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoQueueProcess(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO21 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoQueueProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoQueueProcessCancel")
    @PostMapping(value = {"/queue/process-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoQueueProcessCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO21 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoQueueProcessCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepMoveOut")
    @PostMapping(value = {"/move-out"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepMoveOut(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO22 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoStepMoveOut(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepMoveOutCancel")
    @PostMapping(value = {"/move-out-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepMoveOutCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO22 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoStepMoveOutCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoNextStepMoveInProcess")
    @PostMapping(value = {"/next-step/move-in/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoNextStepMoveInProcess(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoRouterActualVO15 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoNextStepMoveInProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoNextStepMoveInProcessCancel")
    @PostMapping(value = {"/next-step/move-in/process-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoNextStepMoveInProcessCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoRouterActualVO19 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoNextStepMoveInProcessCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepActualProcessedTimesGet")
    @PostMapping(value = {"/processed/times"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Long> eoStepActualProcessedTimesGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoStepActualId) {
        ResponseData<Long> responseData = new ResponseData<Long>();
        try {
            responseData.setRows(this.repository.eoStepActualProcessedTimesGet(tenantId, eoStepActualId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitEoStepActualQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitEoStepActualQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualDTO3 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtEoStepActual param = new MtEoStepActual();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.propertyLimitEoStepActualQuery(tenantId, param));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepCompletedValidate")
    @PostMapping(value = {"/completed/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStepCompletedValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoStepActualId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoStepCompletedValidate(tenantId, eoStepActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepGroupCompletedValidate")
    @PostMapping(value = {"/group/completed/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStepGroupCompletedValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO10 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoStepGroupCompletedValidate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepGroupCompletedBatchValidate")
    @PostMapping(value = {"/group/completed/batch/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO64>> eoStepGroupCompletedBatchValidate(
            @PathVariable("organizationId") Long tenantId, @RequestBody List<MtEoStepActualVO10> dtos) {
        ResponseData<List<MtEoStepActualVO64>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoStepGroupCompletedBatchValidate(tenantId, dtos));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepCompletedBatchValidate")
    @PostMapping(value = {"/completed/validate/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO54>> eoStepCompletedBatchValidate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eoStepActualIds) {
        ResponseData<List<MtEoStepActualVO54>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoStepCompletedBatchValidate(tenantId, eoStepActualIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepBypassed")
    @PostMapping(value = {"/bypassed"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepBypassed(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO24 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoStepBypassed(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepBypassedCancel")
    @PostMapping(value = {"/bypassed-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepBypassedCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO24 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoStepBypassedCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "relaxedFlowEoRouterAllStepCompletedVerify")
    @PostMapping(value = {"/all-step/completed/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> relaxedFlowEoRouterAllStepCompletedVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoRouterActualId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.relaxedFlowEoRouterAllStepCompletedVerify(tenantId, eoRouterActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoOperationAndNcLimitCurrentStepGet")
    @PostMapping(value = {"/limit-op-nc/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoStepActualVO26> eoOperationAndNcLimitCurrentStepGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoStepActualVO25 dto) {
        ResponseData<MtEoStepActualVO26> responseData = new ResponseData<MtEoStepActualVO26>();
        try {
            responseData.setRows(this.repository.eoOperationAndNcLimitCurrentStepGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepAndWkcQueue")
    @PostMapping(value = {"/wkc/queue"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStepAndWkcQueue(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO19 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoStepAndWkcQueue(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepAndWkcQueueCancel")
    @PostMapping(value = {"/wkc/queue-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepAndWkcQueueCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO20 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoStepAndWkcQueueCancel(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationLimitEoForNonDispatchGet")
    @PostMapping(value = {"/limit-eo/non-dispatch/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> operationLimitEoForNonDispatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO27 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.operationLimitEoForNonDispatchGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitEoStepActualPropertyQuery")
    @PostMapping(value = {"/limit-eo/step-actual/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO31>> propertyLimitEoStepActualPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoStepActualVO30 vo) {
        ResponseData<List<MtEoStepActualVO31>> responseData = new ResponseData<List<MtEoStepActualVO31>>();
        try {
            responseData.setRows(this.repository.propertyLimitEoStepActualPropertyQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepActualBatchSplit")
    @PostMapping(value = {"/wkc/batch-split"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepActualBatchSplit(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO32 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoWkcAndStepActualBatchSplit(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitStepActualBatchQuery")
    @PostMapping(value = {"/limit/eo/batch/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO34>> eoLimitStepActualBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eoIdList) {
        ResponseData<List<MtEoStepActualVO34>> responseData = new ResponseData<List<MtEoStepActualVO34>>();
        try {
            responseData.setRows(this.repository.eoLimitStepActualBatchQuery(tenantId, eoIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepActualProcessedBatchGet")
    @PostMapping(value = {"/processed/batch/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO35>> eoStepActualProcessedBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eoStepActualIdList) {
        ResponseData<List<MtEoStepActualVO35>> responseData = new ResponseData<List<MtEoStepActualVO35>>();
        try {
            responseData.setRows(this.repository.eoStepActualProcessedBatchGet(tenantId, eoStepActualIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAndStepLimitStepActualBatchQuery")
    @PostMapping(value = {"/eo-step/limit/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO36>> eoAndStepLimitStepActualBatchQuery(
                    @PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtEoStepActualVO37> eoMessageList) {
        ResponseData<List<MtEoStepActualVO36>> responseData = new ResponseData<List<MtEoStepActualVO36>>();
        try {
            responseData.setRows(this.repository.eoAndStepLimitStepActualBatchQuery(tenantId, eoMessageList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stepActualLimitEoAndRouterBatchGet")
    @PostMapping(value = {"/eo-router/batch/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO1>> stepActualLimitEoAndRouterBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eoStepActualIds) {
        ResponseData<List<MtEoStepActualVO1>> responseData = new ResponseData<List<MtEoStepActualVO1>>();
        try {
            responseData.setRows(this.repository.stepActualLimitEoAndRouterBatchGet(tenantId, eoStepActualIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterLimitStepActualBatchQuery")
    @PostMapping(value = {"/limit-eo-router/batch/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO38>> eoRouterLimitStepActualBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<MtEoStepActualVO14> eoRouterList) {
        ResponseData<List<MtEoStepActualVO38>> responseData = new ResponseData<List<MtEoStepActualVO38>>();
        try {
            responseData.setRows(this.repository.eoRouterLimitStepActualBatchQuery(tenantId, eoRouterList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepBatchMoveIn")
    @PostMapping(value = {"/move-in/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO52>> eoStepBatchMoveIn(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO45 dto) {
        ResponseData<List<MtEoStepActualVO52>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoStepBatchMoveIn(tenantId, dto));

        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoNextStepMoveInBatchProcess")
    @PostMapping(value = {"/next-step/move-in/batch-process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoNextStepMoveInBatchProcess(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoRouterActualVO42 dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.eoNextStepMoveInBatchProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepBatchQueue")
    @PostMapping(value = {"/batch-queue"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO52>> eoStepBatchQueue(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO51 dto) {
        ResponseData<List<MtEoStepActualVO52>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoStepBatchQueue(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepBatchComplete")
    @PostMapping(value = {"/complete/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepBatchComplete(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO61 vo) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.eoStepBatchComplete(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepBatchMoveOut")
    @PostMapping(value = {"/move-out/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepBatchMoveOut(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO53 dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.eoStepBatchMoveOut(tenantId, dto);

        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoQueueBatchProcess")
    @PostMapping(value = {"/batch-queue/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoQueueBatchProcess(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO50 vo) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.eoQueueBatchProcess(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepActualExcessMaxProcessTimesBatchValidate")
    @PostMapping(value = {"/max-process-times/batch/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO60>> eoStepActualExcessMaxProcessTimesBatchValidate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eoStepActualIds) {
        ResponseData<List<MtEoStepActualVO60>> responseData = new ResponseData<>();
        try {
            responseData.setRows(
                            this.repository.eoStepActualExcessMaxProcessTimesBatchValidate(tenantId, eoStepActualIds));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepQueueBatchCancel")
    @PostMapping(value = {"/batch/queue-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepQueueBatchCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepActualVO56 dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.eoStepQueueBatchCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepProductionActualBatchUpdate")
    @PostMapping(value = {"/batch/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualVO46>> eoStepProductionActualBatchUpdate(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtEoStepActualVO40 dto) {
        ResponseData<List<MtEoStepActualVO46>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoStepProductionActualBatchUpdate(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
