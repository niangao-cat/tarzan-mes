package tarzan.modeling.api.controller.v1;

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
import tarzan.modeling.api.dto.MtModWorkcellScheduleDTO;
import tarzan.modeling.domain.entity.MtModWorkcellSchedule;
import tarzan.modeling.domain.repository.MtModWorkcellScheduleRepository;

/**
 * 工作单元计划属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@RestController("mtModWorkcellScheduleController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-workcell-schedule")
@Api(tags = "MtModWorkcellSchedule")
public class MtModWorkcellScheduleController extends BaseController {

    @Autowired
    private MtModWorkcellScheduleRepository repository;

    @ApiOperation("workcellSchedulePropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModWorkcellSchedule> workcellSchedulePropertyGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String workcellId) {
        ResponseData<MtModWorkcellSchedule> responseData = new ResponseData<MtModWorkcellSchedule>();
        try {
            responseData.setRows(this.repository.workcellSchedulePropertyGet(tenantId, workcellId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("workcellSchedulePropertyBatchGet")
    @PostMapping(value = "/property-batch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModWorkcellSchedule>> workcellSchedulePropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> workcellIds) {
        ResponseData<List<MtModWorkcellSchedule>> responseData = new ResponseData<List<MtModWorkcellSchedule>>();
        try {
            responseData.setRows(this.repository.workcellSchedulePropertyBatchGet(tenantId, workcellIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("workcellSchedulePropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> workcellSchedulePropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModWorkcellScheduleDTO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtModWorkcellSchedule param = new MtModWorkcellSchedule();
            BeanUtils.copyProperties(dto, param);
            this.repository.workcellSchedulePropertyUpdate(tenantId, param, fullUpdate);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
