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
import tarzan.method.domain.entity.MtRouterSubstepComponent;
import tarzan.method.domain.repository.MtRouterSubstepComponentRepository;

/**
 * 子步骤组件 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@RestController("mtRouterSubstepComponentController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-substep-component")
@Api(tags = "MtRouterSubstepComponent")
public class MtRouterSubstepComponentController extends BaseController {

    @Autowired
    private MtRouterSubstepComponentRepository repository;

    @ApiOperation(value = "routerSubstepComponentQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterSubstepComponent>> routerSubstepComponentQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String routerSubstepId) {
        ResponseData<List<MtRouterSubstepComponent>> responseData = new ResponseData<List<MtRouterSubstepComponent>>();
        try {
            responseData.setRows(this.repository.routerSubstepComponentQuery(tenantId,
                            routerSubstepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
