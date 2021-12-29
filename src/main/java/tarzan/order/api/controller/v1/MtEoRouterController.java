package tarzan.order.api.controller.v1;

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
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.vo.*;

/**
 * EO工艺路线 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@RestController("mtEoRouterController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-router")
@Api(tags = "MtEoRouter")
public class MtEoRouterController extends BaseController {
    @Autowired
    private MtEoRouterRepository repository;

    @ApiOperation(value = "eoRouterGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoRouterGet(@PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoRouterGet(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterVerify")
    @PostMapping(value = {"/enable-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoRouterVerify(@PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoRouterVerify(tenantId, eoId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterBomMatchValidate")
    @PostMapping(value = {"/bom-match-validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoRouterBomMatchValidate(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody String eoId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoRouterBomMatchValidate(tenantId, eoId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterUpdate")
    @PostMapping(value = {"/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterVO2> eoRouterUpdate(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody MtEoRouterVO vo) {
        ResponseData<MtEoRouterVO2> responseData = new ResponseData<MtEoRouterVO2>();
        try {
            responseData.setRows(this.repository.eoRouterUpdate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoEntryStepGet")
    @PostMapping(value = {"/entry/step"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> eoEntryStepGet(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody String eoId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.eoEntryStepGet(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoEntryStepBatchGet")
    @PostMapping(value = {"/entry/step/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouterVO4>> eoEntryStepBatchGet(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody List<String> eoIds) {
        ResponseData<List<MtEoRouterVO4>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoEntryStepBatchGet(tenantId, eoIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterUpdateVerify")
    @PostMapping(value = {"/update-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoRouterUpdateVerify(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody MtEoRouterVO1 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoRouterUpdateVerify(tenantId, vo);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterBatchUpdate")
    @PostMapping(value = {"/batch-update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouterVO2>> eoRouterBatchUpdate(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtEoRouterVO3 vo) {
        ResponseData<List<MtEoRouterVO2>> responseData = new ResponseData<List<MtEoRouterVO2>>();
        try {
            responseData.setRows(this.repository.eoRouterBatchUpdate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
    
    @ApiOperation(value = "eoRouterBatchGet")
    @PostMapping(value = {"/property/batch/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouter>> eoRouterBatchGet(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> eoIds) {
        ResponseData<List<MtEoRouter>> responseData = new ResponseData<List<MtEoRouter>>();
        try {
            responseData.setRows(this.repository.eoRouterBatchGet(tenantId, eoIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
