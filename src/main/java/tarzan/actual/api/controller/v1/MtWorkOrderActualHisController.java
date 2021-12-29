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
import tarzan.actual.domain.entity.MtWorkOrderActualHis;
import tarzan.actual.domain.repository.MtWorkOrderActualHisRepository;
import tarzan.actual.domain.vo.MtWorkOrderActualHisVO;
import tarzan.actual.domain.vo.MtWorkOrderActualHisVO2;

/**
 * 生产指令实绩历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtWorkOrderActualHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-work-order-actual-his")
@Api(tags = "MtWorkOrderActualHis")
public class MtWorkOrderActualHisController extends BaseController {

    @Autowired
    private MtWorkOrderActualHisRepository repository;

    @ApiOperation(value = "woActualHisPropertyQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderActualHis>> woActualHisPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWorkOrderActualHisVO vo) {
        ResponseData<List<MtWorkOrderActualHis>> responseData = new ResponseData<List<MtWorkOrderActualHis>>();
        try {
            responseData.setRows(this.repository.woActualHisPropertyQuery(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitWoActualHisQuery")
    @PostMapping(value = {"/limit-event/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderActualHis>> eventLimitWoActualHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eventId) {
        ResponseData<List<MtWorkOrderActualHis>> responseData = new ResponseData<List<MtWorkOrderActualHis>>();
        try {
            responseData.setRows(this.repository.eventLimitWoActualHisQuery(tenantId, eventId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }


    @ApiOperation(value = "eventLimitWoActualHisBatchQuery")
    @PostMapping(value = {"/limit-event-list/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderActualHis>> eventLimitWoActualHisBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtWorkOrderActualHis>> responseData = new ResponseData<List<MtWorkOrderActualHis>>();
        try {
            responseData.setRows(this.repository.eventLimitWoActualHisBatchQuery(tenantId, eventIds));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "woLimitWoActualHisQuery")
    @PostMapping(value = {"/limit-wo/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderActualHisVO2>> woLimitWoActualHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWorkOrderActualHisVO v) {
        ResponseData<List<MtWorkOrderActualHisVO2>> responseData = new ResponseData<List<MtWorkOrderActualHisVO2>>();
        try {
            responseData.setRows(this.repository.woLimitWoActualHisQuery(tenantId, v));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
