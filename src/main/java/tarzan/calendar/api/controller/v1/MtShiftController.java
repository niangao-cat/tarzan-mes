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
import tarzan.calendar.api.dto.MtShiftDTO;
import tarzan.calendar.api.dto.MtShiftDTO1;
import tarzan.calendar.app.service.MtShiftService;
import tarzan.calendar.domain.entity.MtShift;
import tarzan.calendar.domain.repository.MtShiftRepository;
import tarzan.calendar.domain.vo.MtShiftVO;
import tarzan.calendar.domain.vo.MtShiftVO1;

/**
 * 班次信息 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:39
 */
@RestController("mtShiftController.v1")
@RequestMapping("/v1/{organizationId}/mt-shift")
@Api(tags = "MtShift")
public class MtShiftController extends BaseController {

    @Autowired
    private MtShiftService service;

    @ApiOperation(value = "UI查询班次信息")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtShiftDTO>> queryShiftForUi(@PathVariable("organizationId") Long tenantId, MtShiftDTO dto,
                    @ApiIgnore @SortDefault(value = {MtShift.FIELD_SHIFT_TYPE, MtShift.FIELD_SEQUENCE},
                                    direction = Sort.Direction.ASC) PageRequest pageRequest) {
        ResponseData<Page<MtShiftDTO>> responseData = new ResponseData<Page<MtShiftDTO>>();
        try {
            responseData.setRows(service.queryShiftForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存班次信息")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveShiftForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtShiftDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(service.saveShiftForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI删除班次信息")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeShiftForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String shiftId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.removeShiftForUi(tenantId, shiftId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI查询班次策略集合")
    @GetMapping(value = {"/types/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtShiftDTO1>> queryShiftTypesForUi(@PathVariable("organizationId") Long tenantId,
                    MtShiftDTO1 dto) {
        ResponseData<List<MtShiftDTO1>> responseData = new ResponseData<List<MtShiftDTO1>>();
        try {
            responseData.setRows(service.queryShiftTypesForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @Autowired
    private MtShiftRepository repository;

    @ApiOperation(value = "shiftTempletGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtShift> shiftTempletGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String shiftId) {
        ResponseData<MtShift> responseData = new ResponseData<MtShift>();
        try {
            responseData.setRows(repository.shiftTempletGet(tenantId, shiftId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "typeLimitShiftTempletQuery")
    @PostMapping(value = {"/limit-type"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> typeLimitShiftTempletQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String shiftType) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.typeLimitShiftTempletQuery(tenantId, shiftType));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "availableShiftTempletQuery")
    @PostMapping(value = {"/enable/limit-type"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> availableShiftTempletQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String shiftType) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.availableShiftTempletQuery(tenantId, shiftType));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "propertyLimitshiftTempletPropertyQuery")
    @PostMapping(value = {"/shift/property/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtShiftVO1>> propertyLimitshiftTempletPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtShiftVO dto) {
        ResponseData<List<MtShiftVO1>> responseData = new ResponseData<List<MtShiftVO1>>();
        try {
            responseData.setRows(repository.propertyLimitshiftTempletPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
