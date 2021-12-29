package tarzan.iface.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.iface.domain.repository.MtPoHeaderRepository;

/**
 * 采购订单头表 管理 API
 *
 * @author yiyang.xie@hand-china.com 2019-10-08 19:52:47
 */
@RestController("mtPoHeaderController.v1")
@RequestMapping("/v1/{organizationId}/mt-po-header")
@Api(tags = "MtPoHeader")
public class MtPoHeaderController extends BaseController {

    @Autowired
    private MtPoHeaderRepository mtPoHeaderRepository;

    @ApiOperation(value = "poHeaderAttrPropertyUpdate")
    @PostMapping(value = {"/property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> poHeaderAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendVO10 mtExtendVO10) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            mtPoHeaderRepository.poHeaderAttrPropertyUpdate(tenantId, mtExtendVO10);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
