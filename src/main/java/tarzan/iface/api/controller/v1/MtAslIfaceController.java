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
import tarzan.iface.domain.repository.MtAslIfaceRepository;

/**
 * 合格供应商（货源清单）数据接口 管理 API
 *
 * @author mingjie.chen@hand-china.com 2019-09-09 17:05:24
 */
@RestController("mtAslIfaceController.v1")
@RequestMapping("/v1/{organizationId}/mt-asl-iface")
@Api(tags = "MtAslIface")
public class MtAslIfaceController extends BaseController {

    @Autowired
    private MtAslIfaceRepository repository;

    @ApiOperation("aslInterfaceImport")
    @PostMapping(value = {"/import"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> aslInterfaceImport(@PathVariable("organizationId") Long tenantId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.aslInterfaceImport(tenantId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
