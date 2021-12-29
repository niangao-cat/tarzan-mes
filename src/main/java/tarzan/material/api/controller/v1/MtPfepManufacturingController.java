package tarzan.material.api.controller.v1;

import java.util.List;

import io.tarzan.common.domain.vo.MtExtendVO10;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.material.api.dto.MtPfepInventoryDTO;
import tarzan.material.api.dto.MtPfepManufacturingDTO;
import tarzan.material.api.dto.MtPfepManufacturingDTO2;
import tarzan.material.api.dto.MtPfepManufacturingDTO3;
import tarzan.material.app.service.MtPfepManufacturingCategoryService;
import tarzan.material.app.service.MtPfepManufacturingService;
import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.repository.MtPfepManufacturingRepository;
import tarzan.material.domain.vo.*;

/**
 * 物料生产属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@RestController("mtPfepManufacturingController.v1")
@RequestMapping("/v1/{organizationId}/mt-pfep-manufacturing")
@Api(tags = "MtPfepManufacturing")
public class MtPfepManufacturingController extends BaseController {

    @Autowired
    private MtPfepManufacturingRepository repository;

    @Autowired
    private MtPfepManufacturingService mtPfepManufacturingService;

    @Autowired
    private MtPfepManufacturingCategoryService mtPfepManufacturingCategoryService;

    @ApiOperation(value = "pfepOperationAssembleFlagGet")
    @PostMapping(value = {"/material/operation-assemble-flag"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> pfepOperationAssembleFlagGet(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtPfepInventoryDTO condition) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtPfepInventoryVO param = new MtPfepInventoryVO();
            BeanUtils.copyProperties(condition, param);
            responseData.setRows(repository.pfepOperationAssembleFlagGet(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "pfepDefaultBomGet")
    @PostMapping(value = {"/material/bom"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> pfepDefaultBomGet(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody MtPfepInventoryDTO dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            MtPfepInventoryVO param = new MtPfepInventoryVO();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.pfepDefaultBomGet(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepDefaultRouterGet")
    @PostMapping(value = {"/material/router"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> pfepDefaultRouterGet(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody MtPfepInventoryDTO dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            MtPfepInventoryVO param = new MtPfepInventoryVO();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.pfepDefaultRouterGet(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepManufacturingIssueControlGet")
    @PostMapping(value = {"/material/issue/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepManufacturing> pfepManufacturingIssueControlGet(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtPfepInventoryDTO dto) {
        ResponseData<MtPfepManufacturing> result = new ResponseData<MtPfepManufacturing>();
        try {
            MtPfepInventoryVO param = new MtPfepInventoryVO();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.pfepManufacturingIssueControlGet(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepManufacturingCompleteControlGet")
    @PostMapping(value = {"/material/complete/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepManufacturing> pfepManufacturingCompleteControlGet(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtPfepInventoryDTO dto) {
        ResponseData<MtPfepManufacturing> result = new ResponseData<MtPfepManufacturing>();
        try {
            MtPfepInventoryVO param = new MtPfepInventoryVO();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.pfepManufacturingCompleteControlGet(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepManufacturingAttritionControlGet")
    @PostMapping(value = {"/material/attrition/parameter"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepManufacturing> pfepManufacturingAttritionControlGet(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtPfepInventoryDTO dto) {
        ResponseData<MtPfepManufacturing> result = new ResponseData<MtPfepManufacturing>();
        try {
            MtPfepInventoryVO param = new MtPfepInventoryVO();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.pfepManufacturingAttritionControlGet(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepRouterLimitMaterialQuery")
    @PostMapping(value = "/limit-pfep-router/material/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtPfepInventoryVO>> pfepRouterLimitMaterialQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody String defaultRoutingId) {
        ResponseData<List<MtPfepInventoryVO>> result = new ResponseData<List<MtPfepInventoryVO>>();
        try {
            result.setRows(repository.pfepRouterLimitMaterialQuery(tenantId, defaultRoutingId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepRouterLimitMaterialCategoryQuery")
    @PostMapping(value = "/limit-pfep-router/category/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtPfepInventoryVO2>> pfepRouterLimitMaterialCategoryQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody String defaultRoutingId) {
        ResponseData<List<MtPfepInventoryVO2>> result = new ResponseData<List<MtPfepInventoryVO2>>();
        try {
            result.setRows(repository.pfepRouterLimitMaterialCategoryQuery(tenantId, defaultRoutingId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepBomLimitMaterialQuery")
    @PostMapping(value = "/limit-pfep-bom/material/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtPfepInventoryVO>> pfepBomLimitMaterialQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody String defaultBomId) {
        ResponseData<List<MtPfepInventoryVO>> result = new ResponseData<List<MtPfepInventoryVO>>();
        try {
            result.setRows(repository.pfepBomLimitMaterialQuery(tenantId, defaultBomId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepBomLimitMaterialCategoryQuery")
    @PostMapping(value = "/limit-pfep-bom/category/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtPfepInventoryVO2>> pfepBomLimitMaterialCategoryQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody String defaultRoutingId) {
        ResponseData<List<MtPfepInventoryVO2>> result = new ResponseData<List<MtPfepInventoryVO2>>();
        try {
            result.setRows(repository.pfepBomLimitMaterialCategoryQuery(tenantId, defaultRoutingId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "UI获取物料生产属性列表")
    @GetMapping(value = "/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtPfepManufacturingVO>> listForUi(@PathVariable("organizationId") Long tenantId,
                                                               @ApiParam("物料ID") @RequestParam(name = "materialId", required = false) String materialId,
                                                               @ApiParam("物料类别ID") @RequestParam(name = "materialCategoryId",
                                                                       required = false) String materialCategoryId,
                                                               @ApiIgnore PageRequest pageRequest) {
        ResponseData<Page<MtPfepManufacturingVO>> result = new ResponseData<Page<MtPfepManufacturingVO>>();
        try {
            if (StringUtils.isNotEmpty(materialCategoryId)) {
                result.setRows(mtPfepManufacturingCategoryService.listForUi(tenantId, materialCategoryId, pageRequest));
            } else {
                result.setRows(mtPfepManufacturingService.listForUi(tenantId, materialId, pageRequest));
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "UI获取物料生产属性详情")
    @GetMapping(value = "/detail/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepManufacturingVO2> detailForUi(@PathVariable("organizationId") Long tenantId,
                                                            @ApiParam(value = "主键ID", required = true) @RequestParam(name = "kid") String kid,
                                                            @ApiParam(value = "类型", required = true) @RequestParam(name = "keyType") String keyType,
                                                            @ApiIgnore PageRequest pageRequest) {
        ResponseData<MtPfepManufacturingVO2> result = new ResponseData<MtPfepManufacturingVO2>();
        try {
            if (StringUtils.isEmpty(kid) || StringUtils.isEmpty(keyType)) {
                result.setRows(null);
            } else if ("material".equals(keyType)) {
                result.setRows(mtPfepManufacturingService.detailForUi(tenantId, kid));
            } else if ("category".equals(keyType)) {
                result.setRows(mtPfepManufacturingCategoryService.detailForUi(tenantId, kid));
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "UI新增$更新物料生产属性")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepManufacturingDTO3> saveForUi(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtPfepManufacturingDTO dto) {
        ResponseData<MtPfepManufacturingDTO3> result = new ResponseData<MtPfepManufacturingDTO3>();
        try {
            if ("material".equals(dto.getKeyType())) {
                result.setRows(mtPfepManufacturingService.saveMtPfepManufacturingForUi(tenantId, dto));
            } else if ("category".equals(dto.getKeyType())) {
                result.setRows(mtPfepManufacturingCategoryService.savePfepManufacturingCategoryForUi(tenantId, dto));
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "UI复制物料生产属性")
    @PostMapping(value = "/copy/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepManufacturingDTO3> copyForUi(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtPfepManufacturingDTO2 dto) {
        ResponseData<MtPfepManufacturingDTO3> result = new ResponseData<MtPfepManufacturingDTO3>();
        try {
            if (StringUtils.isNotEmpty(dto.getSourceMaterialId())) {
                result.setRows(mtPfepManufacturingService.copyPfepManufacturingForUi(tenantId, dto));
            } else if (StringUtils.isNotEmpty(dto.getSourceMaterialCategoryId())) {
                result.setRows(mtPfepManufacturingCategoryService.copyPfepManufacturingCategoryForUi(tenantId, dto));
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialPfepManufacturingUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialPfepManufacturingUpdate(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody MtPfepManufacturingVO3 dto,
                                                                @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.materialPfepManufacturingUpdate(tenantId, dto, fullUpdate));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "propertyLimitPfepManufacturingGet")
    @PostMapping(value = {"/manufacturing/kid/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepManufacturingVO5> propertyLimitPfepManufacturingGet(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtPfepManufacturingVO4 dto) {
        ResponseData<MtPfepManufacturingVO5> result = new ResponseData<MtPfepManufacturingVO5>();
        try {
            result.setRows(repository.propertyLimitPfepManufacturingGet(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepManufacturingBatchGet")
    @PostMapping(value = {"/batch-get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtPfepManufacturingVO11>> pfepManufacturingBatchGet(
            @PathVariable("organizationId") Long tenantId, @RequestBody List<MtPfepManufacturingVO1> voList,
            @RequestParam(name = "fields", required = false) List<String> fields) {
        ResponseData<List<MtPfepManufacturingVO11>> result = new ResponseData<>();
        try {
            result.setRows(repository.pfepManufacturingBatchGet(tenantId, voList, fields));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "pfepManufacturingAttrPropertyUpdate")
    @PostMapping(value = {"/manufacturing/attr/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepManufacturingVO5> pfepManufacturingAttrPropertyUpdate(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtExtendVO10 dto) {
        ResponseData<MtPfepManufacturingVO5> result = new ResponseData<MtPfepManufacturingVO5>();
        try {
            repository.pfepManufacturingAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
