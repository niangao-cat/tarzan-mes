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
import tarzan.material.domain.repository.MtPfepManufacturingCategoryRepository;
import tarzan.material.domain.vo.MtPfepManufactureCateVO1;

/**
 * 物料类别生产属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@RestController("mtPfepManufacturingCategoryController.v1")
@RequestMapping("/v1/{organizationId}/mt-pfep-manufacturing-category")
@Api(tags = "MtPfepManufacturingCategory")
public class MtPfepManufacturingCategoryController extends BaseController {

    @Autowired
    private MtPfepManufacturingCategoryRepository repository;

    @ApiOperation(value = "materialCategoryPfepManufacturingUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategoryPfepManufacturingUpdate(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestBody MtPfepManufactureCateVO1 dto,
                                                                        @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.materialCategoryPfepManufacturingUpdate(tenantId, dto, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "pfepMfgCatgAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> pfepMfgCatgAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.pfepMfgCatgAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
