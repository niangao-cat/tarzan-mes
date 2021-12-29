package tarzan.actual.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.actual.domain.entity.MtAssembleConfirmActualHis;
import tarzan.actual.domain.repository.MtAssembleConfirmActualHisRepository;
import tarzan.actual.domain.vo.MtAssembleConfirmActualHisVO;
import tarzan.actual.domain.vo.MtAssembleConfirmActualHisVO1;

/**
 * 装配确认实绩历史，指示执行作业组件材料的装配和确认历史记录情况 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtAssembleConfirmActualHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-assemble-confirm-actual-his")
@Api(tags = "MtAssembleConfirmActualHis")
public class MtAssembleConfirmActualHisController extends BaseController {

    @Autowired
    private MtAssembleConfirmActualHisRepository repository;

    @ApiOperation(value = "assembleConfirmActualLimitHisQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleConfirmActualHisVO1>> assembleConfirmActualLimitHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleConfirmActualHisVO dto) {
        ResponseData<List<MtAssembleConfirmActualHisVO1>> responseData =
                        new ResponseData<List<MtAssembleConfirmActualHisVO1>>();
        try {
            responseData.setRows(this.repository.assembleConfirmActualLimitHisQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitAssembleConfirmActualHisBatchQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleConfirmActualHis>> eventLimitAssembleConfirmActualHisBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtAssembleConfirmActualHis>> responseData =
                        new ResponseData<List<MtAssembleConfirmActualHis>>();
        try {
            responseData.setRows(this.repository.eventLimitAssembleConfirmActualHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitAssembleConfirmActualHisQuery")
    @PostMapping(value = {"/event-limit/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleConfirmActualHis>> eventLimitAssembleConfirmActualHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eventId) {
        ResponseData<List<MtAssembleConfirmActualHis>> responseData =
                        new ResponseData<List<MtAssembleConfirmActualHis>>();
        try {
            responseData.setRows(this.repository.eventLimitAssembleConfirmActualHisQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
