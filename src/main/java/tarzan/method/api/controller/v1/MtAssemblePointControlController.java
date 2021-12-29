package tarzan.method.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.method.api.dto.MtAssemblePointControlDTO;
import tarzan.method.api.dto.MtAssemblePointControlDTO2;
import tarzan.method.domain.entity.MtAssemblePointControl;
import tarzan.method.domain.repository.MtAssemblePointControlRepository;
import tarzan.method.domain.vo.MtAssemblePointControlVO;
import tarzan.method.domain.vo.MtAssemblePointControlVO1;
import tarzan.method.domain.vo.MtAssemblePointControlVO2;
import tarzan.method.domain.vo.MtAssemblePointControlVO3;
import tarzan.method.domain.vo.MtAssemblePointControlVO4;
import tarzan.method.domain.vo.MtAssemblePointControlVO5;
import tarzan.method.domain.vo.MtAssemblePointControlVO6;
import tarzan.method.domain.vo.MtAssemblePointControlVO7;

/**
 * 装配点控制，指示具体装配控制下装配点可装载的物料 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@RestController("mtAssemblePointControlController.v1")
@RequestMapping("/v1/{organizationId}/mt-assemble-point-control")
@Api(tags = "MtAssemblePointControl")
public class MtAssemblePointControlController extends BaseController {

    @Autowired
    private MtAssemblePointControlRepository mtAssemblePointControlRepository;

    @ApiOperation(value = "assemblePointLimitAssembleControlPropertyGet")
    @PostMapping(value = "/point-limit/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssemblePointControl> assemblePointLimitAssembleControlPropertyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssemblePointControlVO dto) {
        ResponseData<MtAssemblePointControl> responseData = new ResponseData<MtAssemblePointControl>();
        try {
            responseData.setRows(this.mtAssemblePointControlRepository
                            .assemblePointLimitAssembleControlPropertyGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assemblePointMaterialUnitQtyGet")
    @PostMapping(value = "/unit-qty", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> assemblePointMaterialUnitQtyGet(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody MtAssemblePointControlVO dto) {
        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.mtAssemblePointControlRepository.assemblePointMaterialUnitQtyGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLimitAvailableAssemblePointQuery")
    @PostMapping(value = "/limit-material/available/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> materialLimitAvailableAssemblePointQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssemblePointControlVO1 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.mtAssemblePointControlRepository
                            .materialLimitAvailableAssemblePointQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assemblePointLimitAssembleControlPropertyBatchGet")
    @PostMapping(value = "/limit-property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssemblePointControl>> assemblePointLimitAssembleControlPropertyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssemblePointControlVO2 dto) {
        ResponseData<List<MtAssemblePointControl>> responseData = new ResponseData<List<MtAssemblePointControl>>();
        try {
            responseData.setRows(this.mtAssemblePointControlRepository
                            .assemblePointLimitAssembleControlPropertyBatchGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assemblePointControlUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> assemblePointControlUpdate(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtAssemblePointControlDTO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtAssemblePointControl mtAssemblePointControl = new MtAssemblePointControl();
            BeanUtils.copyProperties(dto, mtAssemblePointControl);
            responseData.setRows(this.mtAssemblePointControlRepository.assemblePointControlUpdate(tenantId,
                            mtAssemblePointControl));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLimitAvailableAssemblePointValidate")
    @PostMapping(value = "/limit-material/validate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLimitAvailableAssemblePointValidate(@PathVariable("organizationId") Long tenantId,
                                                                          @RequestBody MtAssemblePointControlDTO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {

            MtAssemblePointControlVO3 condition = new MtAssemblePointControlVO3();
            BeanUtils.copyProperties(dto, condition);

            this.mtAssemblePointControlRepository.materialLimitAvailableAssemblePointValidate(tenantId, condition);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleGroupLimitAssembleControlPropertyQuery")
    @PostMapping(value = "/limit-group/property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssemblePointControlVO4>> assembleGroupLimitAssembleControlPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssemblePointControlVO5 condition) {
        ResponseData<List<MtAssemblePointControlVO4>> responseData =
                        new ResponseData<List<MtAssemblePointControlVO4>>();
        try {
            responseData.setRows(this.mtAssemblePointControlRepository
                            .assembleGroupLimitAssembleControlPropertyQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitAssemblePointControlPropertyQuery")
    @PostMapping(value = "/control/property/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssemblePointControlVO7>> propertyLimitAssemblePointControlPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssemblePointControlVO6 dto) {
        ResponseData<List<MtAssemblePointControlVO7>> responseData =
                        new ResponseData<List<MtAssemblePointControlVO7>>();
        try {
            responseData.setRows(this.mtAssemblePointControlRepository
                            .propertyLimitAssemblePointControlPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
