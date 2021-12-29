package tarzan.iface.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.iface.domain.repository.MtStandardOperationIfaceRepository;

/**
 * 标准工序接口表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@RestController("mtStandardOperationIfaceController.v1")
@RequestMapping("/v1/{organizationId}/mt-standard-operation-iface")
@Api(tags = "MtStandardOperationIface")
public class MtStandardOperationIfaceController extends BaseController {

    @Autowired
    private MtStandardOperationIfaceRepository repository;

    @ApiOperation("standardOperationInterfaceImport")
    @PostMapping(value = {"/import"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> standardOperationInterfaceImport(@PathVariable("organizationId") Long tenantId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.standardOperationInterfaceImport(tenantId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
    
}
