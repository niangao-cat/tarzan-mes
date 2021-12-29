package tarzan.general.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.general.api.dto.MtEventDTO;
import tarzan.general.api.dto.MtEventDTO2;
import tarzan.general.app.service.MtEventService;
import tarzan.general.domain.entity.MtEvent;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.*;

/**
 * 事件记录 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@RestController("mtEventController.v1")
@RequestMapping("/v1/{organizationId}/mt-event")
@Api(tags = "MtEvent")
public class MtEventController extends BaseController {

    @Autowired
    private MtEventService service;

    @ApiOperation(value = "UI获取事件记录")
    @GetMapping(value = {"/property/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEventDTO2>> eventUnionRequestGroupQueryForUi(
                    @PathVariable("organizationId") Long tenantId, MtEventDTO dto,
                    @ApiIgnore @SortDefault(value = MtEvent.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtEventDTO2>> responseData = new ResponseData<Page<MtEventDTO2>>();
        try {
            responseData.setRows(service.eventUnionRequestGroupQueryForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI根据父事件获取事件记录")
    @GetMapping(value = {"/limit-parent/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEventVO5>> parentEventQueryForUi(@PathVariable("organizationId") Long tenantId,
                    String parentEventId) {
        ResponseData<List<MtEventVO5>> responseData = new ResponseData<List<MtEventVO5>>();
        try {
            responseData.setRows(service.parentEventQueryForUi(tenantId, parentEventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @Autowired
    private MtEventRepository repository;

    @ApiOperation(value = "eventGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEventVO1> eventGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eventId) {
        ResponseData<MtEventVO1> responseData = new ResponseData<MtEventVO1>();
        try {
            responseData.setRows(repository.eventGet(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventBatchGet")
    @PostMapping(value = {"/batch/info"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEventVO1>> eventBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> eventIds) {
        ResponseData<List<MtEventVO1>> responseData = new ResponseData<List<MtEventVO1>>();
        try {
            responseData.setRows(repository.eventBatchGet(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitEventQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitEventQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEventVO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.propertyLimitEventQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "requestLimitEventQuery")
    @PostMapping(value = {"/limit-request"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> requestLimitEventQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEventVO2 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.requestLimitEventQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eventCreate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEventCreateVO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.eventCreate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "parentEventQuery")
    @PostMapping(value = {"/parent/event"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEventVO3>> parentEventQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> eventIds) {
        ResponseData<List<MtEventVO3>> responseData = new ResponseData<List<MtEventVO3>>();
        try {
            responseData.setRows(repository.parentEventQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitRequestAndEventQuery")
    @PostMapping(value = {"/limit-property/request/event"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEventVO6>> propertyLimitRequestAndEventQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEventVO4 dto) {
        ResponseData<List<MtEventVO6>> responseData = new ResponseData<List<MtEventVO6>>();
        try {
            responseData.setRows(repository.propertyLimitRequestAndEventQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
