package tarzan.material.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.material.app.service.MtMaterialCategoryAssignService;
import tarzan.material.domain.entity.MtMaterialCategoryAssign;
import tarzan.material.domain.repository.MtMaterialCategoryAssignRepository;
import tarzan.material.domain.vo.*;

/**
 * 物料类别分配 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@RestController("mtMaterialCategoryAssignController.v1")
@RequestMapping("/v1/{organizationId}/mt-material-category-assign")
@Api(tags = "MtMaterialCategoryAssign")
public class MtMaterialCategoryAssignController extends BaseController {

    @Autowired
    private MtMaterialCategoryAssignRepository repository;
    @Autowired
    private MtMaterialCategoryAssignService mtMaterialCategoryAssignService;

    @ApiOperation(value = "setLimitMaterialAssignCategoryGet")
    @PostMapping(value = {"/limit-site-set"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> setLimitMaterialAssignCategoryGet(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody MtMaterialCategoryAssignVO dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.setLimitMaterialAssignCategoryGet(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "defaultSetMaterialAssignCategoryGet")
    @PostMapping(value = {"/limit-site-default"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> defaultSetMaterialAssignCategoryGet(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody MtMaterialCategoryAssignVO dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.defaultSetMaterialAssignCategoryGet(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "categorySiteLimitMaterialQuery")
    @PostMapping(value = {"/limit-site-enable"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> categorySiteLimitMaterialQuery(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody MtMaterialCategoryAssignVO dto) {

        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.categorySiteLimitMaterialQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategoryAssignValidate")
    @PostMapping(value = {"/limit-site-validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategoryAssignValidate(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtMaterialCategoryAssignVO dto) {

        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.materialCategoryAssignValidate(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategoryAssignUniqueValidate")
    @PostMapping(value = {"/limit-site-unique/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategoryAssignUniqueValidate(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody MtMaterialCategoryAssignVO dto) {

        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.materialCategoryAssignUniqueValidate(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategoryAssign")
    @PostMapping(value = {"/new"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategoryAssign(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody MtMaterialCategoryAssignVO2 dto) {

        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.materialCategoryAssign(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "defaultSetMaterialAssignCategoryBatchGet")
    @PostMapping(value = {"/limit-site-default/batch/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialCategoryAssignVO6>> defaultSetMaterialAssignCategoryBatchGet(
            @PathVariable("organizationId") Long tenantId, @RequestBody List<MtMaterialCategoryAssignVO5> dto) {
        ResponseData<List<MtMaterialCategoryAssignVO6>> result = new ResponseData<>();
        try {
            result.setRows(repository.defaultSetMaterialAssignCategoryBatchGet(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "setLimitMaterialAssignCategoryBatchGet")
    @PostMapping(value = {"/limit-site-set/batch/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialCategoryAssignVO4>> setLimitMaterialAssignCategoryBatchGet(
            @PathVariable("organizationId") Long tenantId, @RequestBody List<MtMaterialCategoryAssignVO3> dto) {
        ResponseData<List<MtMaterialCategoryAssignVO4>> result = new ResponseData<>();
        try {
            result.setRows(repository.setLimitMaterialAssignCategoryBatchGet(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("物料维护物料分配查询(前台)")
    @GetMapping(value = "/assign/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MaterialCategoryAssignVO>> materialAssignForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable(value = "organizationId") Long tenantId,
                    @ApiParam(value = "物料ID", required = true) @RequestParam String materialId,
                    @ApiIgnore @SortDefault(value = MtMaterialCategoryAssign.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MaterialCategoryAssignVO>> responseData = new ResponseData<Page<MaterialCategoryAssignVO>>();
        try {
            responseData.setRows(mtMaterialCategoryAssignService.selectMaterialCategoryAssiteById(tenantId, materialId,
                            pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "删除物料类别分配信息(前台)")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialCategoryAssignDeleteForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @ApiParam(value = "物料类别分配id集合") @RequestBody List<String> materialCategoryAssignIds) {

        ResponseData<Void> result = new ResponseData<Void>();
        try {
            mtMaterialCategoryAssignService.materialCategoryAssignDelete(tenantId, materialCategoryAssignIds);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
