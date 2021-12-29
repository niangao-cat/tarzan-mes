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
import tarzan.modeling.domain.entity.MtModAreaPurchase;
import tarzan.modeling.domain.repository.MtModAreaPurchaseRepository;
import tarzan.modeling.domain.vo.MtModAreaPurchaseVO;

/**
 * 区域采购属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@RestController("mtModAreaPurchaseController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-area-purchase")
@Api(tags = "MtModAreaPurchase")
public class MtModAreaPurchaseController extends BaseController {

    @Autowired
    private MtModAreaPurchaseRepository repository;

    @ApiOperation(value = "areaPurchasePropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModAreaPurchase> areaPurchasePropertyGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String areaId) {
        ResponseData<MtModAreaPurchase> responseData = new ResponseData<MtModAreaPurchase>();
        try {
            responseData.setRows(this.repository.areaPurchasePropertyGet(tenantId, areaId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "areaPurchasePropertyBatchGet")
    @PostMapping(value = "/property-batch/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModAreaPurchase>> areaPurchasePropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> areaIds) {
        ResponseData<List<MtModAreaPurchase>> responseData = new ResponseData<List<MtModAreaPurchase>>();
        try {
            responseData.setRows(this.repository.areaPurchasePropertyBatchGet(tenantId, areaIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "areaPurchasePropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> areaPurchasePropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModAreaPurchaseVO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.areaPurchasePropertyUpdate(tenantId, dto, fullUpdate);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
