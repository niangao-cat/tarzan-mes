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
import tarzan.method.domain.entity.MtRouterLink;
import tarzan.method.domain.repository.MtRouterLinkRepository;

/**
 * 嵌套工艺路线步骤 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@RestController("mtRouterLinkController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-link")
@Api(tags = "MtRouterLink")
public class MtRouterLinkController extends BaseController {

    @Autowired
    private MtRouterLinkRepository repository;

    @ApiOperation(value = "routerLinkGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtRouterLink> routerLinkGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<MtRouterLink> responseData = new ResponseData<MtRouterLink>();
        try {
            responseData.setRows(this.repository.routerLinkGet(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerLinkBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterLink>> routerLinkBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> routerStepIds) {
        ResponseData<List<MtRouterLink>> responseData = new ResponseData<List<MtRouterLink>>();
        try {
            responseData.setRows(this.repository.routerLinkBatchGet(tenantId, routerStepIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
