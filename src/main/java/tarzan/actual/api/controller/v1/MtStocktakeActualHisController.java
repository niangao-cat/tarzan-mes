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
import tarzan.actual.api.dto.MtStocktakeActualHisDTO;
import tarzan.actual.domain.entity.MtStocktakeActualHis;
import tarzan.actual.domain.repository.MtStocktakeActualHisRepository;

/**
 * 盘点实绩历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtStocktakeActualHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-stocktake-actual-his")
@Api(tags = "MtStocktakeActualHis")
public class MtStocktakeActualHisController extends BaseController {

    @Autowired
    private MtStocktakeActualHisRepository repository;

    @ApiOperation(value = "stocktakeActualHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtStocktakeActualHis>> stocktakeActualHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtStocktakeActualHisDTO dto) {
        ResponseData<List<MtStocktakeActualHis>> responseData = new ResponseData<List<MtStocktakeActualHis>>();
        try {
            MtStocktakeActualHis param = new MtStocktakeActualHis();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.stocktakeActualHisQuery(tenantId, param));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
