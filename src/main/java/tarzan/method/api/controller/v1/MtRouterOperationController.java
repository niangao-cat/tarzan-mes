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
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.repository.MtRouterOperationRepository;

/**
 * 工艺路线步骤对应工序 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@RestController("mtRouterOperationController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-operation")
@Api(tags = "MtRouterOperation")
public class MtRouterOperationController extends BaseController {

    @Autowired
    private MtRouterOperationRepository repository;

    @ApiOperation(value = "routerOperationGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtRouterOperation> routerOperationGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<MtRouterOperation> responseData = new ResponseData<MtRouterOperation>();
        try {
            responseData.setRows(this.repository.routerOperationGet(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerOperationBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterOperation>> routerOperationBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> routerStepIds) {
        ResponseData<List<MtRouterOperation>> responseData = new ResponseData<List<MtRouterOperation>>();
        try {
            responseData.setRows(this.repository.routerOperationBatchGet(tenantId, routerStepIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
