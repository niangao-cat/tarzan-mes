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
import tarzan.stocktake.api.dto.MtStocktakeDocHisDTO;
import tarzan.stocktake.domain.entity.MtStocktakeDocHis;
import tarzan.stocktake.domain.repository.MtStocktakeDocHisRepository;

/**
 * 盘点单据历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:35:24
 */
@RestController("mtStocktakeDocHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-stocktake-doc-his")
@Api(tags = "MtStocktakeDocHis")
public class MtStocktakeDocHisController extends BaseController {

    @Autowired
    private MtStocktakeDocHisRepository repository;

    @ApiOperation(value = "stocktakeDocHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtStocktakeDocHis>> stocktakeDocHisQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtStocktakeDocHisDTO dto) {
        ResponseData<List<MtStocktakeDocHis>> responseData = new ResponseData<List<MtStocktakeDocHis>>();
        try {
            MtStocktakeDocHis mtStocktakeDocHis = new MtStocktakeDocHis();
            BeanUtils.copyProperties(dto, mtStocktakeDocHis);
            responseData.setRows(this.repository.stocktakeDocHisQuery(tenantId, mtStocktakeDocHis));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
