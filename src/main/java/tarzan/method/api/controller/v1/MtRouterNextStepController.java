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
import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.repository.MtRouterNextStepRepository;
import tarzan.method.domain.vo.MtRouterNextStepVO3;

/**
 * 下一步骤 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@RestController("mtRouterNextStepController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-next-step")
@Api(tags = "MtRouterNextStep")
public class MtRouterNextStepController extends BaseController {

    @Autowired
    private MtRouterNextStepRepository repository;

    @ApiOperation(value = "routerNextStepQuery")
    @PostMapping(value = {"/next"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterNextStep>> routerNextStepQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<List<MtRouterNextStep>> responseData = new ResponseData<List<MtRouterNextStep>>();
        try {
            responseData.setRows(this.repository.routerNextStepQuery(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerPreviousStepQuery")
    @PostMapping(value = {"/previous"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterNextStep>> routerPreviousStepQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<List<MtRouterNextStep>> responseData = new ResponseData<List<MtRouterNextStep>>();
        try {
            responseData.setRows(this.repository.routerPreviousStepQuery(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "decisionTypeLimitRouterNextStepQuery")
    @PostMapping(value = {"/limit-decision/next"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterNextStep>> decisionTypeLimitRouterNextStepQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtRouterNextStepVO3 condition) {
        ResponseData<List<MtRouterNextStep>> responseData = new ResponseData<List<MtRouterNextStep>>();
        try {
            responseData.setRows(
                            this.repository.decisionTypeLimitRouterNextStepQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
