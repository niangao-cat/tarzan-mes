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
import tarzan.method.api.dto.MtBomSubstituteDTO;
import tarzan.method.app.service.MtBomSubstituteService;
import tarzan.method.domain.entity.MtBomSubstitute;
import tarzan.method.domain.repository.MtBomSubstituteRepository;
import tarzan.method.domain.vo.*;

/**
 * 装配清单行替代项 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@RestController("mtBomSubstituteController.v1")
@RequestMapping("/v1/{organizationId}/mt-bom-substitute")
@Api(tags = "MtBomSubstitute")
public class MtBomSubstituteController extends BaseController {

    @Autowired
    private MtBomSubstituteService service;

    @Autowired
    private MtBomSubstituteRepository repository;

    @ApiOperation(value = "获取装配清单组件行的替代项列表（前台）")
    @GetMapping(value = {"/list/by/group/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteVO9>> listbomSubstituteByGroupForUi(
                    @PathVariable("organizationId") Long tenantId,
                    @ApiParam("替代组主键") @RequestParam("bomSubstituteGroupId") String bomSubstituteGroupId) {
        ResponseData<List<MtBomSubstituteVO9>> responseData = new ResponseData<List<MtBomSubstituteVO9>>();
        try {
            responseData.setRows(this.service.bomSubstituteRecordForUi(tenantId, bomSubstituteGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "保存装配清单组件行的替代项（前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveBomSubstituteForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomSubstituteVO9 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(this.service.saveBomSubstituteForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomSubstituteBasicGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtBomSubstitute> bomSubstituteBasicGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomSubstituteId) {
        ResponseData<MtBomSubstitute> responseData = new ResponseData<MtBomSubstitute>();
        try {
            responseData.setRows(repository.bomSubstituteBasicGet(tenantId, bomSubstituteId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "groupLimitBomSubstituteQuery")
    @PostMapping(value = {"/limit-group"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<Map<String, String>>> groupLimitBomSubstituteQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String bomSubstituteGroupId) {
        ResponseData<List<Map<String, String>>> responseData = new ResponseData<List<Map<String, String>>>();
        try {
            responseData.setRows(repository.groupLimitBomSubstituteQuery(tenantId, bomSubstituteGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitBomSubstituteQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteVO2>> propertyLimitBomSubstituteQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtBomSubstituteVO condition) {
        ResponseData<List<MtBomSubstituteVO2>> responseData = new ResponseData<List<MtBomSubstituteVO2>>();
        try {
            responseData.setRows(repository.propertyLimitBomSubstituteQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomSubstituteQtyCalculate")
    @PostMapping(value = {"/qty/calculate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteVO3>> bomSubstituteQtyCalculate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtBomSubstituteVO6 dto) {
        ResponseData<List<MtBomSubstituteVO3>> responseData = new ResponseData<List<MtBomSubstituteVO3>>();
        try {
            responseData.setRows(repository.bomSubstituteQtyCalculate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomSubstituteBasicBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstitute>> bomSubstituteBasicBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> bomSubstituteIds) {
        ResponseData<List<MtBomSubstitute>> responseData = new ResponseData<List<MtBomSubstitute>>();
        try {
            responseData.setRows(repository.bomSubstituteBasicBatchGet(tenantId, bomSubstituteIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomComponentSubstituteQuery")
    @PostMapping(value = {"/enable"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteVO4>> bomComponentSubstituteQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String bomComponentId) {
        ResponseData<List<MtBomSubstituteVO4>> responseData = new ResponseData<List<MtBomSubstituteVO4>>();
        try {
            responseData.setRows(repository.bomComponentSubstituteQuery(tenantId, bomComponentId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomComponentSubstituteVerify")
    @PostMapping(value = {"/enable-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> bomComponentSubstituteVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomSubstituteVO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.bomComponentSubstituteVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomSubstituteUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtBomSubstituteVO10> bomSubstituteUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomSubstituteDTO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<MtBomSubstituteVO10> responseData = new ResponseData<MtBomSubstituteVO10>();
        try {

            MtBomSubstitute dto1 = new MtBomSubstitute();
            BeanUtils.copyProperties(dto, dto1);

            responseData.setRows(repository.bomSubstituteUpdate(tenantId, dto1, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomSubstituteLimitMaterialQtyBatchCalculate")
    @PostMapping(value = {"/limit/material/qty/batch/calculate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteVO12>> bomSubstituteLimitMaterialQtyBatchCalculate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<MtBomSubstituteVO11> dto) {
        ResponseData<List<MtBomSubstituteVO12>> responseData = new ResponseData<List<MtBomSubstituteVO12>>();
        try {

            MtBomSubstitute dto1 = new MtBomSubstitute();
            BeanUtils.copyProperties(dto, dto1);

            responseData.setRows(repository.bomSubstituteLimitMaterialQtyBatchCalculate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomSubstituteQtyBatchCalculate")
    @PostMapping(value = {"/qty/batch-calculate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteVO17>> bomSubstituteQtyBatchCalculate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtBomSubstituteVO15 dto) {
        ResponseData<List<MtBomSubstituteVO17>> responseData = new ResponseData<>();
        try {
            responseData.setRows(repository.bomSubstituteQtyBatchCalculate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
