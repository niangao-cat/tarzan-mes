package tarzan.method.api.controller.v1;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.method.api.dto.MtBomComponentDTO;
import tarzan.method.app.service.MtBomComponentService;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.vo.*;

/**
 * 装配清单行 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@RestController("mtBomComponentController.v1")
@RequestMapping("/v1/{organizationId}/mt-bom-component")
@Api(tags = "MtBomComponent")
public class MtBomComponentController extends BaseController {

    @Autowired
    private MtBomComponentService service;

    @Autowired
    private MtBomComponentRepository repository;


    @ApiOperation("获取装配清单组件行（前台）")
    @GetMapping(value = "/one/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtBomComponentVO4> bomComponentRecordForUi(@PathVariable("organizationId") Long tenantId,
                    @ApiParam("主键") @RequestParam("bomComponentId") String bomComponentId) {
        ResponseData<MtBomComponentVO4> responseData = new ResponseData<MtBomComponentVO4>();
        try {
            responseData.setRows(this.service.bomComponentRecordForUi(tenantId, bomComponentId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("新增&更新装配清单组件行（前台）")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveBomComponentForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomComponentVO4 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(this.service.saveBomComponentForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("bomComponentBasicGet")
    @PostMapping(value = "/basic", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtBomComponentVO8> bomComponentBasicGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomComponentId) {
        ResponseData<MtBomComponentVO8> responseData = new ResponseData<MtBomComponentVO8>();
        try {
            responseData.setRows(this.repository.bomComponentBasicGet(tenantId, bomComponentId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitBomComponentQuery")
    @PostMapping(value = "/limit-property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomComponentVO16>> propertyLimitBomComponentQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtBomComponentVO condition) {
        ResponseData<List<MtBomComponentVO16>> responseData = new ResponseData<List<MtBomComponentVO16>>();
        try {
            responseData.setRows(this.repository.propertyLimitBomComponentQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("attritionLimitComponentQtyCalculate")
    @PostMapping(value = "/component/qty/calculate/limit-attrition", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> attritionLimitComponentQtyCalculate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomComponentVO6 dto) {
        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.repository.attritionLimitComponentQtyCalculate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("materialLimitBomQuery")
    @PostMapping(value = "/limit-material", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> materialLimitBomQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomComponentDTO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.materialLimitBomQuery(tenantId, dto.getSiteId(), dto.getMaterialId(),
                            dto.getBomComponentType()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("bomComponentQtyCalculate")
    @PostMapping(value = "/material-qty/calculate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomComponentVO2>> bomComponentQtyCalculate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomComponentVO5 dto) {
        ResponseData<List<MtBomComponentVO2>> responseData = new ResponseData<List<MtBomComponentVO2>>();
        try {
            responseData.setRows(this.repository.bomComponentQtyCalculate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("bomComponentBasicBatchGet")
    @PostMapping(value = "/basic/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomComponentVO13>> bomComponentBasicBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> bomComponentIds) {
        ResponseData<List<MtBomComponentVO13>> responseData = new ResponseData<List<MtBomComponentVO13>>();
        try {
            responseData.setRows(this.repository.bomComponentBasicBatchGet(tenantId, bomComponentIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("bomComponentUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtBomComponentVO15> bomComponentUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomComponentVO10 dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<MtBomComponentVO15> responseData = new ResponseData<MtBomComponentVO15>();
        try {
            responseData.setRows(this.repository.bomComponentUpdate(tenantId, dto, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("singleBomComponentQtyCalculate")
    @PostMapping(value = "/single/component-qty/calculate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomComponentVO12>> singleBomComponentQtyCalculate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtBomComponentVO11 dto) {
        ResponseData<List<MtBomComponentVO12>> responseData = new ResponseData<List<MtBomComponentVO12>>();
        try {
            List<MtBomComponentVO12> v12s = this.repository.singleBomComponentQtyCalculate(tenantId, dto);
            if (CollectionUtils.isNotEmpty(v12s)) {
                responseData.setRows(v12s);
            }
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("bomComponentQtyBatchCalculate")
    @PostMapping(value = "/material-qty/batch-calculate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomComponentVO25>> bomComponentQtyBatchCalculate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtBomComponentVO22 dto) {
        ResponseData<List<MtBomComponentVO25>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.bomComponentQtyBatchCalculate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("attritionLimitComponentQtyBatchCalculate")
    @PostMapping(value = "/component/qty/batch-calculate/limit-attrition", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomComponentVO28>> attritionLimitComponentQtyBatchCalculate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtBomComponentVO6 dto) {
        ResponseData<List<MtBomComponentVO28>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.attritionLimitComponentQtyBatchCalculate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
