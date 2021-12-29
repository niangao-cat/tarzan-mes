package tarzan.method.api.controller.v1;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.method.api.dto.MtAssemblePointDTO;
import tarzan.method.api.dto.MtAssemblePointDTO2;
import tarzan.method.api.dto.MtAssemblePointDTO3;
import tarzan.method.api.dto.MtAssemblePointDTO4;
import tarzan.method.api.dto.MtAssemblePointDTO5;
import tarzan.method.api.dto.MtAssemblePointDTO6;
import tarzan.method.api.dto.MtAssemblePointDTO7;
import tarzan.method.domain.entity.MtAssemblePoint;
import tarzan.method.domain.repository.MtAssemblePointRepository;
import tarzan.method.domain.vo.MtAssemblePointVO;
import tarzan.method.domain.vo.MtAssemblePointVO1;
import tarzan.method.domain.vo.MtAssemblePointVO10;
import tarzan.method.domain.vo.MtAssemblePointVO2;
import tarzan.method.domain.vo.MtAssemblePointVO3;
import tarzan.method.domain.vo.MtAssemblePointVO4;
import tarzan.method.domain.vo.MtAssemblePointVO5;
import tarzan.method.domain.vo.MtAssemblePointVO6;
import tarzan.method.domain.vo.MtAssemblePointVO7;
import tarzan.method.domain.vo.MtAssemblePointVO9;

