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
import tarzan.inventory.domain.entity.MtContainerHis;
import tarzan.inventory.domain.repository.MtContainerHisRepository;
import tarzan.inventory.domain.vo.MtContainerHisVO;
import tarzan.inventory.domain.vo.MtContainerHisVO1;
import tarzan.inventory.domain.vo.MtContainerHisVO2;
import tarzan.inventory.domain.vo.MtContainerHisVO3;

/**
 * 容器历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@RestController("mtContainerHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-container-his")
@Api(tags = "MtContainerHis")
public class MtContainerHisController extends BaseController {

    @Autowired
    private MtContainerHisRepository repository;

    @ApiOperation("eventLimitContainerHisBatchQuery")
    @PostMapping("/limit-event-list/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerHis>> eventLimitContainerHisBatchQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> eventIds) {

        ResponseData<List<MtContainerHis>> responseData = new ResponseData<List<MtContainerHis>>();
        try {
            responseData.setRows(repository.eventLimitContainerHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("eventLimitContainerHisQuery")
    @PostMapping("/limit-event/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerHis>> eventLimitContainerHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String eventId) {

        ResponseData<List<MtContainerHis>> responseData = new ResponseData<List<MtContainerHis>>();
        try {
            responseData.setRows(repository.eventLimitContainerHisQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("requestLimitContainerHisQuery")
    @PostMapping("/limit-request/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerHis>> requestLimitContainerHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContainerHisVO dto) {

        ResponseData<List<MtContainerHis>> responseData = new ResponseData<List<MtContainerHis>>();
        try {
            responseData.setRows(repository.requestLimitContainerHisQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerHisQuery")
    @PostMapping("/property/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerHisVO2>> containerHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContainerHisVO1 dto) {

        ResponseData<List<MtContainerHisVO2>> responseData = new ResponseData<List<MtContainerHisVO2>>();
        try {
            responseData.setRows(repository.containerHisQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLatestHisGet")
    @PostMapping("/latest/his")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtContainerHisVO3> containerLatestHisGet(@PathVariable(value = "organizationId") Long tenantId,
                                                                 @RequestBody String containerId) {
        ResponseData<MtContainerHisVO3> responseData = new ResponseData<MtContainerHisVO3>();
        try {
            responseData.setRows(repository.containerLatestHisGet(tenantId, containerId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
