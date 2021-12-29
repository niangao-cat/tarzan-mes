package tarzan.material.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.repository.MtPfepInventoryCategoryRepository;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO;

/**
 * 物料类别存储属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@RestController("mtPfepInventoryCategoryController.v1")
@RequestMapping("/v1/{organizationId}/mt-pfep-inventory-category")
@Api(tags = "MtPfepInventoryCategory")
public class MtPfepInventoryCategoryController extends BaseController {

    @Autowired
    private MtPfepInventoryCategoryRepository repository;

    @ApiOperation(value = "materialCategoryPfepInventoryUpdate")
    @PostMapping(value = {"/pfepInventory/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategoryPfepInventoryUpdate(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody MtPfepInventoryCategoryVO dto,
                                                                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.materialCategoryPfepInventoryUpdate(tenantId, dto, fullUpdate));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepInventoryCatgAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property-update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> pfepInventoryCatgAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody MtExtendVO10 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            this.repository.pfepInventoryCatgAttrPropertyUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
