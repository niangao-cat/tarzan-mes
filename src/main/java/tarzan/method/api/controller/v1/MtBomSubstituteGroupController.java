package tarzan.method.api.controller.v1;

import java.util.List;
import java.util.Map;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.method.api.dto.MtBomSubstituteGroupDTO;
import tarzan.method.app.service.MtBomSubstituteGroupService;
import tarzan.method.domain.entity.MtBomSubstituteGroup;
import tarzan.method.domain.repository.MtBomSubstituteGroupRepository;
import tarzan.method.domain.vo.*;

/**
 * 装配清单行替代组 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@RestController("mtBomSubstituteGroupController.v1")
@RequestMapping("/v1/{organizationId}/mt-bom-substitute-group")
@Api(tags = "MtBomSubstituteGroup")
public class MtBomSubstituteGroupController extends BaseController {

    @Autowired
    private MtBomSubstituteGroupService service;

    @Autowired
    private MtBomSubstituteGroupRepository repository;

    @ApiOperation(value = "获取装配清单组件行的替代组列表（前台）")
    @GetMapping(value = {"/list/by/component/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteGroupVO5>> listbomSubstituteGroupUi(
                    @PathVariable("organizationId") Long tenantId,
                    @ApiParam("组件行主键") @RequestParam("bomComponentId") String bomComponentId) {
        ResponseData<List<MtBomSubstituteGroupVO5>> responseData = new ResponseData<List<MtBomSubstituteGroupVO5>>();
        try {
            responseData.setRows(this.service.listbomSubstituteGroupUi(tenantId, bomComponentId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "保存装配清单组件行的替代组（前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveBomSubstituteGroupForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomSubstituteGroupVO5 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(this.service.saveBomSubstituteGroupForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomSubstituteGroupBasicGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtBomSubstituteGroup> bomSubstituteGroupBasicGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomSubstituteGroupId) {
        ResponseData<MtBomSubstituteGroup> responseData = new ResponseData<MtBomSubstituteGroup>();
        try {
            responseData.setRows(this.repository.bomSubstituteGroupBasicGet(tenantId, bomSubstituteGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomSubstituteQuery")
    @PostMapping(value = {"/substitute"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteGroupVO3>> bomSubstituteQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomComponentId) {
        ResponseData<List<MtBomSubstituteGroupVO3>> responseData = new ResponseData<List<MtBomSubstituteGroupVO3>>();
        try {
            responseData.setRows(this.repository.bomSubstituteQuery(tenantId, bomComponentId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "componentLimitBomSubstituteGroupQuery")
    @PostMapping(value = {"/limit-component"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<Map<String, String>>> componentLimitBomSubstituteGroupQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String bomComponentId) {
        ResponseData<List<Map<String, String>>> responseData = new ResponseData<List<Map<String, String>>>();
        try {
            responseData.setRows(this.repository.componentLimitBomSubstituteGroupQuery(tenantId, bomComponentId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitBomSubstituteGroupQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteGroupVO2>> propertyLimitBomSubstituteGroupQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtBomSubstituteGroupVO condition) {
        ResponseData<List<MtBomSubstituteGroupVO2>> responseData = new ResponseData<List<MtBomSubstituteGroupVO2>>();
        try {
            responseData.setRows(this.repository.propertyLimitBomSubstituteGroupQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomSubstituteGroupBasicBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteGroup>> bomSubstituteGroupBasicBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> bomSubstituteGroupIds) {
        ResponseData<List<MtBomSubstituteGroup>> responseData = new ResponseData<List<MtBomSubstituteGroup>>();
        try {
            responseData.setRows(this.repository.bomSubstituteGroupBasicBatchGet(tenantId, bomSubstituteGroupIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomSubstituteGroupUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> bomSubstituteGroupUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomSubstituteGroupDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {

            MtBomSubstituteGroup dto1 = new MtBomSubstituteGroup();
            BeanUtils.copyProperties(dto, dto1);

            responseData.setRows(this.repository.bomSubstituteGroupUpdate(tenantId, dto1));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomSubstituteBatchQuery")
    @PostMapping(value = {"/batch-substitute"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteGroupVO7>> bomSubstituteBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> bomComponentIds) {
        ResponseData<List<MtBomSubstituteGroupVO7>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.bomSubstituteBatchQuery(tenantId, bomComponentIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
