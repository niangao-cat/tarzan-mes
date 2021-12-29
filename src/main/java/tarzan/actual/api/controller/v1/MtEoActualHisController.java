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
import tarzan.actual.domain.entity.MtEoActualHis;
import tarzan.actual.domain.repository.MtEoActualHisRepository;
import tarzan.actual.domain.vo.MtEoActualHisVO1;
import tarzan.actual.domain.vo.MtEoActualHisVO2;
import tarzan.actual.domain.vo.MtEoActualHisVO3;

/**
 * 执行作业实绩历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtEoActualHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-actual-his")
@Api(tags = "MtEoActualHis")
public class MtEoActualHisController extends BaseController {
    @Autowired
    private MtEoActualHisRepository repository;

    @ApiOperation(value = "eventLimitEoActualHisQuery")
    @PostMapping(value = {"/limit-event/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoActualHis>> eventLimitEoActualHisQuery(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestBody String eventId) {
        ResponseData<List<MtEoActualHis>> responseData = new ResponseData<List<MtEoActualHis>>();
        try {
            responseData.setRows(this.repository.eventLimitEoActualHisQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitEoActualHisBatchQuery")
    @PostMapping(value = {"/limit-event-list/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoActualHis>> eventLimitEoActualHisBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtEoActualHis>> responseData = new ResponseData<List<MtEoActualHis>>();
        try {
            responseData.setRows(this.repository.eventLimitEoActualHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoActualHisPropertyQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoActualHis>> eoActualHisPropertyQuery(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtEoActualHisVO1 dto) {
        ResponseData<List<MtEoActualHis>> responseData = new ResponseData<List<MtEoActualHis>>();
        try {
            responseData.setRows(this.repository.eoActualHisPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitEoActualHisQuery")
    @PostMapping(value = {"/limit-eo/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoActualHisVO2>> eoLimitEoActualHisQuery(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestBody MtEoActualHisVO3 dto) {
        ResponseData<List<MtEoActualHisVO2>> responseData = new ResponseData<List<MtEoActualHisVO2>>();
        try {
            responseData.setRows(this.repository.eoLimitEoActualHisQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
