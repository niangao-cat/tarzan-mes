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
import tarzan.instruction.api.dto.MtInstructionDocHisDTO;
import tarzan.instruction.api.dto.MtInstructionDocHisDTO2;
import tarzan.instruction.domain.entity.MtInstructionDocHis;
import tarzan.instruction.domain.repository.MtInstructionDocHisRepository;
import tarzan.instruction.domain.vo.MtInstructionDocHIsVO1;
import tarzan.instruction.domain.vo.MtInstructionDocHisVO;
import tarzan.instruction.domain.vo.MtInstructionDocHisVO2;

/**
 * 指令单据头历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@RestController("mtInstructionDocHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-instruction-doc-his")
@Api(tags = "MtInstructionDocHis")
public class MtInstructionDocHisController extends BaseController {

    @Autowired
    private MtInstructionDocHisRepository repository;

    @ApiOperation("eventLimitInstructionDocHisQuery")
    @PostMapping("/limit-event/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionDocHisVO>> eventLimitInstructionDocHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String eventId) {

        ResponseData<List<MtInstructionDocHisVO>> responseData = new ResponseData<List<MtInstructionDocHisVO>>();
        try {
            responseData.setRows(repository.eventLimitInstructionDocHisQuery(tenantId, eventId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("eventLimitInstructionDocHisBatchQuery")
    @PostMapping("/limit-event-batch/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionDocHisVO>> eventLimitInstructionDocHisBatchQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> eventIdList) {

        ResponseData<List<MtInstructionDocHisVO>> responseData = new ResponseData<List<MtInstructionDocHisVO>>();
        try {
            responseData.setRows(repository.eventLimitInstructionDocHisBatchQuery(tenantId, eventIdList));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocHisPropertyQuery")
    @PostMapping("/property/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionDocHisVO>> instructionDocHisPropertyQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtInstructionDocHisDTO dto) {

        ResponseData<List<MtInstructionDocHisVO>> responseData = new ResponseData<List<MtInstructionDocHisVO>>();
        try {
            MtInstructionDocHis mtInstructionDocHis = new MtInstructionDocHis();
            BeanUtils.copyProperties(dto, mtInstructionDocHis);
            responseData.setRows(repository.instructionDocHisPropertyQuery(tenantId, mtInstructionDocHis));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocLimitHisQuery")
    @PostMapping("/limit/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionDocHisVO>> instructionDocLimitHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String instructionDocId) {

        ResponseData<List<MtInstructionDocHisVO>> responseData = new ResponseData<List<MtInstructionDocHisVO>>();
        try {
            MtInstructionDocHis mtInstructionDocHis = new MtInstructionDocHis();
            mtInstructionDocHis.setInstructionDocId(instructionDocId);
            responseData.setRows(repository.instructionDocLimitHisQuery(tenantId, mtInstructionDocHis));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitInstructionDocHisQuery")
    @PostMapping("/limit-property/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionDocHis>> propertyLimitInstructionDocHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtInstructionDocHisDTO2 dto) {

        ResponseData<List<MtInstructionDocHis>> responseData = new ResponseData<List<MtInstructionDocHis>>();
        try {
            MtInstructionDocHIsVO1 mtInstructionDocHIsVO1 = new MtInstructionDocHIsVO1();
            BeanUtils.copyProperties(dto, mtInstructionDocHIsVO1);
            responseData.setRows(repository.propertyLimitInstructionDocHisQuery(tenantId, mtInstructionDocHIsVO1));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocLatestHisGet")
    @PostMapping("/instruction/his")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionDocHisVO2> instructionDocLatestHisGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String instructionDocId) {
        ResponseData<MtInstructionDocHisVO2> responseData = new ResponseData<>();
        try {
            responseData.setRows(repository.instructionDocLatestHisGet(tenantId, instructionDocId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
