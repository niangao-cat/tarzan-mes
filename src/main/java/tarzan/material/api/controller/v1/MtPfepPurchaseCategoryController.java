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
import tarzan.material.domain.repository.MtPfepPurchaseCategoryRepository;

/**
 * 物料类别采购属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@RestController("mtPfepPurchaseCategoryController.v1")
@RequestMapping("/v1/{organizationId}/mt-pfep-purchase-category")
@Api(tags = "MtPfepPurchaseCategory")
public class MtPfepPurchaseCategoryController extends BaseController {

    @Autowired
    private MtPfepPurchaseCategoryRepository repository;

    @ApiOperation(value = "pfepPurchaseCategoryAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> pfepPurchaseCategoryAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.pfepPurchaseCategoryAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
