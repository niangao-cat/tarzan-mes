package tarzan.method.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.method.domain.entity.MtDispositionFunction;
import tarzan.method.domain.repository.MtDispositionFunctionRepository;

/**
 * 处置方法 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:47
 */
@RestController("mtDispositionFunctionController.v1")
@RequestMapping("/v1/{organizationId}/mt-disposition-function")
@Api(tags = "MtDispositionFunction")
public class MtDispositionFunctionController extends BaseController {

    @Autowired
    private MtDispositionFunctionRepository repository;

    @ApiOperation(value = "dispositionFunctionPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtDispositionFunction> dispositionFunctionPropertyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String dispositionFunctionId) {
        ResponseData<MtDispositionFunction> responseData = new ResponseData<MtDispositionFunction>();
        try {
            responseData.setRows(repository.dispositionFunctionPropertyGet(tenantId,
                            dispositionFunctionId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
