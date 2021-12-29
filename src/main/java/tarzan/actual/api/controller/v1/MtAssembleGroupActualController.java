package tarzan.actual.api.controller.v1;

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
import tarzan.actual.api.dto.MtAssembleGroupActualDTO;
import tarzan.actual.domain.entity.MtAssembleGroupActual;
import tarzan.actual.domain.repository.MtAssembleGroupActualRepository;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO1;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO2;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO3;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO4;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO5;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO6;

/**
 * 装配组实绩，记录装配组安装的位置 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtAssembleGroupActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-assemble-group-actual")
@Api(tags = "MtAssembleGroupActual")
public class MtAssembleGroupActualController extends BaseController {

    @Autowired
    private MtAssembleGroupActualRepository repository;

    @ApiOperation(value = "assembleGroupActualPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssembleGroupActual> assembleGroupActualPropertyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String assembleGroupActualId) {
        ResponseData<MtAssembleGroupActual> responseData = new ResponseData<MtAssembleGroupActual>();
        try {
            responseData.setRows(this.repository.assembleGroupActualPropertyGet(tenantId, assembleGroupActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleGroupActualPropertyBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleGroupActual>> assembleGroupActualPropertyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> assembleGroupActualIds) {
        ResponseData<List<MtAssembleGroupActual>> responseData = new ResponseData<List<MtAssembleGroupActual>>();
        try {
            responseData.setRows(this.repository.assembleGroupActualPropertyBatchGet(tenantId, assembleGroupActualIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleControlLimitAssembleGroupQuery")
    @PostMapping(value = {"/limit-control/assemble-group/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> assembleControlLimitAssembleGroupQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleGroupActualVO condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.assembleControlLimitAssembleGroupQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitAssembleGroupActualQuery")
    @PostMapping(value = {"/limit-wkc-group/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitAssembleGroupActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleGroupActualVO1 condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitAssembleGroupActualQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcLimitCurrentAssembleGroupQuery")
    @PostMapping(value = {"/limit-wkc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleGroupActualVO2>> wkcLimitCurrentAssembleGroupQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String workcellId) {
        ResponseData<List<MtAssembleGroupActualVO2>> responseData = new ResponseData<List<MtAssembleGroupActualVO2>>();
        try {
            responseData.setRows(this.repository.wkcLimitCurrentAssembleGroupQuery(tenantId, workcellId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "firstEmptyAssemblePointGet")
    @PostMapping(value = {"/first-empty/assemble-point"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> firstEmptyAssemblePointGet(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody String assembleGroupId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.firstEmptyAssemblePointGet(tenantId, assembleGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "firstAvailableFeedingAssemblePointGet")
    @PostMapping(value = {"/first-available-feeding/assemble-point"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> firstAvailableFeedingAssemblePointGet(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtAssembleGroupActualVO3 condition) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.firstAvailableFeedingAssemblePointGet(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcAssembleGroupControlVerify")
    @PostMapping(value = {"/wkc/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> wkcAssembleGroupControlVerify(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtAssembleGroupActualVO4 condition) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.wkcAssembleGroupControlVerify(tenantId, condition);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleGroupActualUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssembleGroupActualVO6> assembleGroupActualUpdate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleGroupActualDTO dto) {
        ResponseData<MtAssembleGroupActualVO6> responseData = new ResponseData<MtAssembleGroupActualVO6>();
        try {
            MtAssembleGroupActualVO5 param = new MtAssembleGroupActualVO5();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.assembleGroupActualUpdate(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleGroupActualDelete")
    @PostMapping(value = {"/remove"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assembleGroupActualDelete(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody String assembleGroupActualId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.assembleGroupActualDelete(tenantId, assembleGroupActualId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
