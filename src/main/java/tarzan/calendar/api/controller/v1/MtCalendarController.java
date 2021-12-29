package tarzan.calendar.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import tarzan.calendar.api.dto.MtCalendarDTO;
import tarzan.calendar.api.dto.MtCalendarLovDTO;
import tarzan.calendar.app.service.MtCalendarService;
import tarzan.calendar.domain.entity.MtCalendar;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.vo.*;

/**
 * 工作日历 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
@RestController("mtCalendarController.v1")
@RequestMapping("/v1/{organizationId}/mt-calendar")
@Api(tags = "MtCalendar")
public class MtCalendarController extends BaseController {

    @Autowired
    private MtCalendarService service;

    @ApiOperation(value = "UI查询工作日历列表")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtCalendarDTO>> queryCalendarListForUi(@PathVariable("organizationId") Long tenantId,
                    MtCalendarDTO dto, @ApiIgnore @SortDefault(value = MtCalendar.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtCalendarDTO>> responseData = new ResponseData<Page<MtCalendarDTO>>();
        try {
            responseData.setRows(service.queryCalendarListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI查询工作日历LOV")
    @GetMapping(value = {"/lov/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtCalendarLovDTO>> queryCalendarLovForUi(@PathVariable("organizationId") Long tenantId,
                    MtCalendarDTO dto, @ApiIgnore @SortDefault(value = MtCalendar.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(service.queryCalendarLovForUi(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "UI保存工作日历")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtCalendarDTO> saveCalendarForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarDTO dto) {
        ResponseData<MtCalendarDTO> responseData = new ResponseData<MtCalendarDTO>();
        try {
            responseData.setRows(service.saveCalendarForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "UI根据calendarId查询日历信息")
    @GetMapping(value = {"/id/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtCalendarDTO> getCalendarForUi(@PathVariable("organizationId") Long tenantId,
                    String calendarId) {
        ResponseData<MtCalendarDTO> responseData = new ResponseData<MtCalendarDTO>();
        try {
            responseData.setRows(service.getCalendarForUi(tenantId, calendarId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @Autowired
    private MtCalendarRepository repository;

    @ApiOperation(value = "calendarGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtCalendarVO3> calendarGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String calendarId) {
        ResponseData<MtCalendarVO3> responseData = new ResponseData<MtCalendarVO3>();
        try {
            responseData.setRows(repository.calendarGet(tenantId, calendarId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "calendarBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarVO3>> calendarBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> calendarIds) {
        ResponseData<List<MtCalendarVO3>> responseData = new ResponseData<List<MtCalendarVO3>>();
        try {
            responseData.setRows(this.repository.calendarBatchGet(tenantId, calendarIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "typeLimitCalendarQuery")
    @PostMapping(value = {"/limit-type"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> typeLimitCalendarQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String calendarType) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.typeLimitCalendarQuery(tenantId, calendarType));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "organizationLimitCalendarQuery")
    @PostMapping(value = {"/limit-organization"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> organizationLimitCalendarQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarVO vo) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.organizationLimitCalendarQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "calendarAvailableValidate")
    @PostMapping(value = {"/enable-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> calendarAvailableValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String calendarId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.calendarAvailableValidate(tenantId, calendarId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "organizationLimitOnlyCalendarGet")
    @PostMapping(value = {"/limit-organization-calander"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> organizationLimitOnlyCalendarGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarVO2 vo) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.organizationLimitOnlyCalendarGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitCalendarPropertyQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarVO5>> propertyLimitCalendarPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtCalendarVO4 vo) {
        ResponseData<List<MtCalendarVO5>> responseData = new ResponseData<List<MtCalendarVO5>>();
        try {
            responseData.setRows(repository.propertyLimitCalendarPropertyQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "organizationLimitOnlyCalendarBatchGet")
    @PostMapping(value = {"/limit-organization-calander-batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarVO7>> organizationLimitOnlyCalendarBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtCalendarVO6 vo) {
        ResponseData<List<MtCalendarVO7>> responseData = new ResponseData<List<MtCalendarVO7>>();
        try {
            responseData.setRows(repository.organizationLimitOnlyCalendarBatchGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
