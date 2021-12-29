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
import tarzan.actual.domain.entity.MtWorkOrderActual;
import tarzan.actual.domain.repository.MtWorkOrderActualRepository;
import tarzan.actual.domain.vo.*;

/**
 * 生产指令实绩 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtWorkOrderActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-work-order-actual")
@Api(tags = "MtWorkOrderActual")
public class MtWorkOrderActualController extends BaseController {

    @Autowired
    private MtWorkOrderActualRepository repository;

    @ApiOperation(value = "woActualGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrderActual> woActualGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWorkOrderActualVO vo) {

        ResponseData<MtWorkOrderActual> responseData = new ResponseData<MtWorkOrderActual>();
        try {
            responseData.setRows(this.repository.woActualGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woActualUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrderActualVO5> woActualUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWorkOrderActualVO4 vo,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {

        ResponseData<MtWorkOrderActualVO5> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.woActualUpdate(tenantId, vo, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woProductionPeriodGet")
    @PostMapping(value = {"/production/period"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrderActualVO2> woProductionPeriodGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWorkOrderActualVO2 vo) {

        ResponseData<MtWorkOrderActualVO2> responseData = new ResponseData<MtWorkOrderActualVO2>();
        try {
            responseData.setRows(this.repository.woProductionPeriodGet(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woScrap")
    @PostMapping(value = {"/scrap"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woScrap(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWorkOrderActualVO1 vo) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woScrap(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woScrapCancel")
    @PostMapping(value = {"/scrap-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woScrapCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWorkOrderActualVO1 vo) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woScrapCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woActualBatchUpdate")
    @PostMapping(value = {"/batch-update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderActualVO5>> woActualBatchUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWorkOrderActualVO8 dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {

        ResponseData<List<MtWorkOrderActualVO5>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.woActualBatchUpdate(tenantId, dto, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
