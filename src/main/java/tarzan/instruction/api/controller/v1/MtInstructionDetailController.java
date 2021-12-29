package tarzan.instruction.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.instruction.domain.repository.MtInstructionDetailRepository;
import tarzan.instruction.domain.vo.*;

/**
 * 指令明细行 管理 API
 *
 * @author yiyang.xie@hand-china.com 2019-10-16 10:19:53
 */
@RestController("mtInstructionDetailController.v1")
@RequestMapping("/v1/{organizationId}/mt-instruction-detail")
@Api(tags = "MtInstructionDetail")
public class MtInstructionDetailController extends BaseController {

    @Autowired
    private MtInstructionDetailRepository repository;

    @ApiOperation(value = "propertyLimitInstructionDetailQuery")
    @PostMapping(value = {"/instruction/detail/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionDetailVO1>> propertyLimitInstructionDetailQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtInstructionDetailVO dto) {
        ResponseData<List<MtInstructionDetailVO1>> result = new ResponseData<List<MtInstructionDetailVO1>>();
        try {
            result.setRows(repository.propertyLimitInstructionDetailQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "instructionDetailCreate")
    @PostMapping(value = {"/instruction/detail/create"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> instructionDetailCreate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInstructionDetailVO2 dto) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.instructionDetailCreate(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "propertyLimitInstructionDetailBatchQuery")
    @PostMapping(value = {"/instruction/detail/batch/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionDetailVO4>> propertyLimitInstructionDetailBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<MtInstructionDetailVO3> dtos) {
        ResponseData<List<MtInstructionDetailVO4>> result = new ResponseData<List<MtInstructionDetailVO4>>();
        try {
            result.setRows(repository.propertyLimitInstructionDetailBatchQuery(tenantId, dtos));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "sourceInstructionLimitInstructionDetailCreate")
    @PostMapping(value = {"/limit/source/detail/create"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> sourceInstructionLimitInstructionDetailCreate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String instructionId) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.sourceInstructionLimitInstructionDetailCreate(tenantId, instructionId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
