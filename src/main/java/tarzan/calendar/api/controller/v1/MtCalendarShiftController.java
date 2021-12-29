package tarzan.calendar.api.controller.v1;

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
import tarzan.calendar.api.dto.*;
import tarzan.calendar.app.service.MtCalendarShiftService;
import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.*;

/**
 * 工作日历班次 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
@RestController("mtCalendarShiftController.v1")
@RequestMapping("/v1/{organizationId}/mt-calendar-shift")
@Api(tags = "MtCalendarShift")
public class MtCalendarShiftController extends BaseController {

    @Autowired
    private MtCalendarShiftService service;

    @ApiOperation(value = "UI查询日期班次列表")
    @GetMapping(value = {"/grid/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarShiftDTO2>> queryCalendarShiftGridForUi(
                    @PathVariable("organizationId") Long tenantId, MtCalendarShiftDTO3 dto) {
        ResponseData<List<MtCalendarShiftDTO2>> responseData = new ResponseData<List<MtCalendarShiftDTO2>>();
        try {
            responseData.setRows(service.queryCalendarShiftGridForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI查询日历具体日期班次列表")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtCalendarShiftDTO4>> queryCalendarShiftListForUi(
                    @PathVariable("organizationId") Long tenantId, MtCalendarShiftDTO4 dto,
                    @ApiIgnore @SortDefault(value = MtCalendarShift.FIELD_SEQUENCE,
                                    direction = Sort.Direction.ASC) PageRequest pageRequest) {
        ResponseData<Page<MtCalendarShiftDTO4>> responseData = new ResponseData<Page<MtCalendarShiftDTO4>>();
        try {
            responseData.setRows(service.queryCalendarShiftListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI查询日历具体日期班次列表(不加分页)")
    @GetMapping(value = {"/no-page/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarShiftDTO4>> queryCalendarShiftListNoPageForUi(
                    @PathVariable("organizationId") Long tenantId, MtCalendarShiftDTO4 dto,
                    @ApiIgnore @SortDefault(value = MtCalendarShift.FIELD_SEQUENCE,
                                    direction = Sort.Direction.ASC) PageRequest pageRequest) {
        ResponseData<List<MtCalendarShiftDTO4>> responseData = new ResponseData<List<MtCalendarShiftDTO4>>();
        try {
            responseData.setRows(service.queryCalendarShiftListNoPageForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存日历班次信息")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveCalendarShiftForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarShiftDTO4 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(service.saveCalendarShiftForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI删除日历班次信息")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeCalendarShiftForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> calendarIdList) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.newRemoveCalendarShiftForUi(tenantId, calendarIdList);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI初始化日历班次信息")
    @PostMapping(value = {"/init/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> initCalendarShiftForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarShiftDTO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        validObject(dto);
        try {
            service.newInitCalendarShiftForUi(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "UI复制日历班次信息-前校验")
    @PostMapping(value = {"/check/copy/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> copyCalendarShiftCheckForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarShiftDTO6 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(service.copyCalendarShiftCheckForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI复制日历班次信息")
    @PostMapping(value = {"/copy/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> copyCalendarShiftForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarShiftDTO6 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        validObject(dto);
        try {
            service.copyCalendarShiftForUi(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @Autowired
    private MtCalendarShiftRepository repository;

    @ApiOperation(value = "calendarShiftGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtCalendarShift> calendarShiftGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String calendarShiftId) {
        ResponseData<MtCalendarShift> responseData = new ResponseData<MtCalendarShift>();
        try {
            responseData.setRows(repository.calendarShiftGet(tenantId, calendarShiftId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "calendarLimitShiftQuery")
    @PostMapping(value = {"/limit-calendar"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> calendarLimitShiftQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarShiftVO2 vo) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.calendarLimitShiftQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "calendarShiftAvailableValidate")
    @PostMapping(value = {"/enable-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> calendarShiftAvailableValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String calendarShiftId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.calendarShiftAvailableValidate(tenantId, calendarShiftId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "timeLimitCalendarShiftQuery")
    @PostMapping(value = {"/limit-time"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> timeLimitCalendarShiftQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarShiftVO7 vo) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.timeLimitCalendarShiftQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "calendarShiftCopy")
    @PostMapping(value = {"/copy"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> calendarShiftCopy(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarShiftVO1 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.calendarShiftCopy(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "calendarShiftBatchCopy")
    @PostMapping(value = {"/copy-list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> calendarShiftBatchCopy(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarShiftVO6 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.calendarShiftBatchCopy(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "calendarShiftBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarShift>> calendarShiftBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> calendarShiftIdList) {
        ResponseData<List<MtCalendarShift>> responseData = new ResponseData<List<MtCalendarShift>>();
        try {
            responseData.setRows(repository.calendarShiftBatchGet(tenantId, calendarShiftIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "timeLimitAvailableCalendarShiftQuery")
    @PostMapping(value = {"/enable/limit-time"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> timeLimitAvailableCalendarShiftQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtCalendarShiftVO7 vo) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.timeLimitAvailableCalendarShiftQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "calendarPreviousShiftGet")
    @PostMapping(value = {"/previous"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtCalendarShift> calendarPreviousShiftGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarShiftVO vo) {

        ResponseData<MtCalendarShift> responseData = new ResponseData<MtCalendarShift>();
        try {
            responseData.setRows(repository.calendarPreviousShiftGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "calendarNextShiftGet")
    @PostMapping(value = {"/next"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtCalendarShift> calendarNextShiftGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarShiftVO vo) {
        ResponseData<MtCalendarShift> responseData = new ResponseData<MtCalendarShift>();
        try {
            responseData.setRows(repository.calendarNextShiftGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "calendarShiftLimitNearShiftQuery")
    @PostMapping(value = {"/near"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarShiftVO4>> calendarShiftLimitNearShiftQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtCalendarShiftVO3 vo) {
        ResponseData<List<MtCalendarShiftVO4>> responseData = new ResponseData<List<MtCalendarShiftVO4>>();
        try {
            responseData.setRows(repository.calendarShiftLimitNearShiftQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "organizationAndShiftLimitCalendarShiftGet")
    @PostMapping(value = {"/limit-organization-shift"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> organizationAndShiftLimitCalendarShiftGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarShiftVO5 vo) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.organizationAndShiftLimitCalendarShiftGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "currentShiftLimitNextCalendarShiftGet")
    @PostMapping(value = {"/current-shift/limit-next-calendar-shift"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtCalendarShiftVO8> currentShiftLimitNextCalendarShiftGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String workcellId) {

        ResponseData<MtCalendarShiftVO8> responseData = new ResponseData<MtCalendarShiftVO8>();
        try {
            responseData.setRows(repository.currentShiftLimitNextCalendarShiftGet(tenantId, workcellId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitCalendarShiftPropertyQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarShiftVO10>> propertyLimitCalendarShiftPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtCalendarShiftVO9 vo) {
        ResponseData<List<MtCalendarShiftVO10>> responseData = new ResponseData<List<MtCalendarShiftVO10>>();
        try {
            responseData.setRows(repository.propertyLimitCalendarShiftPropertyQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "calendarShiftLimitTargetShiftQuery")
    @PostMapping(value = {"/limit-shift/target"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarShiftVO12>> calendarShiftLimitTargetShiftQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtCalendarShiftVO11 vo) {
        ResponseData<List<MtCalendarShiftVO12>> responseData = new ResponseData<List<MtCalendarShiftVO12>>();
        try {
            responseData.setRows(repository.calendarShiftLimitTargetShiftQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "calendarLimitShiftBatchQuery")
    @PostMapping(value = {"/calendar/limit/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> calendarLimitShiftBatchQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtCalendarShiftVO2> dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.calendarLimitShiftBatchQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "dateLimitCalendarShiftGet")
    @PostMapping(value = {"/limit/date/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarShiftVO14>> dateLimitCalendarShiftGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtCalendarShiftVO13 dto) {
        ResponseData<List<MtCalendarShiftVO14>> responseData = new ResponseData<List<MtCalendarShiftVO14>>();
        try {
            responseData.setRows(repository.dateLimitCalendarShiftGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
