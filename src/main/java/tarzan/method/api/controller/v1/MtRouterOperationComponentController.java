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
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.method.domain.repository.MtRouterOperationComponentRepository;
import tarzan.method.domain.vo.*;

/**
 * 工艺路线步骤对应工序组件 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@RestController("mtRouterOperationComponentController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-operation-component")
@Api(tags = "MtRouterOperationComponent")
public class MtRouterOperationComponentController extends BaseController {

    @Autowired
    private MtRouterOperationComponentRepository repository;

    @ApiOperation(value = "routerOperationComponentQuery")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterOperationComponent>> routerOperationComponentQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String routerStepId) {
        ResponseData<List<MtRouterOperationComponent>> responseData =
                        new ResponseData<List<MtRouterOperationComponent>>();
        try {
            responseData.setRows(this.repository.routerOperationComponentQuery(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerOperationComponentPerQtyQuery")
    @PostMapping(value = {"/pre-qty"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterOpComponentVO>> routerOperationComponentPerQtyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtRouterOpComponentVO1 condition) {
        ResponseData<List<MtRouterOpComponentVO>> responseData = new ResponseData<List<MtRouterOpComponentVO>>();
        try {
            responseData.setRows(this.repository.routerOperationComponentPerQtyQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerOperationComponentVerify")
    @PostMapping(value = {"/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> routerOperationComponentVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtRouterOpComponentVO2 condition) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.routerOperationComponentVerify(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationOrMaterialLimitBomComponentQuery")
    @PostMapping(value = {"/material-limit/bom-component"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> operationOrMaterialLimitBomComponentQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtRouterOpComponentVO3 condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.operationOrMaterialLimitBomComponentQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerOperationComponentPerQtyBatchQuery")
    @PostMapping(value = {"/pre-qty/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterOpComponentVO>> routerOperationComponentPerQtyBatchQuery(
                    @PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtRouterOpComponentVO1> condition) {
        ResponseData<List<MtRouterOpComponentVO>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.routerOperationComponentPerQtyBatchQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationOrMaterialLimitBomComponentBatchQuery")
    @PostMapping(value = {"/material-limit/bom-component/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterOpComponentVO6>> operationOrMaterialLimitBomComponentBatchQuery(
                    @PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtRouterOpComponentVO3> condition) {
        ResponseData<List<MtRouterOpComponentVO6>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.operationOrMaterialLimitBomComponentBatchQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
