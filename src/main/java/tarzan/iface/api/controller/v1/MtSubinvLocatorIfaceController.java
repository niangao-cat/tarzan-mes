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
import tarzan.iface.domain.repository.MtSubinvLocatorIfaceRepository;

/**
 * ERP货位接口表 管理 API
 *
 * @author guichuan.li@hand-china.com 2019-09-24 10:33:46
 */
@RestController("mtSubinvLocatorIfaceController.v1")
@RequestMapping("/v1/{organizationId}/mt-subinv-locator-iface")
@Api(tags = "MtSubinvLocatorIface")
public class MtSubinvLocatorIfaceController extends BaseController {
    @Autowired
    private MtSubinvLocatorIfaceRepository repository;

    @ApiOperation("subinvLocatorInterfaceImport")
    @PostMapping(value = {"/import"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> subinvLocatorInterfaceImport(@PathVariable("organizationId") Long tenantId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.subinvLocatorInterfaceImport(tenantId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
