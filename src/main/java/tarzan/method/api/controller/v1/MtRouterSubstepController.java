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
import tarzan.method.domain.entity.MtRouterSubstep;
import tarzan.method.domain.repository.MtRouterSubstepRepository;

/**
 * 工艺路线子步骤 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@RestController("mtRouterSubstepController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-substep")
@Api(tags = "MtRouterSubstep")
public class MtRouterSubstepController extends BaseController {

    @Autowired
    private MtRouterSubstepRepository repository;

    @ApiOperation(value = "routerSubstepQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterSubstep>> routerSubstepQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<List<MtRouterSubstep>> responseData = new ResponseData<List<MtRouterSubstep>>();
        try {
            responseData.setRows(this.repository.routerSubstepQuery(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
