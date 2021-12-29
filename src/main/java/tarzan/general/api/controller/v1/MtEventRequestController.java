package tarzan.general.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventRequestVO1;
import tarzan.general.domain.vo.MtEventRequestVO2;

/**
 * 事件请求记录 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@RestController("mtEventRequestController.v1")
@RequestMapping("/v1/{organizationId}/mt-event-request")
@Api(tags = "MtEventRequest")
public class MtEventRequestController extends BaseController {

    @Autowired
    private MtEventRequestRepository repository;

    @ApiOperation(value = "eventRequestGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEventRequestVO1> eventRequestGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eventRequestId) {
        ResponseData<MtEventRequestVO1> result = new ResponseData<MtEventRequestVO1>();
        try {
            result.setRows(repository.eventRequestGet(tenantId, eventRequestId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "propertyLimitEventRequestQuery")
    @PostMapping(value = {"/limit-property/request"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitEventRequestQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEventRequestVO2 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.propertyLimitEventRequestQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventRequestCreate")
    @PostMapping(value = {"/request/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eventRequestCreate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String requestTypeCode) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.eventRequestCreate(tenantId, requestTypeCode));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
