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
import tarzan.method.api.dto.MtAssembleGroupDTO;
import tarzan.method.domain.entity.MtAssembleGroup;
import tarzan.method.domain.repository.MtAssembleGroupRepository;
import tarzan.method.domain.vo.MtAssembleGroupVO1;
import tarzan.method.domain.vo.MtAssembleGroupVO2;
import tarzan.method.domain.vo.MtAssembleGroupVO3;
import tarzan.method.domain.vo.MtAssembleGroupVO4;
import tarzan.method.domain.vo.MtAssembleGroupVO5;

/**
 * 装配组，标识一个装载设备或一类装配关系 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@RestController("mtAssembleGroupController.v1")
@RequestMapping("/v1/{organizationId}/mt-assemble-group")
@Api(tags = "MtAssembleGroup")
public class MtAssembleGroupController extends BaseController {

    @Autowired
    private MtAssembleGroupRepository mtAssembleGroupRepository;

    @ApiOperation(value = "propertyLimitAssembleGroupQuery")
    @PostMapping(value = "/limit-property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitAssembleGroupQuery(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtAssembleGroupDTO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtAssembleGroup param = new MtAssembleGroup();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.mtAssembleGroupRepository.propertyLimitAssembleGroupQuery(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleGroupPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssembleGroup> assembleGroupPropertyGet(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody String assembleGroupId) {
        ResponseData<MtAssembleGroup> responseData = new ResponseData<MtAssembleGroup>();
        try {
            responseData.setRows(this.mtAssembleGroupRepository.assembleGroupPropertyGet(tenantId, assembleGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleGroupAvailableValidate")
    @PostMapping(value = "/available-verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assembleGroupAvailableValidate(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody String assembleGroupId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtAssembleGroupRepository.assembleGroupAvailableValidate(tenantId, assembleGroupId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleGroupUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> assembleGroupUpdate(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody MtAssembleGroupDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtAssembleGroup param = new MtAssembleGroup();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.mtAssembleGroupRepository.assembleGroupUpdate(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcAssembleGroupSetupCancel")
    @PostMapping(value = "/setup-cancel", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> wkcAssembleGroupSetupCancel(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody MtAssembleGroupVO1 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtAssembleGroupRepository.wkcAssembleGroupSetupCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcAssembleGroupSetup")
    @PostMapping(value = "/setup", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> wkcAssembleGroupSetup(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody MtAssembleGroupVO1 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtAssembleGroupRepository.wkcAssembleGroupSetup(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleGroupMaterialConsume")
    @PostMapping(value = "/material-consume", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assembleGroupMaterialConsume(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtAssembleGroupVO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtAssembleGroupRepository.assembleGroupMaterialConsume(tenantId, dto);

        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleGroupPropertyBatchGet")
    @PostMapping(value = "/property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleGroup>> assembleGroupPropertyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> assembleGroupIds) {
        ResponseData<List<MtAssembleGroup>> responseData = new ResponseData<List<MtAssembleGroup>>();
        try {
            responseData.setRows(
                            this.mtAssembleGroupRepository.assembleGroupPropertyBatchGet(tenantId, assembleGroupIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleGroupStatusUpdateVerify")
    @PostMapping(value = "/status-update/verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assembleGroupStatusUpdateVerify(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtAssembleGroupVO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtAssembleGroupRepository.assembleGroupStatusUpdateVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleGroupStatusUpdate")
    @PostMapping(value = "/status/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assembleGroupStatusUpdate(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtAssembleGroupVO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtAssembleGroupRepository.assembleGroupStatusUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitAssembleGroupPropertyQuery")
    @PostMapping(value = "/group/property/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleGroupVO5>> propertyLimitAssembleGroupPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleGroupVO4 dto) {
        ResponseData<List<MtAssembleGroupVO5>> responseData = new ResponseData<List<MtAssembleGroupVO5>>();
        try {
            responseData.setRows(this.mtAssembleGroupRepository.propertyLimitAssembleGroupPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
