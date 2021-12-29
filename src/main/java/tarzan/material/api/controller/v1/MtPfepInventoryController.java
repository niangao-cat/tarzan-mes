package tarzan.material.api.controller.v1;

import org.apache.commons.lang3.StringUtils;
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
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.material.api.dto.MtPfepInventoryDTO;
import tarzan.material.api.dto.MtPfepInventoryDTO2;
import tarzan.material.api.dto.MtPfepInventoryDTO3;
import tarzan.material.api.dto.MtPfepInventoryDTO4;
import tarzan.material.app.service.MtPfepInventoryCategoryService;
import tarzan.material.app.service.MtPfepInventoryService;
import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.material.domain.vo.MtPfepInventoryVO3;
import tarzan.material.domain.vo.MtPfepInventoryVO4;
import tarzan.material.domain.vo.MtPfepInventoryVO6;
import tarzan.material.domain.vo.MtPfepInventoryVO7;
import tarzan.material.domain.vo.MtPfepInventoryVO8;

import java.util.List;

/**
 * 物料存储属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@RestController("mtPfepInventoryController.v1")
@RequestMapping("/v1/{organizationId}/mt-pfep-inventory")
@Api(tags = "MtPfepInventory")
public class MtPfepInventoryController extends BaseController {

    @Autowired
    private MtPfepInventoryRepository repository;

    @Autowired
    private MtPfepInventoryService service;

    @Autowired
    private MtPfepInventoryCategoryService categoryService;

    @ApiOperation(value = "pfepInventoryGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepInventory> pfepInventoryGet(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody MtPfepInventoryDTO dto) {
        ResponseData<MtPfepInventory> result = new ResponseData<MtPfepInventory>();
        try {
            MtPfepInventoryVO param = new MtPfepInventoryVO();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.pfepInventoryGet(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepDefaultManufacturingLocationGet")
    @PostMapping(value = {"/location"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepInventory> pfepDefaultManufacturingLocationGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtPfepInventoryDTO dto) {
        ResponseData<MtPfepInventory> result = new ResponseData<MtPfepInventory>();
        try {
            MtPfepInventoryVO param = new MtPfepInventoryVO();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.pfepDefaultManufacturingLocationGet(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialIdentifyTypeValidate")
    @PostMapping(value = {"/identify/type/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialIdentifyTypeValidate(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtPfepInventoryVO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialIdentifyTypeValidate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI根据物料或物料类别获取详细PFEP")
    @GetMapping(value = "/limit-detial/property/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtPfepInventoryVO4>> queryDetial(@PathVariable("organizationId") Long tenantId,
                                                              @ApiIgnore PageRequest pageRequest, MtPfepInventoryDTO4 dto) {
        ResponseData<Page<MtPfepInventoryVO4>> responseData = new ResponseData<>();
        try {
            responseData.setRows(service.pfepInventoryDetialQuery(tenantId, pageRequest, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "保存物料或物料类别对应PFEP属性UI")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepInventoryDTO4> saveMtPfepInventory(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtPfepInventoryDTO2 dto) {
        validObject(dto);
        ResponseData<MtPfepInventoryDTO4> result = new ResponseData<>();
        try {
            result.setRows(service.pfepInventorySave(tenantId, dto));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "复制物料或物料类别存储属性UI")
    @PostMapping(value = "/copy/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepInventoryDTO4> copyMtPfepInventory(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtPfepInventoryDTO3 dto) {
        ResponseData<MtPfepInventoryDTO4> result = new ResponseData<>();
        try {
            if (StringUtils.isNotEmpty(dto.getSourceMaterialId())
                            && StringUtils.isNotEmpty(dto.getTargetMaterialId())) {
                result.setRows(service.copyMtPfepInventory(tenantId, dto));
            } else if (StringUtils.isNotEmpty(dto.getSourceMaterialCategoryId())
                            && StringUtils.isNotEmpty(dto.getTargetMaterialCategoryId())) {
                result.setRows(categoryService.copyMtPfepInventoryCategory(tenantId, dto));
            }

        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialPfepInventoryUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialPfepInventoryUpdate(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtPfepInventoryVO6 vo,
                                                            @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(this.repository.materialPfepInventoryUpdate(tenantId, vo, fullUpdate));

        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "pfepInventoryKidGet")
    @PostMapping(value = "/kid/get", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepInventoryVO8> pfepInventoryKidGet(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody MtPfepInventoryVO7 vo) {
        ResponseData<MtPfepInventoryVO8> result = new ResponseData<MtPfepInventoryVO8>();
        try {
            result.setRows(this.repository.pfepInventoryKidGet(tenantId, vo));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepInventoryBatchGet")
    @PostMapping(value = {"/batch-get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtPfepInventoryVO3>> pfepInventoryBatchGet(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestBody List<MtPfepInventoryVO> voList,
                                                                        @RequestParam(name = "fields", required = false) List<String> fields) {
        ResponseData<List<MtPfepInventoryVO3>> result = new ResponseData<>();
        try {
            result.setRows(repository.pfepInventoryBatchGet(tenantId, voList, fields));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pfepInventoryAttrPropertyUpdate")
    @PostMapping(value = "/attr/table/update", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> pfepInventoryAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtExtendVO10 mtExtendVO10) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            this.repository.pfepInventoryAttrPropertyUpdate(tenantId, mtExtendVO10);
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }
}
