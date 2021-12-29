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
import tarzan.modeling.domain.entity.MtModAreaSchedule;
import tarzan.modeling.domain.repository.MtModAreaScheduleRepository;
import tarzan.modeling.domain.vo.MtModAreaScheduleVO;

/**
 * 区域计划属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@RestController("mtModAreaScheduleController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-area-schedule")
@Api(tags = "MtModAreaSchedule")
public class MtModAreaScheduleController extends BaseController {

    @Autowired
    private MtModAreaScheduleRepository repository;

    @ApiOperation("areaSchedulePropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModAreaSchedule> areaSchedulePropertyGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String areaId) {
        ResponseData<MtModAreaSchedule> responseData = new ResponseData<MtModAreaSchedule>();
        try {
            responseData.setRows(this.repository.areaSchedulePropertyGet(tenantId, areaId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("areaSchedulePropertyBatchGet")
    @PostMapping(value = "/property-batch/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModAreaSchedule>> areaSchedulePropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> areaIds) {
        ResponseData<List<MtModAreaSchedule>> responseData = new ResponseData<List<MtModAreaSchedule>>();
        try {
            responseData.setRows(this.repository.areaSchedulePropertyBatchGet(tenantId, areaIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("areaSchedulePropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> areaSchedulePropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModAreaScheduleVO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.areaSchedulePropertyUpdate(tenantId, dto, fullUpdate);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
