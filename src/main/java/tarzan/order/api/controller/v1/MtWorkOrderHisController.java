package tarzan.order.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.order.domain.entity.MtWorkOrderHis;
import tarzan.order.domain.repository.MtWorkOrderHisRepository;
import tarzan.order.domain.vo.MtWorkOrderHisVO;
import tarzan.order.domain.vo.MtWorkOrderHisVO1;
import tarzan.order.domain.vo.MtWorkOrderHisVO2;

/**
 * 生产指令历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
@RestController("mtWorkOrderHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-work-order-his")
@Api(tags = "MtWorkOrderHis")
public class MtWorkOrderHisController extends BaseController {
    @Autowired
    private MtWorkOrderHisRepository repository;

    @ApiOperation("woHisPropertyQuery")
    @PostMapping("/property/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderHis>> woHisPropertyQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtWorkOrderHisVO condition) {
        ResponseData<List<MtWorkOrderHis>> responseData = new ResponseData<List<MtWorkOrderHis>>();
        try {
            responseData.setRows(this.repository.woHisPropertyQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woLimitWoHisQuery")
    @PostMapping("/limit-wo")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderHisVO1>> woLimitWoHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String workOrderId) {
        ResponseData<List<MtWorkOrderHisVO1>> responseData = new ResponseData<List<MtWorkOrderHisVO1>>();
        try {
            responseData.setRows(this.repository.woLimitWoHisQuery(tenantId, workOrderId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("eventLimitWoHisQuery")
    @PostMapping("/limit-event/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderHis>> eventLimitWoHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String eventId) {
        ResponseData<List<MtWorkOrderHis>> responseData = new ResponseData<List<MtWorkOrderHis>>();
        try {
            responseData.setRows(this.repository.eventLimitWoHisQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("eventLimitWoHisBatchQuery")
    @PostMapping("/limit-event-list/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderHis>> eventLimitWoHisBatchQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtWorkOrderHis>> responseData = new ResponseData<List<MtWorkOrderHis>>();
        try {
            responseData.setRows(this.repository.eventLimitWoHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "woLatestHisGet")
    @PostMapping(value = {"/latest/his"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrderHisVO2> woLatestHisGet(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody String workOrderId) {
        ResponseData<MtWorkOrderHisVO2> responseData = new ResponseData<MtWorkOrderHisVO2>();
        try {
            responseData.setRows(this.repository.woLatestHisGet(tenantId, workOrderId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
