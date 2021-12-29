package tarzan.order.api.controller.v1;

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
import tarzan.order.domain.entity.MtEoHis;
import tarzan.order.domain.repository.MtEoHisRepository;
import tarzan.order.domain.vo.MtEoHisVO;
import tarzan.order.domain.vo.MtEoHisVO1;
import tarzan.order.domain.vo.MtEoHisVO2;

/**
 * 执行作业历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@RestController("mtEoHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-his")
@Api(tags = "MtEoHis")
public class MtEoHisController extends BaseController {
    @Autowired
    private MtEoHisRepository repository;

    @ApiOperation(value = "eoHisPropertyQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoHis>> eoHisPropertyQuery(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody MtEoHisVO condition) {
        ResponseData<List<MtEoHis>> responseData = new ResponseData<List<MtEoHis>>();
        try {
            responseData.setRows(this.repository.eoHisPropertyQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitEoHisQuery")
    @PostMapping(value = {"/limit-eo/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoHisVO1>> eoLimitEoHisQuery(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody String eoId) {
        ResponseData<List<MtEoHisVO1>> responseData = new ResponseData<List<MtEoHisVO1>>();
        try {
            responseData.setRows(this.repository.eoLimitEoHisQuery(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitEoHisBatchQuery")
    @PostMapping(value = {"/property/list/limit-events"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoHis>> eventLimitEoHisBatchQuery(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody List<String> eventIds) {
        ResponseData<List<MtEoHis>> responseData = new ResponseData<List<MtEoHis>>();
        try {
            responseData.setRows(this.repository.eventLimitEoHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitEoHisQuery")
    @PostMapping(value = {"/property/limit-event"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoHis>> eventLimitEoHisQuery(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody String eventId) {
        ResponseData<List<MtEoHis>> responseData = new ResponseData<List<MtEoHis>>();
        try {
            responseData.setRows(this.repository.eventLimitEoHisQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLatestHisGet")
    @PostMapping(value = {"/eo/his/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoHisVO2> eoLatestHisGet(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody String eoId) {
        ResponseData<MtEoHisVO2> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoLatestHisGet(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
