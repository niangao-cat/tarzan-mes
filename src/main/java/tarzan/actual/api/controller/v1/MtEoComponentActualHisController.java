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
import tarzan.actual.domain.entity.MtEoComponentActualHis;
import tarzan.actual.domain.repository.MtEoComponentActualHisRepository;
import tarzan.actual.domain.vo.MtEoComponentActualVO3;

/**
 * 执行作业组件装配实绩历史，记录执行作业物料和组件实际装配情况变更历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtEoComponentActualHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-component-actual-his")
@Api(tags = "MtEoComponentActualHis")
public class MtEoComponentActualHisController extends BaseController {

    @Autowired
    private MtEoComponentActualHisRepository repository;

    @ApiOperation(value = "eventLimitEoComponentActualHisQuery")
    @PostMapping(value = {"/limit-event/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActualHis>> eventLimitEoComponentActualHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eventId) {
        ResponseData<List<MtEoComponentActualHis>> responseData = new ResponseData<List<MtEoComponentActualHis>>();
        try {
            responseData.setRows(this.repository.eventLimitEoComponentActualHisQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentActualLimitHisQuery")
    @PostMapping(value = {"/limit-component/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActualVO3>> eoComponentActualLimitHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eoComponentActualId) {
        ResponseData<List<MtEoComponentActualVO3>> responseData = new ResponseData<List<MtEoComponentActualVO3>>();
        try {
            responseData.setRows(this.repository.eoComponentActualLimitHisQuery(tenantId, eoComponentActualId));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitEoComponentActualHisBatchQuery")
    @PostMapping(value = {"/limit-event-list/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActualHis>> eventLimitEoComponentActualHisBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtEoComponentActualHis>> responseData = new ResponseData<List<MtEoComponentActualHis>>();
        try {
            responseData.setRows(this.repository.eventLimitEoComponentActualHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }
}
