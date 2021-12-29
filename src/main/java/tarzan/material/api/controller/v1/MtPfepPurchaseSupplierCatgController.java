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
import tarzan.material.domain.repository.MtPfepPurchaseSupplierCatgRepository;

/**
 * 物料类别供应商采购属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@RestController("mtPfepPurchaseSupplierCatgController.v1")
@RequestMapping("/v1/{organizationId}/mt-pfep-purchase-supplier-catg")
@Api(tags = "MtPfepPurchaseSupplierCatg")
public class MtPfepPurchaseSupplierCatgController extends BaseController {
    @Autowired
    private MtPfepPurchaseSupplierCatgRepository repository;

    @ApiOperation(value = "pfepPurSupplierCatgAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> pfepPurSupplierCatgAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.pfepPurSupplierCatgAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
