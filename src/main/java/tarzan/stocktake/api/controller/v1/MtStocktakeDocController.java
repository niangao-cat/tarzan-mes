package tarzan.stocktake.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.stocktake.api.dto.MtStocktakeDocDTO;
import tarzan.stocktake.api.dto.MtStocktakeDocDTO2;
import tarzan.stocktake.domain.entity.MtStocktakeDoc;
import tarzan.stocktake.domain.repository.MtStocktakeDocRepository;
import tarzan.stocktake.domain.vo.*;

/**
 * 盘点单据头表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:35:24
 */
@RestController("mtStocktakeDocController.v1")
@RequestMapping("/v1/{organizationId}/mt-stocktake-doc")
@Api(tags = "MtStocktakeDoc")
public class MtStocktakeDocController extends BaseController {

    @Autowired
    private MtStocktakeDocRepository repository;

    @ApiOperation(value = "propertyLimitStocktakeDocQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitStocktakeDocQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocDTO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtStocktakeDocVO mtStocktakeDocVO = new MtStocktakeDocVO();
            BeanUtils.copyProperties(dto, mtStocktakeDocVO);
            responseData.setRows(repository.propertyLimitStocktakeDocQuery(tenantId, mtStocktakeDocVO));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeDocPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtStocktakeDoc> stocktakeDocPropertyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String stocktakeId) {
        ResponseData<MtStocktakeDoc> responseData = new ResponseData<MtStocktakeDoc>();
        try {
            responseData.setRows(this.repository.stocktakeDocPropertyGet(tenantId, stocktakeId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeDocPropertyBatchGet")
    @PostMapping(value = {"/property-batch/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtStocktakeDoc>> stocktakeDocPropertyBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> stocktakeIdList) {
        ResponseData<List<MtStocktakeDoc>> responseData = new ResponseData<List<MtStocktakeDoc>>();
        try {
            responseData.setRows(this.repository.stocktakeDocPropertyBatchGet(tenantId, stocktakeIdList));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeDocCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> stocktakeDocCreate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocVO1 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.stocktakeDocCreate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeDocUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeDocUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocVO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.stocktakeDocUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeDocStatusUpdateVerify")
    @PostMapping(value = {"/modify-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeDocStatusUpdateVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocVO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.stocktakeDocStatusUpdateVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeDocRelease")
    @PostMapping(value = {"/release"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeDocRelease(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.stocktakeDocRelease(tenantId, dto.getStocktakeId(), dto.getEventRequestId());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeDocComplete")
    @PostMapping(value = {"/complete"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeDocComplete(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.stocktakeDocComplete(tenantId, dto.getStocktakeId(), dto.getEventRequestId());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeDocClose")
    @PostMapping(value = {"/close"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeDocClose(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.stocktakeDocClose(tenantId, dto.getStocktakeId(), dto.getEventRequestId());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeRangeLimitMaterialLotQuery")
    @PostMapping(value = {"/limit-range/material-lot"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> stocktakeRangeLimitMaterialLotQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String stocktakeId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.stocktakeRangeLimitMaterialLotQuery(tenantId, stocktakeId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktaketAndMaterialLotDifferenceQuery")
    @PostMapping(value = {"/material-lot-difference"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> stocktaketAndMaterialLotDifferenceQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String stocktakeId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(
                            repository.stocktaketAndMaterialLotDifferenceQuery(tenantId, stocktakeId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeDocFirstcountComplete")
    @PostMapping(value = {"/first-count-complete"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeDocFirstcountComplete(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.stocktakeDocFirstcountComplete(tenantId, dto.getStocktakeId(),
                            dto.getEventRequestId());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeDocCountComplete")
    @PostMapping(value = {"/count-complete"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeDocCountComplete(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.stocktakeDocCountComplete(tenantId, dto.getStocktakeId(),
                            dto.getEventRequestId());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeStatusBackout")
    @PostMapping(value = {"/status-back-out"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeStatusBackout(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtStocktakeDocVO4 mtStocktakeDocVO4 = new MtStocktakeDocVO4();
            BeanUtils.copyProperties(dto, mtStocktakeDocVO4);
            this.repository.stocktakeStatusBackout(tenantId, mtStocktakeDocVO4);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeLimitMaterialLotUnlock")
    @PostMapping(value = {"/limit-stocktake/material-lot-unlock"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeLimitMaterialLotUnlock(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.stocktakeLimitMaterialLotUnlock(tenantId, dto.getStocktakeId(),
                            dto.getEventRequestId());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeLimitMaterialLotLock")
    @PostMapping(value = {"/limit-stocktake/material-lot-lock"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeLimitMaterialLotLock(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.stocktakeLimitMaterialLotLock(tenantId, dto.getStocktakeId(),
                            dto.getEventRequestId());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
