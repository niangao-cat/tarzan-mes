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
import tarzan.method.domain.entity.MtRouterStepGroup;
import tarzan.method.domain.repository.MtRouterStepGroupRepository;

/**
 * 工艺路线步骤组 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@RestController("mtRouterStepGroupController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-step-group")
@Api(tags = "MtRouterStepGroup")
public class MtRouterStepGroupController extends BaseController {

    @Autowired
    private MtRouterStepGroupRepository repository;

    @ApiOperation(value = "routerStepGroupGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtRouterStepGroup> routerStepGroupGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<MtRouterStepGroup> responseData = new ResponseData<MtRouterStepGroup>();
        try {
            responseData.setRows(this.repository.routerStepGroupGet(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerStepGroupBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStepGroup>> routerStepGroupBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> routerStepIds) {
        ResponseData<List<MtRouterStepGroup>> responseData = new ResponseData<List<MtRouterStepGroup>>();
        try {
            responseData.setRows(this.repository.routerStepGroupBatchGet(tenantId, routerStepIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
