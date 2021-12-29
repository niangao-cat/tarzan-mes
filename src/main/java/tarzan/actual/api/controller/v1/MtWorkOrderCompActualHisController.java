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
import tarzan.actual.domain.entity.MtWorkOrderCompActualHis;
import tarzan.actual.domain.repository.MtWorkOrderCompActualHisRepository;
import tarzan.actual.domain.vo.MtWoComponentActualVO7;

/**
 * 生产订单组件装配实绩历史，记录生产订单物料和组件实际装配情况变更历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtWorkOrderCompActualHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-work-order-comp-actual-his")
@Api(tags = "MtWorkOrderCompActualHis")
public class MtWorkOrderCompActualHisController extends BaseController {

    @Autowired
    private MtWorkOrderCompActualHisRepository repository;

    @ApiOperation(value = "woComponentActualLimitHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWoComponentActualVO7>> woComponentActualLimitHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String workOrderComponentActualId) {
        ResponseData<List<MtWoComponentActualVO7>> responseData = new ResponseData<List<MtWoComponentActualVO7>>();
        try {
            List<MtWoComponentActualVO7> rows =
                            this.repository.woComponentActualLimitHisQuery(tenantId, workOrderComponentActualId);
            responseData.setRows(rows);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitWoComponentActualHisBatchQuery")
    @PostMapping(value = {"/limit-event-list/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderCompActualHis>> eventLimitWoComponentActualHisBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtWorkOrderCompActualHis>> responseData = new ResponseData<List<MtWorkOrderCompActualHis>>();
        try {
            responseData.setRows(this.repository.eventLimitWoComponentActualHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitWoComponentActualHisQuery")
    @PostMapping(value = {"/limit-event/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderCompActualHis>> eventLimitWoComponentActualHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eventId) {
        ResponseData<List<MtWorkOrderCompActualHis>> responseData = new ResponseData<List<MtWorkOrderCompActualHis>>();
        try {
            responseData.setRows(this.repository.eventLimitWoComponentActualHisQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
