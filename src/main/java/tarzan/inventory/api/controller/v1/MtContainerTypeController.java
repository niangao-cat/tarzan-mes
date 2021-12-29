package tarzan.inventory.api.controller.v1;

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
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.inventory.api.dto.MtContainerTypeDTO;
import tarzan.inventory.api.dto.MtContainerTypeDTO1;
import tarzan.inventory.api.dto.MtContainerTypeDTO2;
import tarzan.inventory.app.service.MtContainerTypeService;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.vo.MtContainerTypeAttrVO1;
import tarzan.inventory.domain.vo.MtContainerTypeAttrVO2;
import tarzan.inventory.domain.vo.MtContainerTypeAttrVO3;
import tarzan.inventory.domain.vo.MtContainerTypeVO;
import tarzan.inventory.domain.vo.MtContainerTypeVO1;

/**
 * 容器类型，定义一类容器和一类容器的控制属性，包括容器可装载的对象类型、最大容量、混装类型等 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@RestController("mtContainerTypeController.v1")
@RequestMapping("/v1/{organizationId}/mt-container-type")
@Api(tags = "MtContainerType")
public class MtContainerTypeController extends BaseController {

    @Autowired
    private MtContainerTypeRepository repository;

    @Autowired
    private MtContainerTypeService service;

    @ApiOperation(value = "UI查询容器类型列表")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtContainerTypeDTO1>> queryContainerTypeListForUi(
            @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
            MtContainerTypeDTO dto, @ApiIgnore @SortDefault(value = MtContainerType.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtContainerTypeDTO1>> responseData = new ResponseData<Page<MtContainerTypeDTO1>>();
        try {
            responseData.setRows(service.queryContainerTypeListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI查询容器类型详细信息")
    @GetMapping(value = {"/detail/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtContainerTypeDTO1> queryContainerTypeDetailForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @ApiParam(value = "容器类型ID",
                                    required = true) @RequestParam(name = "containerTypeId") String containerTypeId) {
        ResponseData<MtContainerTypeDTO1> responseData = new ResponseData<MtContainerTypeDTO1>();
        try {
            responseData.setRows(service.queryContainerTypeDetailForUi(tenantId, containerTypeId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存容器类型")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveContainerTypeForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @RequestBody MtContainerTypeDTO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(service.saveContainerTypeForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerTypePropertyGet")
    @PostMapping("/property/get")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtContainerType> containerTypePropertyGet(@PathVariable(value = "organizationId") Long tenantId,
                                                                  @RequestBody String containerTypeId) {

        ResponseData<MtContainerType> responseData = new ResponseData<MtContainerType>();
        try {
            responseData.setRows(this.repository.containerTypePropertyGet(tenantId, containerTypeId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerTypePropertyBatchGet")
    @PostMapping("/property/batch/get")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerType>> containerTypePropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody List<String> containerTypeIdList) {

        ResponseData<List<MtContainerType>> responseData = new ResponseData<List<MtContainerType>>();
        try {
            responseData.setRows(this.repository.containerTypePropertyBatchGet(tenantId, containerTypeIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitContainerTypeQuery")
    @PostMapping("/limit-property/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitContainerTypeQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContainerTypeDTO dto,
                    @RequestParam(name = "fuzzyQueryFlag", defaultValue = "N",
                                    required = false) String fuzzyQueryFlag) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtContainerType mtContainerType = new MtContainerType();
            BeanUtils.copyProperties(dto, mtContainerType);
            responseData.setRows(
                            this.repository.propertyLimitContainerTypeQuery(tenantId, mtContainerType, fuzzyQueryFlag));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerTypePropertyUpdate")
    @PostMapping("/save")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> containerTypePropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                            @RequestBody MtContainerTypeDTO dto,
                                                            @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtContainerType mtContainerType = new MtContainerType();
            BeanUtils.copyProperties(dto, mtContainerType);
            responseData.setRows(this.repository.containerTypePropertyUpdate(tenantId, mtContainerType, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerTypeEnableValidate")
    @PostMapping("/enable/validate")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerTypeEnableValidate(@PathVariable(value = "organizationId") Long tenantId,
                                                          @RequestBody String containerTypeId) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerTypeEnableValidate(tenantId, containerTypeId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerTypeLimitAttrQuery")
    @PostMapping("/limit-attr/property/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendAttrVO>> containerTypeLimitAttrQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContainerTypeAttrVO1 dto) {

        ResponseData<List<MtExtendAttrVO>> responseData = new ResponseData<List<MtExtendAttrVO>>();
        try {
            responseData.setRows(this.repository.containerTypeLimitAttrQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("attrLimitContainerTypeQuery")
    @PostMapping("/limit-attr/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> attrLimitContainerTypeQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                  @RequestBody MtContainerTypeAttrVO2 dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.attrLimitContainerTypeQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerTypeLimitAttrUpdate")
    @PostMapping("/limit-attr/save")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerTypeLimitAttrUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                           @RequestBody MtContainerTypeAttrVO3 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerTypeLimitAttrUpdate(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitContainerTypePropertyQuery")
    @PostMapping(value = {"/container/type/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerTypeVO1>> propertyLimitContainerTypePropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtContainerTypeVO dto) {
        ResponseData<List<MtContainerTypeVO1>> result = new ResponseData<List<MtContainerTypeVO1>>();
        try {
            result.setRows(repository.propertyLimitContainerTypePropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "containerTypeAttrPropertyUpdate")
    @PostMapping(value = {"/container/type/attr/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerTypeAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.containerTypeAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
