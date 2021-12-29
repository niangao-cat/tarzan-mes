package tarzan.material.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
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
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.material.api.dto.MtMaterialCategoryDTO;
import tarzan.material.api.dto.MtMaterialCategoryDTO2;
import tarzan.material.app.service.MtMaterialCategoryService;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.vo.MtMaterialCategoryVO;
import tarzan.material.domain.vo.MtMaterialCategoryVO2;
import tarzan.material.domain.vo.MtMaterialCategoryVO3;
import tarzan.material.domain.vo.MtMaterialCategoryVO4;
import tarzan.material.domain.vo.MtMaterialCategoryVO5;
import tarzan.material.domain.vo.MtMaterialCategoryVO6;

/**
 * 物料类别 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@RestController("mtMaterialCategoryController.v1")
@RequestMapping("/v1/{organizationId}/mt-material-category")
@Api(tags = "MtMaterialCategory")
public class MtMaterialCategoryController extends BaseController {

    @Autowired
    private MtMaterialCategoryService service;

    @Autowired
    private MtMaterialCategoryRepository repository;

    @ApiOperation(value = "获取物料类别列表（前台）")
    @GetMapping(value = "/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtMaterialCategoryVO3>> listMaterialCategoryForUi(
                    @PathVariable("organizationId") Long tenantId, MtMaterialCategoryVO2 condition,
                    @ApiIgnore @SortDefault(value = MtMaterialCategory.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {

        ResponseData<Page<MtMaterialCategoryVO3>> responseData = new ResponseData<Page<MtMaterialCategoryVO3>>();
        try {
            responseData.setRows(service.listMaterialCategoryForUi(tenantId, condition, pageRequest));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "新增&更新物料类别（前台）")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveMaterialCategoryForUi(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody MtMaterialCategoryDTO2 dto) {

        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(service.saveMaterialCategoryForUi(tenantId, dto));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "默认物料类别集校验（前台）")
    @GetMapping(value = "/verify/default-set/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Boolean> verifyDefaultSetForUi(@PathVariable("organizationId") Long tenantId,
                                                       @RequestParam("materialCategoryId") String materialCategoryId) {

        ResponseData<Boolean> responseData = new ResponseData<Boolean>();
        try {
            responseData.setRows(service.verifyDefaultSetForUi(tenantId, materialCategoryId));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "materialCategoryGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialCategory> materialCategoryGet(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody String materialCategoryId) {

        ResponseData<MtMaterialCategory> responseData = new ResponseData<MtMaterialCategory>();
        try {
            responseData.setRows(repository.materialCategoryGet(tenantId, materialCategoryId));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialCategoryEnableValidate")
    @PostMapping(value = "/enable/validate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategoryEnableValidate(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody String materialCategoryId) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.materialCategoryEnableValidate(tenantId, materialCategoryId));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "setLimitMaterialCategoryQuery")
    @PostMapping(value = "/limit-category-set", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> setLimitMaterialCategoryQuery(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody String materialCategorySetId) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.setLimitMaterialCategoryQuery(tenantId, materialCategorySetId));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "codeLimitMaterialCategoryQuery")
    @PostMapping(value = {"/limit-code-enable"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> codeLimitMaterialCategoryQuery(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody MtMaterialCategoryDTO dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtMaterialCategory param = new MtMaterialCategory();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.codeLimitMaterialCategoryQuery(tenantId, param));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialCategoryPropertyBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialCategory>> materialCategoryPropertyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> materialCategoryIds) {

        ResponseData<List<MtMaterialCategory>> responseData = new ResponseData<List<MtMaterialCategory>>();
        try {
            responseData.setRows(repository.materialCategoryPropertyBatchGet(tenantId, materialCategoryIds));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialCategoryCodeGet")
    @PostMapping(value = "/code", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialCategoryVO> materialCategoryCodeGet(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody String materialCategoryId) {

        ResponseData<MtMaterialCategoryVO> responseData = new ResponseData<MtMaterialCategoryVO>();
        try {
            responseData.setRows(repository.materialCategoryCodeGet(tenantId, materialCategoryId));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialCategorySetGet")
    @PostMapping(value = "/category-set", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategorySetGet(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody String materialCategoryId) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.materialCategorySetGet(tenantId, materialCategoryId));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialCategoryUpdate")
    @PostMapping(value = "/category/update", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategoryUpdate(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody MtMaterialCategoryVO4 vo) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.materialCategoryUpdate(tenantId, vo));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitMaterialCategoryPropertyQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialCategoryVO6>> propertyLimitMaterialCategoryPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtMaterialCategoryVO5 dto) {
        ResponseData<List<MtMaterialCategoryVO6>> result = new ResponseData<List<MtMaterialCategoryVO6>>();
        try {
            result.setRows(repository.propertyLimitMaterialCategoryPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategorySetBatchGet")
    @PostMapping(value = "/category-set/batch/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialCategoryVO6>> materialCategorySetBatchGet(
            @PathVariable("organizationId") Long tenantId, @RequestBody List<String> materialCategoryIdList) {
        ResponseData<List<MtMaterialCategoryVO6>> responseData = new ResponseData<>();
        try {
            responseData.setRows(repository.materialCategorySetBatchGet(tenantId, materialCategoryIdList));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialCategoryAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialCategoryAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.materialCategoryAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
