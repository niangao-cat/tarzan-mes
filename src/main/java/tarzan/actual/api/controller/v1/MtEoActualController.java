package tarzan.actual.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.actual.domain.entity.MtEoActual;
import tarzan.actual.domain.repository.MtEoActualRepository;
import tarzan.actual.domain.vo.*;

/**
 * 执行作业【执行作业需求和实绩拆分开】 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtEoActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-actual")
@Api(tags = "MtEoActual")
public class MtEoActualController extends BaseController {
    @Autowired
    private MtEoActualRepository repository;

    @ApiOperation(value = "eoActualGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoActual> eoActualGet(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody MtEoActualVO vo) {
        ResponseData<MtEoActual> responseData = new ResponseData<MtEoActual>();
        try {
            responseData.setRows(this.repository.eoActualGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoProductionPeriodGet")
    @PostMapping(value = {"/eo-production-period"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoActualVO3> eoProductionPeriodGet(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtEoActualVO2 vo) {
        ResponseData<MtEoActualVO3> responseData = new ResponseData<MtEoActualVO3>();
        try {
            responseData.setRows(this.repository.eoProductionPeriodGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoActualUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoActualVO7> eoActualUpdate(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody MtEoActualVO4 vo,
                                                      @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<MtEoActualVO7> responseData = new ResponseData<MtEoActualVO7>();
        try {
            responseData.setRows(this.repository.eoActualUpdate(tenantId, vo, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWorking")
    @PostMapping(value = {"/eo-working"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWorking(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoActualVO5 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoWorking(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWorkingCancel")
    @PostMapping(value = {"/eo-working-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWorkingCancel(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody MtEoActualVO5 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoWorkingCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoScrap")
    @PostMapping(value = {"/scrap"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoScrap(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoActualVO6 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoScrap(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoScrapCancel")
    @PostMapping(value = {"/scrap-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoScrapCancel(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody MtEoActualVO6 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoScrapCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitEoActualPropertyQuery")
    @PostMapping(value = {"/limit-property/actual"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoActualVO9>> propertyLimitEoActualPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoActualVO8 vo) {
        ResponseData<List<MtEoActualVO9>> responseData = new ResponseData<List<MtEoActualVO9>>();
        try {
            responseData.setRows(this.repository.propertyLimitEoActualPropertyQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoActualBatchUpdate")
    @PostMapping(value = {"/eo-actual/batch/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoActualVO12>> eoActualBatchUpdate(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody MtEoActualVO11 vo,
                                                                  @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<List<MtEoActualVO12>> responseData = new ResponseData<List<MtEoActualVO12>>();
        try {
            responseData.setRows(this.repository.eoActualBatchUpdate(tenantId, vo, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoBatchWorking")
    @PostMapping(value = {"/eo-batch-working"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoBatchWorking(@PathVariable("organizationId") Long tenantId,
                                             @RequestBody MtEoActualVO13 vo) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.eoBatchWorking(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
