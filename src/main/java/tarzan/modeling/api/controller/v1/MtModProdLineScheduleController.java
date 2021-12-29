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
import tarzan.modeling.domain.entity.MtModProdLineSchedule;
import tarzan.modeling.domain.repository.MtModProdLineScheduleRepository;
import tarzan.modeling.domain.vo.MtModProdLineScheduleVO;

/**
 * 生产线计划属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@RestController("mtModProdLineScheduleController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-prod-line-schedule")
@Api(tags = "MtModProdLineSchedule")
public class MtModProdLineScheduleController extends BaseController {

    @Autowired
    private MtModProdLineScheduleRepository repository;

    @ApiOperation("prodLineSchedulePropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModProdLineSchedule> prodLineSchedulePropertyGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String prodLineId) {
        ResponseData<MtModProdLineSchedule> responseData = new ResponseData<MtModProdLineSchedule>();
        try {
            responseData.setRows(this.repository.prodLineSchedulePropertyGet(tenantId, prodLineId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("prodLineSchedulePropertyBatchGet")
    @PostMapping(value = "/property-batch/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModProdLineSchedule>> prodLineSchedulePropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> prodLineIds) {
        ResponseData<List<MtModProdLineSchedule>> responseData = new ResponseData<List<MtModProdLineSchedule>>();
        try {
            responseData.setRows(this.repository.prodLineSchedulePropertyBatchGet(tenantId, prodLineIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("prodLineSchedulePropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> prodLineSchedulePropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModProdLineScheduleVO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.prodLineSchedulePropertyUpdate(tenantId, dto, fullUpdate);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
