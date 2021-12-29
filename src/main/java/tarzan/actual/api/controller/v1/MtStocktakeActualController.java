package tarzan.actual.api.controller.v1;

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
import tarzan.actual.api.dto.MtStocktakeActualDTO;
import tarzan.actual.api.dto.MtStocktakeActualDTO2;
import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.actual.domain.repository.MtStocktakeActualRepository;
import tarzan.actual.domain.vo.MtStocktakeActualVO;
import tarzan.actual.domain.vo.MtStocktakeActualVO1;
import tarzan.actual.domain.vo.MtStocktakeActualVO2;
import tarzan.actual.domain.vo.MtStocktakeActualVO4;

/**
 * 盘点实绩表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtStocktakeActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-stocktake-actual")
@Api(tags = "MtStocktakeActual")
public class MtStocktakeActualController extends BaseController {

    @Autowired
    private MtStocktakeActualRepository repository;

    @ApiOperation(value = "stocktakeActualQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtStocktakeActual>> stocktakeActualQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeActualDTO dto) {
        ResponseData<List<MtStocktakeActual>> responseData = new ResponseData<List<MtStocktakeActual>>();
        try {
            MtStocktakeActual param = new MtStocktakeActual();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.stocktakeActualQuery(tenantId, param));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeLimitActualPropertyQuery")
    @PostMapping(value = {"/limit-stocktake/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtStocktakeActual>> stocktakeLimitActualPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String stocktakeId) {
        ResponseData<List<MtStocktakeActual>> responseData = new ResponseData<List<MtStocktakeActual>>();
        try {
            responseData.setRows(repository.stocktakeLimitActualPropertyQuery(tenantId, stocktakeId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeActualUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeActualUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeActualVO updateVO) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.stocktakeActualUpdate(tenantId, updateVO);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktaketActualDifferenceCreate")
    @PostMapping(value = {"/difference/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktaketActualDifferenceCreate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeActualDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.stocktaketActualDifferenceCreate(tenantId, dto.getStocktakeId(), dto.getEventRequestId());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeFirstcount")
    @PostMapping(value = {"/first/count"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeFirstcount(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeActualVO1 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.stocktakeFirstcount(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeRecount")
    @PostMapping(value = {"/recount"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeRecount(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeActualVO1 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.stocktakeRecount(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeActualDifferenceAdjust")
    @PostMapping(value = {"/difference/adjust"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeActualDifferenceAdjust(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeActualVO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.stocktakeActualDifferenceAdjust(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stocktakeActualDifferenceBatchAdjust")
    @PostMapping(value = {"/difference/batch/adjust"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> stocktakeActualDifferenceBatchAdjust(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeActualVO4 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.stocktakeActualDifferenceBatchAdjust(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
