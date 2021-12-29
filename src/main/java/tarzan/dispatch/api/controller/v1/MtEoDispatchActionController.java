package tarzan.dispatch.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.actual.domain.vo.MtEoStepActualVO37;
import tarzan.dispatch.domain.entity.MtEoDispatchAction;
import tarzan.dispatch.domain.repository.MtEoDispatchActionRepository;
import tarzan.dispatch.domain.vo.*;

/**
 * 调度结果执行表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:56
 */
@RestController("mtEoDispatchActionController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-dispatch-action")
@Api(tags = "MtEoDispatchAction")
public class MtEoDispatchActionController extends BaseController {

    @Autowired
    private MtEoDispatchActionRepository repository;

    @ApiOperation(value = "wkcShiftLimitDispatchedPublishedEoQuery")
    @PostMapping(value = "/dispatched/published/limit-wkc-shift", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchAction>> wkcShiftLimitDispatchedPublishedEoQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchActionVO1 vo) {
        ResponseData<List<MtEoDispatchAction>> responseData = new ResponseData<List<MtEoDispatchAction>>();
        try {
            responseData.setRows(this.repository.wkcShiftLimitDispatchedPublishedEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "dispatchedEoRevocableQtyGet")
    @PostMapping(value = "/dispatched/revocable-qty", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> dispatchedEoRevocableQtyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchActionVO2 vo) {
        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.repository.dispatchedEoRevocableQtyGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcShiftLimitDispatchedEoQuery")
    @PostMapping(value = "/dispatched/limit-wkc-shift", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchActionVO3>> wkcShiftLimitDispatchedEoQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchActionVO1 vo) {
        ResponseData<List<MtEoDispatchActionVO3>> responseData = new ResponseData<List<MtEoDispatchActionVO3>>();
        try {
            responseData.setRows(this.repository.wkcShiftLimitDispatchedEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcShiftLimitDispatchedOngoingEoQuery")
    @PostMapping(value = "/dispatched/ongoing/limit-wkc-shift", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchActionVO3>> wkcShiftLimitDispatchedOngoingEoQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchActionVO1 vo) {
        ResponseData<List<MtEoDispatchActionVO3>> responseData = new ResponseData<List<MtEoDispatchActionVO3>>();
        try {
            responseData.setRows(this.repository.wkcShiftLimitDispatchedOngoingEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcShiftPeriodLimitDispatchedPublishedEoQuery")
    @PostMapping(value = "/dispatched/published/limit-wkc-shift-period", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchAction>> wkcShiftPeriodLimitDispatchedPublishedEoQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchActionVO4 vo) {
        ResponseData<List<MtEoDispatchAction>> responseData = new ResponseData<List<MtEoDispatchAction>>();
        try {
            responseData.setRows(this.repository.wkcShiftPeriodLimitDispatchedPublishedEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoOnStandbyQuery")
    @PostMapping(value = "/dispatched/standby", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchActionVO5>> eoOnStandbyQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchActionVO1 vo) {
        ResponseData<List<MtEoDispatchActionVO5>> responseData = new ResponseData<List<MtEoDispatchActionVO5>>();
        try {
            responseData.setRows(this.repository.eoOnStandbyQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "dispatchedEoAssignQtyGet")
    @PostMapping(value = "/dispatched/qty", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoDispatchActionVO7> dispatchedEoAssignQtyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchActionVO2 vo) {
        ResponseData<MtEoDispatchActionVO7> responseData = new ResponseData<MtEoDispatchActionVO7>();
        try {
            responseData.setRows(this.repository.dispatchedEoAssignQtyGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoDispatchVerify")
    @PostMapping(value = "/dispatch/verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoDispatchVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchActionVO8 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoDispatchVerify(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoDispatch")
    @PostMapping(value = "/dispatch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoDispatch(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchActionVO9 vo) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoDispatch(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoDispatchCancelVerify")
    @PostMapping(value = "/dispatch/verify/cancel", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoDispatchCancelVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchActionVO6 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoDispatchCancelVerify(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "toBeDispatchedEoUnassignQtyGet")
    @PostMapping(value = "/need-to-dispatch/unassign-qty", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoDispatchActionVO10> toBeDispatchedEoUnassignQtyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchActionVO16 dto) {
        ResponseData<MtEoDispatchActionVO10> responseData = new ResponseData<MtEoDispatchActionVO10>();
        try {
            responseData.setRows(this.repository.toBeDispatchedEoUnassignQtyGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "dispatchedEoPriorityGenerate")
    @PostMapping(value = "/priority/generate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Long> dispatchedEoPriorityGenerate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchActionVO1 vo) {
        ResponseData<Long> responseData = new ResponseData<Long>();
        try {
            responseData.setRows(this.repository.dispatchedEoPriorityGenerate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "processLimitDispatchActionAndHistoryUpdate")
    @PostMapping(value = "/save/limit-process", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoDispatchActionVO17> processLimitDispatchActionAndHistoryUpdate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eoDispatchProcessId) {
        ResponseData<MtEoDispatchActionVO17> responseData = new ResponseData<MtEoDispatchActionVO17>();
        try {
            responseData.setRows(
                            this.repository.processLimitDispatchActionAndHistoryUpdate(tenantId, eoDispatchProcessId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoDispatchPublish")
    @PostMapping(value = "/publish", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoDispatchPublish(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchActionVO20 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoDispatchPublish(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoDispatchCancel")
    @PostMapping(value = "/dispatch/cancel", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoDispatchCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchActionVO11 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoDispatchCancel(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "sequenceLimitPriorityAdjust")
    @PostMapping(value = "/priority-adjust/limit-sequence", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchActionVO12>> sequenceLimitPriorityAdjust(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<MtEoDispatchActionVO12> dto) {
        ResponseData<List<MtEoDispatchActionVO12>> responseData = new ResponseData<List<MtEoDispatchActionVO12>>();
        try {
            responseData.setRows(repository.sequenceLimitPriorityAdjust(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "sequenceLimitDispatchedEoPriorityAdjust")
    @PostMapping(value = "/dispatched/priority-adjust/limit-sequence", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> sequenceLimitDispatchedEoPriorityAdjust(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtEoDispatchActionVO15> dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.sequenceLimitDispatchedEoPriorityAdjust(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitDispatchStrategyGet")
    @PostMapping(value = {"/limit-eo/strategy"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoDispatchActionVO24> eoLimitDispatchStrategyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<MtEoDispatchActionVO24> responseData = new ResponseData<MtEoDispatchActionVO24>();
        try {
            responseData.setRows(this.repository.eoLimitDispatchStrategyGet(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitDispatchedActionPropertyQuery")
    @PostMapping(value = {"/limit-dispatch-action/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchActionVO19>> propertyLimitDispatchedActionPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchActionVO18 vo) {
        ResponseData<List<MtEoDispatchActionVO19>> responseData = new ResponseData<List<MtEoDispatchActionVO19>>();
        try {
            responseData.setRows(this.repository.propertyLimitDispatchedActionPropertyQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitDispatchedActionPropertyBatchQuery")
    @PostMapping(value = {"/limit-batch-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchActionVO19>> propertyLimitDispatchedActionPropertyBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchActionVO21 vo) {
        ResponseData<List<MtEoDispatchActionVO19>> responseData = new ResponseData<List<MtEoDispatchActionVO19>>();
        try {
            responseData.setRows(this.repository.propertyLimitDispatchedActionPropertyBatchQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitDispatchStrategyBatchGet")
    @PostMapping(value = {"/limit-eo/strategy-batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoDispatchActionVO24> eoLimitDispatchStrategyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eoIds) {
        ResponseData<MtEoDispatchActionVO24> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoLimitDispatchStrategyBatchGet(tenantId, eoIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "toBeDispatchedEoUnassignQtyBatchGet")
    @PostMapping(value = {"/need-to-dispatch/unassign-qty-batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchActionVO23>> toBeDispatchedEoUnassignQtyBatchGet(
                    @PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtEoStepActualVO37> eoMessageList) {
        ResponseData<List<MtEoDispatchActionVO23>> responseData = new ResponseData<List<MtEoDispatchActionVO23>>();
        try {
            responseData.setRows(this.repository.toBeDispatchedEoUnassignQtyBatchGet(tenantId, eoMessageList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "productionLineLimitDispatchStrategyGet")
    @PostMapping(value = {"/limit/production/strategy/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoDispatchActionVO24> productionLineLimitDispatchStrategyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String productionLineId) {
        ResponseData<MtEoDispatchActionVO24> responseData = new ResponseData<MtEoDispatchActionVO24>();
        try {
            responseData.setRows(this.repository.productionLineLimitDispatchStrategyGet(tenantId, productionLineId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "dispatchActionDelete")
    @PostMapping(value = "/action/delete", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> dispatchActionDelete(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchActionVO20 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.dispatchActionDelete(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
