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
import tarzan.dispatch.domain.entity.MtEoDispatchProcess;
import tarzan.dispatch.domain.repository.MtEoDispatchProcessRepository;
import tarzan.dispatch.domain.vo.*;

/**
 * 调度过程处理表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:58
 */
@RestController("mtEoDispatchProcessController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-dispatch-process")
@Api(tags = "MtEoDispatchProcess")
public class MtEoDispatchProcessController extends BaseController {

    @Autowired
    private MtEoDispatchProcessRepository repository;

    @ApiOperation(value = "wkcShiftLimitDispatchedProcessEoQuery")
    @PostMapping(value = "/property/limit-wkc-shift", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchProcess>> wkcShiftLimitDispatchedProcessEoQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchProcessVO1 vo) {
        ResponseData<List<MtEoDispatchProcess>> responseData = new ResponseData<List<MtEoDispatchProcess>>();
        try {
            responseData.setRows(this.repository.wkcShiftLimitDispatchedProcessEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "dispatchedEoUpdate")
    @PostMapping(value = "/save/limit-wkc-shift", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> dispatchedEoUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchProcessVO2 vo,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.dispatchedEoUpdate(tenantId, vo, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "toBeDispatchedEoDispatchableQtyGet")
    @PostMapping(value = "/dispatchable-qty", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> toBeDispatchedEoDispatchableQtyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchProcessVO8 dto) {
        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.repository.toBeDispatchedEoDispatchableQtyGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationLimitToBeDispatchedEoQuery")
    @PostMapping(value = "/dispatched-qty/limit-operation", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchProcessVO4>> operationLimitToBeDispatchedEoQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchProcessVO3 vo) {
        ResponseData<List<MtEoDispatchProcessVO4>> responseData = new ResponseData<List<MtEoDispatchProcessVO4>>();
        try {
            responseData.setRows(this.repository.operationLimitToBeDispatchedEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "planTimeLimitToBeDispatchedEoQuery")
    @PostMapping(value = "/dispatched-qty/limit-plan-time", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchProcessVO4>> planTimeLimitToBeDispatchedEoQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchProcessVO5 vo) {
        ResponseData<List<MtEoDispatchProcessVO4>> responseData = new ResponseData<List<MtEoDispatchProcessVO4>>();
        try {
            responseData.setRows(this.repository.planTimeLimitToBeDispatchedEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcShiftLimitToBeDispatchedEoQuery")
    @PostMapping(value = "/dispatched-qty/limit-wkc-shift", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchProcessVO4>> wkcShiftLimitToBeDispatchedEoQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchProcessVO6 vo) {
        ResponseData<List<MtEoDispatchProcessVO4>> responseData = new ResponseData<List<MtEoDispatchProcessVO4>>();
        try {
            responseData.setRows(this.repository.wkcShiftLimitToBeDispatchedEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcShiftPeriodLimitToBeDispatchedEoQuery")
    @PostMapping(value = "/dispatched-qty/limit-wkc-shift-period", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchProcessVO4>> wkcShiftPeriodLimitToBeDispatchedEoQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchProcessVO7 vo) {
        ResponseData<List<MtEoDispatchProcessVO4>> responseData = new ResponseData<List<MtEoDispatchProcessVO4>>();
        try {
            responseData.setRows(this.repository.wkcShiftPeriodLimitToBeDispatchedEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitDispatchedProcessPropertyQuery")
    @PostMapping(value = "/limit-property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchProcessVO10>> propertyLimitDispatchedProcessPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchProcessVO9 vo) {
        ResponseData<List<MtEoDispatchProcessVO10>> responseData = new ResponseData<List<MtEoDispatchProcessVO10>>();
        try {
            responseData.setRows(this.repository.propertyLimitDispatchedProcessPropertyQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitDispatchedProcessPropertyBatchQuery")
    @PostMapping(value = "/limit-batch-property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchProcessVO10>> propertyLimitDispatchedProcessPropertyBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchProcessVO13 vo) {
        ResponseData<List<MtEoDispatchProcessVO10>> responseData = new ResponseData<List<MtEoDispatchProcessVO10>>();
        try {
            responseData.setRows(this.repository.propertyLimitDispatchedProcessPropertyBatchQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "rangeLimitToBeDispatchedEoQuery")
    @PostMapping(value = "/dispatch/eo/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchProcessVO12>> rangeLimitToBeDispatchedEoQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchProcessVO11 vo) {
        ResponseData<List<MtEoDispatchProcessVO12>> responseData = new ResponseData<List<MtEoDispatchProcessVO12>>();
        try {
            responseData.setRows(this.repository.rangeLimitToBeDispatchedEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "dispatchedEoAssignQtyBatchGet")
    @PostMapping(value = "/dispatched/eo/assign/qty/batch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchProcessVO14>> dispatchedEoAssignQtyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchProcessVO15 vo) {
        ResponseData<List<MtEoDispatchProcessVO14>> responseData = new ResponseData<List<MtEoDispatchProcessVO14>>();
        try {
            responseData.setRows(this.repository.dispatchedEoAssignQtyBatchGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
