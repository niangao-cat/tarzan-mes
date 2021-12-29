package tarzan.material.api.controller.v1;


import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.material.api.dto.MtMaterialCategorySiteDTO;
import tarzan.material.app.service.MtMaterialCategorySiteService;
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.repository.MtMaterialCategorySiteRepository;
import tarzan.material.domain.vo.*;
import tarzan.modeling.domain.entity.MtModSite;

import java.util.List;

/**
 * 物料类别站点分配 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@RestController("mtMaterialCategorySiteController.v1")
@RequestMapping("/v1/{organizationId}/mt-material-category-site")
@Api(tags = "MtMaterialCategorySite")
public class MtMaterialCategorySiteController extends BaseController {

    @Autowired
    private MtMaterialCategorySiteRepository repository;

    @Autowired
    private MtMaterialCategorySiteService service;

    @ApiOperation(value = "获取物料类别分配站点列表（前台）")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtMaterialCategorySiteVO>> listMaterialCategorySiteForUi(
                    @PathVariable("organizationId") Long tenantId,
                    @ApiParam(value = "物料类别主键",
                                    required = true) @RequestParam(required = true) String materialCategoryId,
                    @ApiIgnore @SortDefault(value = MtMaterialCategorySite.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtMaterialCategorySiteVO>> result = new ResponseData<Page<MtMaterialCategorySiteVO>>();
        try {
            result.setRows(service.listMaterialCategorySiteForUi(tenantId, materialCategoryId, pageRequest));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "获取未被当前物料类别分配的站点列表（lov）")
    @GetMapping(value = "/site/list/not-exist/lov/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtMaterialCategorySiteVO2>> materialCategorySiteNotExistForUi(
                    @PathVariable("organizationId") Long tenantId, MtMaterialCategorySiteVO3 condition,
                    @ApiIgnore @SortDefault(value = MtModSite.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(service.materialCategorySiteNotExistForUi(tenantId, condition, pageRequest));

    }

    @ApiOperation(value = "新增&更新分配站点（前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveMaterialCategorySiteForUi(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtMaterialCategorySiteDTO dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(service.saveMaterialCategorySiteForUi(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "删除分配站点（前台）")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeMaterialCategorySiteForUi(@PathVariable("organizationId") Long tenantId,
                                                              @ApiParam(value = "物料类别分配站点主键") @RequestBody(required = true) String materialCategorySiteId) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            service.removeMaterialCategorySiteForUi(tenantId, materialCategorySiteId);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategorySiteValidate")
    @PostMapping(value = {"/site/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategorySiteValidate(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtMaterialCategorySiteDTO dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            MtMaterialCategorySite param = new MtMaterialCategorySite();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.materialCategorySiteValidate(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategorySitePfepExistValidate")
    @PostMapping(value = {"/pfep/exist/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategorySitePfepExistValidate(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtMaterialCategorySiteDTO dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            MtMaterialCategorySite param = new MtMaterialCategorySite();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.materialCategorySitePfepExistValidate(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategorySiteLimitRelationGet")
    @PostMapping(value = {"/limit-category-site"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategorySiteLimitRelationGet(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody MtMaterialCategorySiteDTO dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            MtMaterialCategorySite param = new MtMaterialCategorySite();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.materialCategorySiteLimitRelationGet(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "relationLimitMaterialCategorySiteGet")
    @PostMapping(value = "/limit-relation", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialCategorySite> relationLimitMaterialCategorySiteGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String materialCategorySiteId) {
        ResponseData<MtMaterialCategorySite> result = new ResponseData<MtMaterialCategorySite>();
        try {
            result.setRows(repository.relationLimitMaterialCategorySiteGet(tenantId, materialCategorySiteId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategorySiteAssign")
    @PostMapping(value = "/assign", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialCategorySiteAssign(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtMaterialCategorySiteVO4 materialCategorySiteVO) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.materialCategorySiteAssign(tenantId, materialCategorySiteVO));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategorySiteLimitRelationBatchGet")
    @PostMapping(value = "/material-category/site/limit/batch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialCategorySiteVO7>> materialCategorySiteLimitRelationBatchGet(
            @PathVariable("organizationId") Long tenantId,
            @RequestBody List<MtMaterialCategorySiteVO4> materialCategorySiteIds) {
        ResponseData<List<MtMaterialCategorySiteVO7>> result = new ResponseData<>();
        try {
            result.setRows(repository.materialCategorySiteLimitRelationBatchGet(tenantId, materialCategorySiteIds));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialCategorySiteAttrPropertyUpdate")
    @PostMapping(value = "/attr/table/update", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialCategorySiteAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody MtExtendVO10 mtExtendVO10) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.materialCategorySiteAttrPropertyUpdate(tenantId, mtExtendVO10);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
