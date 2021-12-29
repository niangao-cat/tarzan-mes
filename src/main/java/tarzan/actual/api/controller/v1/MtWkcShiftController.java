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
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO;
import tarzan.actual.domain.vo.MtWkcShiftVO2;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.actual.domain.vo.MtWkcShiftVO4;
import tarzan.actual.domain.vo.MtWkcShiftVO5;
import tarzan.actual.domain.vo.MtWkcShiftVO6;
import tarzan.actual.domain.vo.MtWkcShiftVO7;
import tarzan.actual.domain.vo.MtWkcShiftVO8;
import tarzan.actual.domain.vo.MtWkcShiftVO9;

/**
 * 开班实绩数据表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:00:23
 */
@RestController("mtWkcShiftController.v1")
@RequestMapping("/v1/{organizationId}/mt-wkc-shift")
@Api(tags = "MtWkcShift")
public class MtWkcShiftController extends BaseController {

    @Autowired
    private MtWkcShiftRepository repository;

    @ApiOperation(value = "nextShiftGet")
    @PostMapping(value = {"/next-shift"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> nextShiftGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String wkcShiftId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.nextShiftGet(tenantId, wkcShiftId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "timePeriodLimitShiftQuery")
    @PostMapping(value = {"/limit-time/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> timePeriodLimitShiftQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWkcShiftVO5 dto) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.timePeriodLimitShiftQuery(tenantId, dto));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "timePeriodShiftCodeLimitShiftQuery")
    @PostMapping(value = {"/limit-time-shift/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> timePeriodShiftCodeLimitShiftQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWkcShiftVO dto) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.timePeriodShiftCodeLimitShiftQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "previousShiftGet")
    @PostMapping(value = {"/previous"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> previousShiftGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String wkcShiftId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.previousShiftGet(tenantId, wkcShiftId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcCurrentShiftQuery")
    @PostMapping(value = {"/current-wkc/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWkcShiftVO3> wkcCurrentShiftQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String workcellId) {
        ResponseData<MtWkcShiftVO3> responseData = new ResponseData<MtWkcShiftVO3>();
        try {
            responseData.setRows(this.repository.wkcCurrentShiftQuery(tenantId, workcellId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcLimitLastShiftGet")
    @PostMapping(value = {"/last"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> wkcLimitLastShiftGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String workcellId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.wkcLimitLastShiftGet(tenantId, workcellId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcLimitShiftQuery")
    @PostMapping(value = {"/limit-wkc/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> wkcLimitShiftQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String workcellId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.wkcLimitShiftQuery(tenantId, workcellId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "timePeriodShiftTimeSum")
    @PostMapping(value = {"/limit-period/time/sum"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Long> timePeriodShiftTimeSum(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWkcShiftVO2 dto) {
        ResponseData<Long> result = new ResponseData<Long>();
        try {
            result.setRows(repository.timePeriodShiftTimeSum(tenantId, dto));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "wkcAndDateLimitShiftQuery")
    @PostMapping(value = {"/limit-wkc-date/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> wkcAndDateLimitShiftQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWkcShiftVO6 dto) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.wkcAndDateLimitShiftQuery(tenantId, dto));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "wkcShiftBack")
    @PostMapping(value = {"/remove"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> wkcShiftBack(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWkcShiftVO7 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.wkcShiftBack(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "wkcShiftEnd")
    @PostMapping(value = {"/end"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> wkcShiftEnd(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String workcellId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.wkcShiftEnd(tenantId, workcellId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcShiftGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWkcShift> wkcShiftGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String wkcShiftId) {
        ResponseData<MtWkcShift> responseData = new ResponseData<MtWkcShift>();
        try {
            responseData.setRows(this.repository.wkcShiftGet(tenantId, wkcShiftId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcShiftStart")
    @PostMapping(value = {"/start"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> wkcShiftStart(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWkcShiftVO7 dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.wkcShiftStart(tenantId, dto));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "wkcShiftHandover")
    @PostMapping(value = {"/handover"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> wkcShiftHandover(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String workcellId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.wkcShiftHandover(tenantId, workcellId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcShiftTimeCalculate")
    @PostMapping(value = {"/time/calculation"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Long> wkcShiftTimeCalculate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWkcShiftVO4 dto) {
        ResponseData<Long> result = new ResponseData<Long>();
        try {
            result.setRows(repository.wkcShiftTimeCalculate(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "dateAndShiftCodeLimitShiftQuery")
    @PostMapping(value = {"/limit-date-shift/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> dateAndShiftCodeLimitShiftQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWkcShiftVO7 dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.dateAndShiftCodeLimitShiftQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "dateLimitShiftQuery")
    @PostMapping(value = {"/limit-date/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> dateLimitShiftQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String shiftDate) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.dateLimitShiftQuery(tenantId, shiftDate));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "propertyLimitWkcShiftPropertyQuery")
    @PostMapping(value = {"/shift/property/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWkcShiftVO9>> propertyLimitWkcShiftPropertyQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtWkcShiftVO8 dto) {
        ResponseData<List<MtWkcShiftVO9>> result = new ResponseData<List<MtWkcShiftVO9>>();
        try {
            result.setRows(repository.propertyLimitWkcShiftPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
