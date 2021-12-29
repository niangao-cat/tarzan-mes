package tarzan.method.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.method.domain.entity.MtRouterReturnStep;
import tarzan.method.domain.repository.MtRouterReturnStepRepository;

/**
 * 返回步骤 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@RestController("mtRouterReturnStepController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-return-step")
@Api(tags = "MtRouterReturnStep")
public class MtRouterReturnStepController extends BaseController {

    @Autowired
    private MtRouterReturnStepRepository repository;

    @ApiOperation(value = "routerReturnStepGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtRouterReturnStep> routerReturnStepGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<MtRouterReturnStep> responseData = new ResponseData<MtRouterReturnStep>();
        try {
            responseData.setRows(this.repository.routerReturnStepGet(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "returnStepVerify")
    @PostMapping(value = {"/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> returnStepVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.returnStepValidate(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerReturnStepBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterReturnStep>> routerReturnStepBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> routerStepIds) {
        ResponseData<List<MtRouterReturnStep>> responseData = new ResponseData<List<MtRouterReturnStep>>();
        try {
            responseData.setRows(this.repository.routerReturnStepBatchGet(tenantId, routerStepIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
