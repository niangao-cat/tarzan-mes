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
import tarzan.actual.domain.entity.MtEoRouterActualHis;
import tarzan.actual.domain.repository.MtEoRouterActualHisRepository;
import tarzan.actual.domain.vo.MtEoRouterActualHisVO;

/**
 * EO工艺路线实绩历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtEoRouterActualHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-router-actual-his")
@Api(tags = "MtEoRouterActualHis")
public class MtEoRouterActualHisController extends BaseController {
    @Autowired
    private MtEoRouterActualHisRepository repository;

    @ApiOperation(value = "eoRouterActualHisByEventQuery")
    @PostMapping(value = {"/limit-event/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouterActualHis>> eoRouterActualHisByEventQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eventId) {
        ResponseData<List<MtEoRouterActualHis>> responseData = new ResponseData<List<MtEoRouterActualHis>>();
        try {
            responseData.setRows(this.repository.eoRouterActualHisByEventQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterActualHisQuery")
    @PostMapping(value = {"/limit-eo/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouterActualHis>> eoRouterActualHisQuery(@PathVariable("organizationId") Long tenantId,
                                                                          @RequestBody String eoId) {
        ResponseData<List<MtEoRouterActualHis>> responseData = new ResponseData<List<MtEoRouterActualHis>>();
        try {
            responseData.setRows(this.repository.eoRouterActualHisQuery(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventAndEoRouterLimitEoRouterActualQuery")
    @PostMapping(value = {"/limit-event-router/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouterActualHis>> eventAndEoRouterLimitEoRouterActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoRouterActualHisVO condition) {
        ResponseData<List<MtEoRouterActualHis>> responseData = new ResponseData<List<MtEoRouterActualHis>>();
        try {
            responseData.setRows(this.repository.eventAndEoRouterLimitEoRouterActualQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