/**
 * 装配点，标识具体装配组下具体的装配位置 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@RestController("mtAssemblePointController.v1")
@RequestMapping("/v1/{organizationId}/mt-assemble-point")
@Api(tags = "MtAssemblePoint")
public class MtAssemblePointController extends BaseController {

    @Autowired
    private MtAssemblePointRepository mtAssemblePointRepository;


    @ApiOperation(value = "propertyLimitAssemblePointQuery")
    @PostMapping(value = "/limit-property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitAssemblePointQuery(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtAssemblePointDTO5 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
            BeanUtils.copyProperties(dto, mtAssemblePoint);
            responseData.setRows(mtAssemblePointRepository.propertyLimitAssemblePointQuery(tenantId, mtAssemblePoint));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assemblePointPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssemblePoint> assemblePointPropertyGet(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody String assemblePointId) {
        ResponseData<MtAssemblePoint> responseData = new ResponseData<MtAssemblePoint>();
        try {
            responseData.setRows(this.mtAssemblePointRepository.assemblePointPropertyGet(tenantId, assemblePointId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleGroupLimitEnableAssemblePointQuery")
    @PostMapping(value = "/limit-group/available/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<Map<String, String>>> assembleGroupLimitEnableAssemblePointQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String assembleGroupId) {
        ResponseData<List<Map<String, String>>> responseData = new ResponseData<List<Map<String, String>>>();
        try {
            responseData.setRows(this.mtAssemblePointRepository.assembleGroupLimitEnableAssemblePointQuery(tenantId,
                            assembleGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointIsEnabledValidate")
    @PostMapping(value = "/available/verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assemblePointIsEnabledValidate(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody String assemblePointId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtAssemblePointRepository.assemblePointIsEnabledValidate(tenantId, assemblePointId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> assemblePointUpdate(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody MtAssemblePointDTO5 dto,
                                                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
            BeanUtils.copyProperties(dto, mtAssemblePoint);
            responseData.setRows(
                            this.mtAssemblePointRepository.assemblePointUpdate(tenantId, mtAssemblePoint, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointCodeGenerate")
    @PostMapping(value = "/code/generate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> assemblePointCodeGenerate(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody String assembleGroupId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtAssemblePointRepository.assemblePointCodeGenerate(tenantId, assembleGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointNextSequenceGenerate")
    @PostMapping(value = "/sequence/generate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Long> assemblePointNextSequenceGenerate(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody String assembleGroupId) {
        ResponseData<Long> responseData = new ResponseData<Long>();
        try {
            responseData.setRows(this.mtAssemblePointRepository.assemblePointNextSequenceGenerate(tenantId,
                            assembleGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointAutoCreate")
    @PostMapping(value = "/auto/create", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> assemblePointAutoCreate(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody String assembleGroupId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtAssemblePointRepository.assemblePointAutoCreate(tenantId, assembleGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assembleGroupLimitAssemblePointOrderBySequenceSort")
    @PostMapping(value = "/sequence/sorted", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssemblePointVO>> assembleGroupLimitAssemblePointOrderBySequenceSort(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssemblePointDTO6 dto) {
        ResponseData<List<MtAssemblePointVO>> responseData = new ResponseData<List<MtAssemblePointVO>>();
        try {
            responseData.setRows(this.mtAssemblePointRepository.assembleGroupLimitAssemblePointOrderBySequenceSort(
                            tenantId, dto.getAssembleGroupId(), dto.getSortBy()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointPropertyBatchGet")
    @PostMapping(value = "/property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssemblePoint>> assemblePointPropertyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> assemblePointIds) {
        ResponseData<List<MtAssemblePoint>> responseData = new ResponseData<List<MtAssemblePoint>>();
        try {
            responseData.setRows(
                            this.mtAssemblePointRepository.assemblePointPropertyBatchGet(tenantId, assemblePointIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointMaxQtyVerify")
    @PostMapping(value = "/max-qty/verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assemblePointMaxQtyVerify(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtAssemblePointVO1 condition) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtAssemblePointRepository.assemblePointMaxQtyVerify(tenantId, condition);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointMaterialLoadVerify")
    @PostMapping(value = "/material/load/verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assemblePointMaterialLoadVerify(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtAssemblePointVO2 condition) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtAssemblePointRepository.assemblePointMaterialLoadVerify(tenantId, condition);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointMaterialSequenceLoad")
    @PostMapping(value = "/material/load/sequence", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assemblePointMaterialSequenceLoad(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody MtAssemblePointDTO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {

            MtAssemblePointVO3 condition = new MtAssemblePointVO3();
            BeanUtils.copyProperties(dto, condition);

            this.mtAssemblePointRepository.assemblePointMaterialSequenceLoad(tenantId, condition);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointMaterialConsume")
    @PostMapping(value = "/material/consumption", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assemblePointMaterialConsume(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtAssemblePointDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {

            MtAssemblePointVO4 condition = new MtAssemblePointVO4();
            BeanUtils.copyProperties(dto, condition);

            this.mtAssemblePointRepository.assemblePointMaterialConsume(tenantId, condition);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointMaterialLoad")
    @PostMapping(value = "/material/load", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assemblePointMaterialLoad(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtAssemblePointDTO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {

            MtAssemblePointVO5 dto1 = new MtAssemblePointVO5();
            BeanUtils.copyProperties(dto, dto1);

            this.mtAssemblePointRepository.assemblePointMaterialLoad(tenantId, dto1);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointMaterialUnload")
    @PostMapping(value = "/material/unload", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assemblePointMaterialUnload(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody MtAssemblePointDTO4 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {

            MtAssemblePointVO6 dto1 = new MtAssemblePointVO6();
            BeanUtils.copyProperties(dto, dto1);

            this.mtAssemblePointRepository.assemblePointMaterialUnload(tenantId, dto1);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("firstAvailableLoadingAssemblePointGet")
    @PostMapping(value = "/limit-available-loading-material/first-point", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> firstAvailableLoadingAssemblePointGet(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtAssemblePointDTO7 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            String assemblePointId = this.mtAssemblePointRepository.firstAvailableLoadingAssemblePointGet(tenantId,
                            dto.getAssembleGroupId(), dto.getMaterialId());
            if (StringUtils.isNotEmpty(assemblePointId)) {
                responseData.setRows(assemblePointId);
            }
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("assemblePointMaterialSequenceConsume")
    @PostMapping(value = "/material/sequence/consumption", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assemblePointMaterialSequenceConsume(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody MtAssemblePointVO7 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtAssemblePointRepository.assemblePointMaterialSequenceConsume(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitAssemblePointPropertyQuery")
    @PostMapping(value = "/point/property/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssemblePointVO10>> propertyLimitAssemblePointPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssemblePointVO9 dto) {
        ResponseData<List<MtAssemblePointVO10>> responseData = new ResponseData<List<MtAssemblePointVO10>>();
        try {
            responseData.setRows(this.mtAssemblePointRepository.propertyLimitAssemblePointPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
