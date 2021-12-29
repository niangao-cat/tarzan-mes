package tarzan.modeling.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.modeling.domain.entity.MtModProdLineManufacturing;
import tarzan.modeling.domain.repository.MtModProdLineManufacturingRepository;
import tarzan.modeling.domain.vo.MtModProdLineManufacturingVO;

/**
 * 生产线生产属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@RestController("mtModProdLineManufacturingController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-prod-line-manufacturing")
@Api(tags = "MtModProdLineManufacturing")
public class MtModProdLineManufacturingController extends BaseController {

    @Autowired
    private MtModProdLineManufacturingRepository repository;

    @ApiOperation("prodLineManufacturingPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModProdLineManufacturing> prodLineManufacturingPropertyGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String prodLineId) {
        ResponseData<MtModProdLineManufacturing> responseData = new ResponseData<MtModProdLineManufacturing>();
        try {
            responseData.setRows(this.repository.prodLineManufacturingPropertyGet(tenantId, prodLineId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("prodLineManufacturingPropertyBatchGet")
    @PostMapping(value = "/property-batch/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModProdLineManufacturing>> prodLineManufacturingPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> prodLineIds) {
        ResponseData<List<MtModProdLineManufacturing>> responseData =
                        new ResponseData<List<MtModProdLineManufacturing>>();
        try {
            responseData.setRows(this.repository.prodLineManufacturingPropertyBatchGet(tenantId, prodLineIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("prodLineManufacturingPropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> prodLineManufacturingPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModProdLineManufacturingVO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.prodLineManufacturingPropertyUpdate(tenantId, dto, fullUpdate);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
