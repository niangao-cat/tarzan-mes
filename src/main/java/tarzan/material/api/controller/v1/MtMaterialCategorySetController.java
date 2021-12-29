package tarzan.material.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.material.api.dto.MtMaterialCategorySetDTO;
import tarzan.material.app.service.MtMaterialCategorySetService;
import tarzan.material.domain.entity.MtMaterialCategorySet;
import tarzan.material.domain.repository.MtMaterialCategorySetRepository;
import tarzan.material.domain.vo.MtMaterialCategorySetVO;
import tarzan.material.domain.vo.MtMaterialCategorySetVO2;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO5;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO6;

/**
 * 物料类别集 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@RestController("mtMaterialCategorySetController.v1")
@RequestMapping("/v1/{organizationId}/mt-material-category-set")
@Api(tags = "MtMaterialCategorySet")
public class MtMaterialCategorySetController extends BaseController {

    @Autowired
    private MtMaterialCategorySetRepository repository;

    @Autowired
    private MtMaterialCategorySetService mtMaterialCategorySetService;

    @ApiOperation(value = "defaultCategorySetGet")
    @PostMapping(value = "/default-set/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialCategorySet>> defaultCategorySetGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String type) {
        ResponseData<List<MtMaterialCategorySet>> result = new ResponseData<List<MtMaterialCategorySet>>();
        try {
            result.setRows(repository.defaultCategorySetGet(tenantId, type));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategorySetPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialCategorySet> materialCategorySetPropertyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String materialCategorySetId) {
        ResponseData<MtMaterialCategorySet> result = new ResponseData<MtMaterialCategorySet>();
        try {
            result.setRows(repository.materialCategorySetPropertyGet(tenantId, materialCategorySetId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "defaultCategorySetValidate")
    @PostMapping(value = "/enable-validate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> defaultCategorySetValidate(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody String materialCategorySetId) {

        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.defaultCategorySetValidate(tenantId, materialCategorySetId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategorySetPropertyBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialCategorySet>> materialCategorySetPropertyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> materialCategorySetIds) {
        ResponseData<List<MtMaterialCategorySet>> result = new ResponseData<List<MtMaterialCategorySet>>();
        try {
            result.setRows(repository.materialCategorySetPropertyBatchGet(tenantId, materialCategorySetIds));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "物料类别集维护查询(前台)")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtMaterialCategorySet>> listUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    MtMaterialCategorySetDTO dto,
                    @ApiIgnore @SortDefault(value = MtMaterialCategorySet.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtMaterialCategorySet>> result = new ResponseData<Page<MtMaterialCategorySet>>();
        try {
            result.setRows(mtMaterialCategorySetService.listUi(tenantId, dto, pageRequest));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "物料类别集维护新增保存(前台)")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategorySetUpdateForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialCategorySet dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(mtMaterialCategorySetService.materialCategorySetUpdate(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategorySetUpdate")
    @PostMapping(value = {"/set/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategorySetUpdate(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialCategorySetVO vo) {
        ResponseData<String> result = new ResponseData<>();
        try {
            result.setRows(repository.materialCategorySetUpdate(tenantId, vo));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "propertyLimitMaterialCategorySetPropertyQuery")
    @PostMapping(value = {"/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialCategorySiteVO6>> propertyLimitMaterialCategorySetPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtMaterialCategorySiteVO5 dto) {
        ResponseData<List<MtMaterialCategorySiteVO6>> result = new ResponseData<List<MtMaterialCategorySiteVO6>>();
        try {
            result.setRows(repository.propertyLimitMaterialCategorySetPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "defaultCategorySetBatchGet")
    @PostMapping(value = "/default-set/batch/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialCategorySetVO2>> defaultCategorySetBatchGet(
            @PathVariable("organizationId") Long tenantId, @RequestBody List<String> defaultTypes) {
        ResponseData<List<MtMaterialCategorySetVO2>> result = new ResponseData<>();
        try {
            result.setRows(repository.defaultCategorySetBatchGet(tenantId, defaultTypes));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategorySetAttrPropertyUpdate")
    @PostMapping(value = {"/attr/table/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialCategorySetAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.materialCategorySetAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
