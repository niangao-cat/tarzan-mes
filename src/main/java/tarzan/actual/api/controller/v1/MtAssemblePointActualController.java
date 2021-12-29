package tarzan.actual.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
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
import tarzan.actual.domain.repository.MtAssemblePointActualRepository;
import tarzan.actual.domain.vo.MtAssemblePointActualVO;
import tarzan.actual.domain.vo.MtAssemblePointActualVO1;
import tarzan.actual.domain.vo.MtAssemblePointActualVO2;
import tarzan.actual.domain.vo.MtAssemblePointActualVO3;
import tarzan.actual.domain.vo.MtAssemblePointActualVO4;
import tarzan.actual.domain.vo.MtAssemblePointActualVO5;
import tarzan.actual.domain.vo.MtAssemblePointActualVO6;
import tarzan.actual.domain.vo.MtAssemblePointActualVO7;
import tarzan.actual.domain.vo.MtAssemblePointActualVO8;
import tarzan.actual.domain.vo.MtAssemblePointActualVO9;

/**
 * 装配点实绩，记录装配组下装配点实际装配信息 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtAssemblePointActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-assemble-point-actual")
@Api(tags = "MtAssemblePointActual")
public class MtAssemblePointActualController extends BaseController {

    @Autowired
    private MtAssemblePointActualRepository repository;

    @ApiOperation(value = "propertyLimitAssemblePointActualQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitAssemblePointActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssemblePointActualVO1 condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitAssemblePointActualQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assemblePointActualPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssemblePointActualVO1> assemblePointActualPropertyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String assemblePointActualId) {
        ResponseData<MtAssemblePointActualVO1> responseData = new ResponseData<MtAssemblePointActualVO1>();
        try {
            responseData.setRows(this.repository.assemblePointActualPropertyGet(tenantId, assemblePointActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assemblePointActualPropertyBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssemblePointActualVO1>> assemblePointActualPropertyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> assemblePointActualIds) {
        ResponseData<List<MtAssemblePointActualVO1>> responseData = new ResponseData<List<MtAssemblePointActualVO1>>();
        try {
            responseData.setRows(this.repository.assemblePointActualPropertyBatchGet(tenantId, assemblePointActualIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleControlLimitAssemblePointQuery")
    @PostMapping(value = {"/limit-control/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> assembleControlLimitAssemblePointQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssemblePointActualVO condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.assembleControlLimitAssemblePointQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assemblePointFeedingNextSequenceGet")
    @PostMapping(value = {"/feeding-sequence"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Long> assemblePointFeedingNextSequenceGet(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody MtAssemblePointActualVO6 condition) {
        ResponseData<Long> responseData = new ResponseData<Long>();
        try {
            responseData.setRows(this.repository.assemblePointFeedingNextSequenceGet(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assemblePointLoadingMaterialQtyGet")
    @PostMapping(value = {"/loading-material-qty"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> assemblePointLoadingMaterialQtyGet(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody MtAssemblePointActualVO2 condition) {
        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.repository.assemblePointLoadingMaterialQtyGet(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "feedingLotNextSequenceGet")
    @PostMapping(value = {"/material-lot/feeding-sequence"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Long> feedingLotNextSequenceGet(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody String assemblePointId) {
        ResponseData<Long> responseData = new ResponseData<Long>();
        try {
            responseData.setRows(this.repository.feedingLotNextSequenceGet(tenantId, assemblePointId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialAssemblePointControlVerify")
    @PostMapping(value = {"/material/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialAssemblePointControlVerify(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtAssemblePointActualVO3 condition) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialAssemblePointControlVerify(tenantId, condition);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assemblePointHaveMaterialVerify")
    @PostMapping(value = {"/material-lot/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assemblePointHaveMaterialVerify(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtAssemblePointActualVO5 condition) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.assemblePointHaveMaterialVerify(tenantId, condition);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assemblePointActualDelete")
    @PostMapping(value = {"/remove"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assemblePointActualDelete(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody String assemblePointActualId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.assemblePointActualDelete(tenantId, assemblePointActualId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assemblePointActualUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssemblePointActualVO7> assemblePointActualUpdate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssemblePointActualVO4 dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<MtAssemblePointActualVO7> responseData = new ResponseData<MtAssemblePointActualVO7>();
        try {
            responseData.setRows(this.repository.assemblePointActualUpdate(tenantId, dto, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitAssemblePointActualPropertyQuery")
    @PostMapping(value = {"/actual/property/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssemblePointActualVO9>> propertyLimitAssemblePointActualPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssemblePointActualVO8 dto) {
        ResponseData<List<MtAssemblePointActualVO9>> responseData = new ResponseData<List<MtAssemblePointActualVO9>>();
        try {
            responseData.setRows(this.repository.propertyLimitAssemblePointActualPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
