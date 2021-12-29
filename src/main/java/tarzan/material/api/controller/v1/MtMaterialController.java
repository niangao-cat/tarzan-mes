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
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.material.api.dto.MtMaterialDTO;
import tarzan.material.api.dto.MtMaterialDTO2;
import tarzan.material.api.dto.MtMaterialDTO3;
import tarzan.material.api.dto.MtMaterialDTO4;
import tarzan.material.api.dto.MtMaterialDTO5;
import tarzan.material.api.dto.MtMaterialDTO6;
import tarzan.material.app.service.MtMaterialService;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtMaterialVO1;
import tarzan.material.domain.vo.MtMaterialVO2;
import tarzan.material.domain.vo.MtMaterialVO4;
import tarzan.material.domain.vo.MtMaterialVO5;

/**
 * 物料基础属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@RestController("mtMaterialController.v1")
@RequestMapping("/v1/{organizationId}/mt-material")
@Api(tags = "MtMaterial")
public class MtMaterialController extends BaseController {

    @Autowired
    private MtMaterialRepository repository;
    @Autowired
    private MtMaterialService mtMaterialService;

    @ApiOperation(value = "materialPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialVO> materialPropertyGet(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody String materialId) {
        ResponseData<MtMaterialVO> responseData = new ResponseData<MtMaterialVO>();
        try {
            responseData.setRows(repository.materialPropertyGet(tenantId, materialId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "nameLimitMaterialQuery")
    @PostMapping(value = {"/limit-name/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> nameLimitMaterialQuery(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtMaterialDTO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtMaterial param = new MtMaterial();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.nameLimitMaterialQuery(tenantId, param));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "uomLimitMaterialQuery")
    @PostMapping(value = {"/limit-uom"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> uomLimitMaterialQuery(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtMaterialDTO2 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtMaterialVO param = new MtMaterialVO();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.uomLimitMaterialQuery(tenantId, param));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitMaterialQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitMaterialQuery(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtMaterialDTO3 dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtMaterialVO param = new MtMaterialVO();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.propertyLimitMaterialQuery(tenantId, param));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialDualUomValidate")
    @PostMapping(value = "/dual-uom/validate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialDualUomValidate(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody String materialId) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.materialDualUomValidate(tenantId, materialId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialPropertyBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialVO>> materialPropertyBatchGet(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody List<String> materialIds) {

        ResponseData<List<MtMaterialVO>> responseData = new ResponseData<List<MtMaterialVO>>();
        try {
            responseData.setRows(repository.materialPropertyBatchGet(tenantId, materialIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialUomGet")
    @PostMapping(value = "/material-uom", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialVO1> materialUomGet(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody String materialId) {

        ResponseData<MtMaterialVO1> responseData = new ResponseData<MtMaterialVO1>();
        try {
            responseData.setRows(repository.materialUomGet(tenantId, materialId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialUomBatchGet")
    @PostMapping(value = "/uom/batch/get", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialVO1>> materialUomBatchGet(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody List<String> materialIds) {
        ResponseData<List<MtMaterialVO1>> responseData = new ResponseData<>();
        try {
            responseData.setRows(repository.materialUomBatchGet(tenantId, materialIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "materialUomTypeValidate")
    @PostMapping(value = {"/type/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialUomTypeValidate(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody MtMaterialDTO4 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtMaterialVO2 param = new MtMaterialVO2();
            BeanUtils.copyProperties(dto, param);
            repository.materialUomTypeValidate(tenantId, param);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "物料查询(前台)")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtMaterialVO>> materialListForUi(
            @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
            MtMaterialDTO6 dto, @ApiIgnore @SortDefault(value = MtMaterial.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtMaterialVO>> result = new ResponseData<Page<MtMaterialVO>>();
        try {
            result.setRows(mtMaterialService.materialListUi(tenantId, dto, pageRequest));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "获取物料基础属性(前台)")
    @GetMapping(value = "/property/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialVO> materialPropertyGetForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @ApiParam(value = "物料ID", required = true) @RequestParam(name = "materialId") String materialId) {
        ResponseData<MtMaterialVO> responseData = new ResponseData<MtMaterialVO>();
        try {
            responseData.setRows(mtMaterialService.materialPropertyGetForUi(tenantId, materialId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("物料信息保存(前台)")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable(value = "organizationId") Long tenantId,
                    @ApiParam(value = "物料对象", required = true) @RequestBody MtMaterialDTO5 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(mtMaterialService.materialSaveForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "物料校验(前台)")
    @GetMapping(value = {"/check/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterial> materialCheckForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @ApiParam(value = "物料代码") @RequestParam String materialCode) {
        ResponseData<MtMaterial> result = new ResponseData<MtMaterial>();
        try {
            result.setRows(mtMaterialService.materialCheckForUi(tenantId, materialCode));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "propertyLimitMaterialPropertyQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialVO5>> propertyLimitMaterialPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtMaterialVO4 dto) {
        ResponseData<List<MtMaterialVO5>> result = new ResponseData<List<MtMaterialVO5>>();
        try {
            result.setRows(repository.propertyLimitMaterialPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialAttrPropertyUpdate")
    @PostMapping(value = {"/material/attr/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.materialAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
