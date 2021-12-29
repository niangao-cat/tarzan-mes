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
import tarzan.modeling.domain.entity.MtModSiteManufacturing;
import tarzan.modeling.domain.repository.MtModSiteManufacturingRepository;
import tarzan.modeling.domain.vo.MtModSiteManufacturingVO;

/**
 * 站点生产属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@RestController("mtModSiteManufacturingController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-site-manufacturing")
@Api(tags = "MtModSiteManufacturing")
public class MtModSiteManufacturingController extends BaseController {

    @Autowired
    private MtModSiteManufacturingRepository repository;

    @ApiOperation("siteManufacturingPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModSiteManufacturing> siteManufacturingPropertyGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String siteId) {
        ResponseData<MtModSiteManufacturing> responseData = new ResponseData<MtModSiteManufacturing>();
        try {
            responseData.setRows(this.repository.siteManufacturingPropertyGet(tenantId, siteId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("siteManufacturingPropertyBatchGet")
    @PostMapping(value = "/property-batch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModSiteManufacturing>> siteManufacturingPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> condition) {
        ResponseData<List<MtModSiteManufacturing>> responseData = new ResponseData<List<MtModSiteManufacturing>>();
        try {
            responseData.setRows(this.repository.siteManufacturingPropertyBatchGet(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("siteManufacturingPropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> siteManufacturingPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModSiteManufacturingVO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.siteManufacturingPropertyUpdate(tenantId, dto, fullUpdate);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
