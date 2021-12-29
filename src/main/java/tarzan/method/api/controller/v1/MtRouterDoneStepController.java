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
import tarzan.method.domain.entity.MtRouterDoneStep;
import tarzan.method.domain.repository.MtRouterDoneStepRepository;

/**
 * 完成步骤 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@RestController("mtRouterDoneStepController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-done-step")
@Api(tags = "MtRouterDoneStep")
public class MtRouterDoneStepController extends BaseController {

    @Autowired
    private MtRouterDoneStepRepository repository;

    @ApiOperation(value = "routerDoneStepGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtRouterDoneStep> routerDoneStepGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<MtRouterDoneStep> responseData = new ResponseData<MtRouterDoneStep>();
        try {
            responseData.setRows(this.repository.routerDoneStepGet(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "doneStepVerify")
    @PostMapping(value = {"/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> doneStepVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.doneStepValidate(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerDoneStepBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterDoneStep>> routerDoneStepBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> routerStepIds) {
        ResponseData<List<MtRouterDoneStep>> responseData = new ResponseData<List<MtRouterDoneStep>>();
        try {
            responseData.setRows(this.repository.routerDoneStepBatchGet(tenantId, routerStepIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
