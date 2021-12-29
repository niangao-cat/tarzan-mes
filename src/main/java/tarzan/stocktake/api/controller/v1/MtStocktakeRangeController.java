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
import tarzan.stocktake.api.dto.MtStocktakeRangeDTO;
import tarzan.stocktake.domain.entity.MtStocktakeRange;
import tarzan.stocktake.domain.repository.MtStocktakeRangeRepository;

/**
 * 盘点范围表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:35:24
 */
@RestController("mtStocktakeRangeController.v1")
@RequestMapping("/v1/{organizationId}/mt-stocktake-range")
@Api(tags = "MtStocktakeRange")
public class MtStocktakeRangeController extends BaseController {

    @Autowired
    private MtStocktakeRangeRepository repository;

    @ApiOperation(value = "stocktakeRangeQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtStocktakeRange>> stocktakeRangeQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeRangeDTO dto) {
        ResponseData<List<MtStocktakeRange>> responseData = new ResponseData<List<MtStocktakeRange>>();
        try {
            MtStocktakeRange mtStocktakeRange = new MtStocktakeRange();
            BeanUtils.copyProperties(dto, mtStocktakeRange);
            responseData.setRows(this.repository.stocktakeRangeQuery(tenantId, mtStocktakeRange));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
