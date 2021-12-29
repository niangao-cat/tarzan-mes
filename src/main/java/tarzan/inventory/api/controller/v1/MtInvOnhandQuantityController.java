package tarzan.inventory.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.inventory.api.dto.MtInvOnhandQuantityDTO;
import tarzan.inventory.app.service.MtInvOnhandQuantityService;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.vo.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 库存量 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
@RestController("mtInvOnhandQuantityController.v1")
@RequestMapping("/v1/{organizationId}/mt-inv-onhand-quantity")
@Api(tags = "MtInvOnhandQuantity")
public class MtInvOnhandQuantityController extends BaseController {

    @Autowired
    private MtInvOnhandQuantityService service;

    @ApiOperation(value = "UI获取库存信息")
    @PostMapping(value = "/property/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvOnhandQuantityVO4>> queryInventoryOnhandQuantityForUi(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtInvOnhandQuantityDTO dto,
                    @ApiIgnore PageRequest pageRequest) {
        ResponseData<List<MtInvOnhandQuantityVO4>> responseData = new ResponseData<List<MtInvOnhandQuantityVO4>>();
        try {
            responseData.setRows(this.service.queryInventoryOnhandQuantityForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @ApiOperation(value = "organizationSumOnhandQtyGet")
    @PostMapping(value = "/total", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> organizationSumOnhandQtyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInvOnhandQuantityVO7 condition) {
        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.mtInvOnhandQuantityRepository.organizationSumOnhandQtyGet(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "organizationDetailOnhandQtyQuery")
    @PostMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvOnhandQuantity>> organizationDetailOnhandQtyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtInvOnhandQuantityVO7 condition) {
        ResponseData<List<MtInvOnhandQuantity>> responseData = new ResponseData<List<MtInvOnhandQuantity>>();
        try {
            responseData.setRows(
                            this.mtInvOnhandQuantityRepository.organizationDetailOnhandQtyQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitDetailOnhandQtyQuery")
    @PostMapping(value = "/limit-property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvOnhandQuantity>> propertyLimitDetailOnhandQtyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtInvOnhandQuantityVO condition) {
        ResponseData<List<MtInvOnhandQuantity>> responseData = new ResponseData<List<MtInvOnhandQuantity>>();
        try {
            responseData.setRows(
                            this.mtInvOnhandQuantityRepository.propertyLimitDetailOnhandQtyQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitSumOnhandQtyGet")
    @PostMapping(value = "/limit-property/total", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> propertyLimitSumOnhandQtyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInvOnhandQuantityVO condition) {
        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.mtInvOnhandQuantityRepository.propertyLimitSumOnhandQtyGet(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitDetailOnhandQtyBatchQuery")
    @PostMapping(value = "/limit-property/detail/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvOnhandQuantity>> propertyLimitDetailOnhandQtyBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtInvOnhandQuantityVO1 condition) {
        ResponseData<List<MtInvOnhandQuantity>> responseData = new ResponseData<List<MtInvOnhandQuantity>>();
        try {
            responseData.setRows(this.mtInvOnhandQuantityRepository.propertyLimitDetailOnhandQtyBatchQuery(tenantId,
                            condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "onhandQtyUpdateVerify")
    @PostMapping(value = "/save/verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInvOnhandQuantityVO2> onhandQtyUpdateVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInvOnhandQuantityVO8 condition) {
        ResponseData<MtInvOnhandQuantityVO2> responseData = new ResponseData<MtInvOnhandQuantityVO2>();
        try {
            responseData.setRows(this.mtInvOnhandQuantityRepository.onhandQtyUpdateVerify(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "onhandQtyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandQtyUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInvOnhandQuantityVO9 condition) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtInvOnhandQuantityRepository.onhandQtyUpdate(tenantId, condition);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitSumAvailableOnhandQtyGet")
    @PostMapping(value = "/limit-property/available-total", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> propertyLimitSumAvailableOnhandQtyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInvOnhandQuantityVO condition) {
        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.mtInvOnhandQuantityRepository.propertyLimitSumAvailableOnhandQtyGet(tenantId,
                            condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitDetailAvailableOnhandQtyQuery")
    @PostMapping(value = "/limit-property/available-detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvOnhandQuantityVO6>> propertyLimitDetailAvailableOnhandQtyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtInvOnhandQuantityVO condition) {
        ResponseData<List<MtInvOnhandQuantityVO6>> responseData = new ResponseData<List<MtInvOnhandQuantityVO6>>();
        try {
            responseData.setRows(this.mtInvOnhandQuantityRepository.propertyLimitDetailAvailableOnhandQtyQuery(tenantId,
                            condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "onhandQtyUpdateProcess")
    @PostMapping(value = "/process", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandQtyUpdateProcess(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInvOnhandQuantityVO9 condition) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, condition);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "onhandQtyUpdateBatchProcess")
    @PostMapping(value = "/batch/process", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandQtyUpdateBatchProcess(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInvOnhandQuantityVO16 dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "onhandQtyUpdateBatchVerify")
    @PostMapping(value = "/batch/save/verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvOnhandQuantityVO17>> onhandQtyUpdateBatchVerify(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtInvOnhandQuantityVO14 dto) {
        ResponseData<List<MtInvOnhandQuantityVO17>> responseData = new ResponseData<>();
        try {
            responseData.setRows(mtInvOnhandQuantityRepository.onhandQtyUpdateBatchVerify(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "onhandQtyBatchUpdate")
    @PostMapping(value = "/batch/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandQtyBatchUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInvOnhandQuantityVO16 dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            mtInvOnhandQuantityRepository.onhandQtyBatchUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "propertyLimitSumAvailableOnhandQtyBatchGet")
    @PostMapping(value = "/limit-property/batch-available-total", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvOnhandQuantityVO12>> propertyLimitSumAvailableOnhandQtyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<MtInvOnhandQuantityVO> condition) {
        ResponseData<List<MtInvOnhandQuantityVO12>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.mtInvOnhandQuantityRepository.propertyLimitSumAvailableOnhandQtyBatchGet(tenantId,
                            condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
