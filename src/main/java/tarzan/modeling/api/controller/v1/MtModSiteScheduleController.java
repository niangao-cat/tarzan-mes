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
import tarzan.modeling.domain.entity.MtModSiteSchedule;
import tarzan.modeling.domain.repository.MtModSiteScheduleRepository;
import tarzan.modeling.domain.vo.MtModSiteScheduleVO;

/**
 * 站点计划属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@RestController("mtModSiteScheduleController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-site-schedule")
@Api(tags = "MtModSiteSchedule")
public class MtModSiteScheduleController extends BaseController {

    @Autowired
    private MtModSiteScheduleRepository repository;

    @ApiOperation("siteSchedulePropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModSiteSchedule> siteSchedulePropertyGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String siteId) {
        ResponseData<MtModSiteSchedule> responseData = new ResponseData<MtModSiteSchedule>();
        try {
            responseData.setRows(this.repository.siteSchedulePropertyGet(tenantId, siteId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("siteSchedulePropertyBatchGet")
    @PostMapping(value = "/property-batch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModSiteSchedule>> siteSchedulePropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> siteIds) {
        ResponseData<List<MtModSiteSchedule>> responseData = new ResponseData<List<MtModSiteSchedule>>();
        try {
            responseData.setRows(this.repository.siteSchedulePropertyBatchGet(tenantId, siteIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("siteSchedulePropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> siteSchedulePropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModSiteScheduleVO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.siteSchedulePropertyUpdate(tenantId, dto, fullUpdate);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
