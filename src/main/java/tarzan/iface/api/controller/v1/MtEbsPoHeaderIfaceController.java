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
import tarzan.iface.domain.repository.MtEbsPoHeaderIfaceRepository;

/**
 * EBS采购订单接口表 管理 API
 *
 * @author guichuan.li@hand-china.com 2019-10-08 15:19:56
 */
@RestController("mtEbsPoHeaderIfaceController.v1")
@RequestMapping("/v1/{organizationId}/mt-ebs-po-header-iface")
@Api(tags = "MtEbsPoHeaderIface")
public class MtEbsPoHeaderIfaceController extends BaseController {
    @Autowired
    private MtEbsPoHeaderIfaceRepository repository;

    @ApiOperation("ebsPoHeaderInterfaceImport")
    @PostMapping(value = {"/import"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> ebsPoHeaderInterfaceImport(@PathVariable("organizationId") Long tenantId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.ebsPoHeaderInterfaceImport(tenantId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
