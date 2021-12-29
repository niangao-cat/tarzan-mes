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
import tarzan.modeling.api.dto.MtModWorkcellManufacturingDTO;
import tarzan.modeling.domain.entity.MtModWorkcellManufacturing;
import tarzan.modeling.domain.repository.MtModWorkcellManufacturingRepository;

/**
 * 工作单元生产属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@RestController("mtModWorkcellManufacturingController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-workcell-manufacturing")
@Api(tags = "MtModWorkcellManufacturing")
public class MtModWorkcellManufacturingController extends BaseController {

    @Autowired
    private MtModWorkcellManufacturingRepository repository;

    @ApiOperation("workcellManufacturingPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModWorkcellManufacturing> workcellManufacturingPropertyGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String workcellId) {
        ResponseData<MtModWorkcellManufacturing> responseData = new ResponseData<MtModWorkcellManufacturing>();
        try {
            responseData.setRows(this.repository.workcellManufacturingPropertyGet(tenantId, workcellId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("workcellManufacturingPropertyBatchGet")
    @PostMapping(value = "/property-batch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModWorkcellManufacturing>> workcellManufacturingPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> condition) {
        ResponseData<List<MtModWorkcellManufacturing>> responseData =
                        new ResponseData<List<MtModWorkcellManufacturing>>();
        try {
            responseData.setRows(this.repository.workcellManufacturingPropertyBatchGet(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("workcellManufacturingPropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> workcellManufacturingPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModWorkcellManufacturingDTO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtModWorkcellManufacturing param = new MtModWorkcellManufacturing();
            BeanUtils.copyProperties(dto, param);
            this.repository.workcellManufacturingPropertyUpdate(tenantId, param, fullUpdate);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
