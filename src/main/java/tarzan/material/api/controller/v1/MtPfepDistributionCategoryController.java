package tarzan.material.api.controller.v1;

import java.util.List;

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
import tarzan.material.domain.repository.MtPfepDistributionCategoryRepository;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO2;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO3;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO4;

/**
 * 物料类别配送属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@RestController("mtPfepDistributionCategoryController.v1")
@RequestMapping("/v1/{organizationId}/mt-pfep-distribution-category")
@Api(tags = "MtPfepDistributionCategory")
public class MtPfepDistributionCategoryController extends BaseController {

    @Autowired
    private MtPfepDistributionCategoryRepository repository;

    @ApiOperation(value = "pfepDistributionCatgAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property-update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> pfepDistributionCatgAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody MtExtendVO10 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            this.repository.pfepDistributionCatgAttrPropertyUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialCategoryPfepDistributionUpdate")
    @PostMapping(value = {"/category/pfep/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategoryPfepDistributionUpdate(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody MtPfepInventoryCategoryVO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.materialCategoryPfepDistributionUpdate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "materialCategoryDistributionPfepQuery")
    @PostMapping(value = {"/category/pfep/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtPfepInventoryCategoryVO4>> materialCategoryDistributionPfepQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtPfepInventoryCategoryVO3 dto) {
        ResponseData<List<MtPfepInventoryCategoryVO4>> responseData =
                        new ResponseData<List<MtPfepInventoryCategoryVO4>>();
        try {
            responseData.setRows(this.repository.materialCategoryDistributionPfepQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
