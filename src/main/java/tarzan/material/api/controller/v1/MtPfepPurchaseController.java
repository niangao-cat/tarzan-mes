package tarzan.material.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.repository.MtPfepPurchaseRepository;

/**
 * 物料采购属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@RestController("mtPfepPurchaseController.v1")
@RequestMapping("/v1/{organizationId}/mt-pfep-purchase")
@Api(tags = "MtPfepPurchase")
public class MtPfepPurchaseController extends BaseController {

    @Autowired
    private MtPfepPurchaseRepository pfepPurchaseRepository;

    @ApiOperation(value = "pfepPurchaseAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property-update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> pfepPurchaseAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtExtendVO10 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            this.pfepPurchaseRepository.pfepPurchaseAttrPropertyUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
