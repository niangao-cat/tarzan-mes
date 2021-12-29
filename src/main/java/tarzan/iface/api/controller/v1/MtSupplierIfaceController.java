package tarzan.iface.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import tarzan.iface.domain.repository.MtSupplierIfaceRepository;

/**
 * 供应商数据接口表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@RestController("mtSupplierIfaceController.v1")
@RequestMapping("/v1/{organizationId}/mt-supplier-iface")
@Api(tags = "MtSupplierIface")
public class MtSupplierIfaceController extends BaseController {
    @Autowired
    private MtSupplierIfaceRepository repository;

    @ApiOperation(value="supplierInterfaceImport")
    @PostMapping(value = "/import", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> supplierInterfaceImport(@PathVariable(value = "organizationId") Long tenantId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.supplierInterfaceImport(tenantId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
