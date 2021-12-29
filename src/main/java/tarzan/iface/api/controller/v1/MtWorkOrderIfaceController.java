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
import tarzan.iface.domain.repository.MtWorkOrderIfaceRepository;

/**
 * 工单接口表 管理 API
 *
 * @author xiao.tang02@hand-china.com 2019-08-23 14:16:17
 */
@RestController("mtWorkOrderIfaceController.v1")
@RequestMapping("/v1/{organizationId}/mt-work-order-iface")
@Api(tags = "MtWorkOrderIface")
public class MtWorkOrderIfaceController extends BaseController {

    @Autowired
    private MtWorkOrderIfaceRepository repository;
    @ApiOperation(value = "routerInterfaceImport")
    @PostMapping(value = "/import",produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> routerInterfaceImport(@PathVariable(value = "organizationId") Long tenantId) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            repository.workOrderInterfaceImport(tenantId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
