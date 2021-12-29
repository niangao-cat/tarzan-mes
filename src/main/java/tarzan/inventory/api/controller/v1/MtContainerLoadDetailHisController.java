package tarzan.inventory.api.controller.v1;

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
import tarzan.inventory.domain.entity.MtContainerLoadDetailHis;
import tarzan.inventory.domain.repository.MtContainerLoadDetailHisRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlHisVO;
import tarzan.inventory.domain.vo.MtContLoadDtlHisVO2;
import tarzan.inventory.domain.vo.MtContLoadDtlHisVO3;

/**
 * 容器装载明细历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@RestController("mtContainerLoadDetailHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-container-load-detail-his")
@Api(tags = "MtContainerLoadDetailHis")
public class MtContainerLoadDetailHisController extends BaseController {

    @Autowired
    private MtContainerLoadDetailHisRepository repository;

    @ApiOperation("eventLimitContainerLoadDetailBatchQuery")
    @PostMapping("/limit-event-list/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerLoadDetailHis>> eventLimitContainerLoadDetailBatchQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> eventIds) {

        ResponseData<List<MtContainerLoadDetailHis>> responseData = new ResponseData<List<MtContainerLoadDetailHis>>();
        try {
            responseData.setRows(this.repository.eventLimitContainerLoadDetailBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("requestLimitContainerLoadDetailHisQuery")
    @PostMapping("/limit-request/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerLoadDetailHis>> requestLimitContainerLoadDetailHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContLoadDtlHisVO dto) {

        ResponseData<List<MtContainerLoadDetailHis>> responseData = new ResponseData<List<MtContainerLoadDetailHis>>();
        try {
            responseData.setRows(this.repository.requestLimitContainerLoadDetailHisQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("eventLimitContainerLoadDetailQuery")
    @PostMapping("/limit-event/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerLoadDetailHis>> eventLimitContainerLoadDetailQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String eventId) {

        ResponseData<List<MtContainerLoadDetailHis>> responseData = new ResponseData<List<MtContainerLoadDetailHis>>();
        try {
            responseData.setRows(this.repository.eventLimitContainerLoadDetailQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLoadDetailHisQuery")
    @PostMapping("/property")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContLoadDtlHisVO3>> containerLoadDetailHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContLoadDtlHisVO2 dto) {

        ResponseData<List<MtContLoadDtlHisVO3>> responseData = new ResponseData<List<MtContLoadDtlHisVO3>>();
        try {
            responseData.setRows(this.repository.containerLoadDetailHisQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
