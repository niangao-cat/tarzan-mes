package tarzan.instruction.api.controller.v1;

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
import tarzan.instruction.api.dto.MtInstructionHisDTO;
import tarzan.instruction.domain.entity.MtInstructionHis;
import tarzan.instruction.domain.repository.MtInstructionHisRepository;
import tarzan.instruction.domain.vo.MtInstructionHisVO;
import tarzan.instruction.domain.vo.MtInstructionHisVO1;
import tarzan.instruction.domain.vo.MtInstructionHisVO2;

/**
 * 仓储物流指令内容历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@RestController("mtInstructionHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-instruction-his")
@Api(tags = "MtInstructionHis")
public class MtInstructionHisController extends BaseController {

    @Autowired
    private MtInstructionHisRepository repository;

    @ApiOperation("eventLimitInstructionHisQuery")
    @PostMapping("/limit-event/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionHisVO>> eventLimitInstructionHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String eventId) {
        ResponseData<List<MtInstructionHisVO>> responseData = new ResponseData<List<MtInstructionHisVO>>();
        try {
            responseData.setRows(repository.eventLimitInstructionHisQuery(tenantId, eventId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("eventLimitInstructionHisBatchQuery")
    @PostMapping("/limit-event-batch/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionHisVO>> eventLimitInstructionHisBatchQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> eventIdList) {
        ResponseData<List<MtInstructionHisVO>> responseData = new ResponseData<List<MtInstructionHisVO>>();
        try {
            responseData.setRows(repository.eventLimitInstructionHisBatchQuery(tenantId, eventIdList));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionLimitHisQuery")
    @PostMapping("/limit/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionHisVO>> instructionLimitHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String instructionId) {
        ResponseData<List<MtInstructionHisVO>> responseData = new ResponseData<List<MtInstructionHisVO>>();
        try {
            responseData.setRows(repository.instructionLimitHisQuery(tenantId, instructionId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitInstructionHisQuery")
    @PostMapping("/limit-property/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionHis>> propertyLimitInstructionHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtInstructionHisDTO dto) {
        ResponseData<List<MtInstructionHis>> responseData = new ResponseData<List<MtInstructionHis>>();
        try {
            MtInstructionHisVO1 mtInstructionHis = new MtInstructionHisVO1();
            BeanUtils.copyProperties(dto, mtInstructionHis);
            responseData.setRows(repository.propertyLimitInstructionHisQuery(tenantId, mtInstructionHis));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionLatestHisGet")
    @PostMapping("/instruction/his/get")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionHisVO2> instructionLatestHisGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String instructionId) {
        ResponseData<MtInstructionHisVO2> responseData = new ResponseData<>();
        try {
            responseData.setRows(repository.instructionLatestHisGet(tenantId, instructionId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
