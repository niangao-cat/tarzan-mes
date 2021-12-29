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
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.instruction.api.dto.MtInstructionDTO;
import tarzan.instruction.api.dto.MtInstructionDTO3;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.*;

/**
 * 仓储物流指令内容表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@RestController("mtInstructionController.v1")
@RequestMapping("/v1/{organizationId}/mt-instruction")
@Api(tags = "MtInstruction")
public class MtInstructionController extends BaseController {

    @Autowired
    private MtInstructionRepository repository;

    @ApiOperation("propertyLimitInstructionQuery")
    @PostMapping("/limit-property")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitInstructionQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtInstructionDTO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtInstructionVO10 mtInstruction = new MtInstructionVO10();
            BeanUtils.copyProperties(dto, mtInstruction);
            responseData.setRows(repository.propertyLimitInstructionQuery(tenantId, mtInstruction));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionPropertyGet")
    @PostMapping("/property")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstruction> instructionPropertyGet(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String instructionId) {
        ResponseData<MtInstruction> responseData = new ResponseData<MtInstruction>();
        try {
            responseData.setRows(repository.instructionPropertyGet(tenantId, instructionId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionPropertyBatchGet")
    @PostMapping("/property-batch/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstruction>> instructionPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody List<String> instructionIdList) {
        ResponseData<List<MtInstruction>> responseData = new ResponseData<List<MtInstruction>>();
        try {
            responseData.setRows(repository.instructionPropertyBatchGet(tenantId, instructionIdList));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionNextNumGet")
    @PostMapping("/next-num")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> instructionNextNumGet(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String siteId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.instructionNextNumGet(tenantId, siteId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionUpdate")
    @PostMapping("/save")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionVO6> instructionUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionVO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<MtInstructionVO6> responseData = new ResponseData<MtInstructionVO6>();
        try {
            responseData.setRows(repository.instructionUpdate(tenantId, dto, fullUpdate));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionExecute")
    @PostMapping("/execution")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionVO4> instructionExecute(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionVO3 executeVO) {
        ResponseData<MtInstructionVO4> responseData = new ResponseData<MtInstructionVO4>();
        try {
            responseData.setRows(repository.instructionExecute(tenantId, executeVO));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionExecuteVerify")
    @PostMapping("/execution-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionExecuteVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionVO8 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionExecuteVerify(tenantId, vo);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionExecutedBack")
    @PostMapping("/executed-back")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionVO5>> instructionExecutedBack(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtInstructionDTO3 dto) {
        ResponseData<List<MtInstructionVO5>> responseData = new ResponseData<List<MtInstructionVO5>>();
        try {
            responseData.setRows(repository.instructionExecutedBack(tenantId, dto.getInstructionId(),
                            dto.getEventRequestId()));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionRelease")
    @PostMapping("/release")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionRelease(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionDTO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionRelease(tenantId, dto.getInstructionId(), dto.getEventRequestId());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("instructionReleaseVerify")
    @PostMapping("/release-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionReleaseVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String instructionId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionReleaseVerify(tenantId, instructionId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionCancel")
    @PostMapping("/cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionCancel(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionDTO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionCancel(tenantId, dto.getInstructionId(), dto.getEventRequestId());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("instructionCancelVerify")
    @PostMapping("/cancel-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionCancelVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String instructionId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionCancelVerify(tenantId, instructionId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionComplete")
    @PostMapping("/complete")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionComplete(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionDTO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionComplete(tenantId, dto.getInstructionId(), dto.getEventRequestId());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionCompleteVerify")
    @PostMapping("/complete-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionCompleteVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String instructionId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionCompleteVerify(tenantId, instructionId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionCompletedCancel")
    @PostMapping("/completed-cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionCompletedCancel(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionDTO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionCompletedCancel(tenantId, dto.getInstructionId(), dto.getEventRequestId());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionCompletedCancelVerify")
    @PostMapping("/completed-cancel-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionCompletedCancelVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String instructionId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionCompletedCancelVerify(tenantId, instructionId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionExecutedCancel")
    @PostMapping("/executed-cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionVO9> instructionExecutedCancel(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtInstructionVO12 executeVO) {
        ResponseData<MtInstructionVO9> responseData = new ResponseData<MtInstructionVO9>();
        try {
            responseData.setRows(repository.instructionExecutedCancel(tenantId, executeVO));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionLimitInstructionAndActualQuery")
    @PostMapping("/limit-property-instruction-actual")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionVO11> instructionLimitInstructionAndActualQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String instructionId) {
        ResponseData<MtInstructionVO11> responseData = new ResponseData<MtInstructionVO11>();
        try {
            responseData.setRows(repository.instructionLimitInstructionAndActualQuery(tenantId, instructionId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("noInstructionExecute")
    @PostMapping("/no-execution")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionVO15> noInstructionExecute(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionVO13 executeVO) {
        ResponseData<MtInstructionVO15> responseData = new ResponseData<MtInstructionVO15>();
        try {
            responseData.setRows(repository.noInstructionExecute(tenantId, executeVO));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "instructionAttrPropertyUpdate")
    @PostMapping(value = {"/attr-table-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> instructionAttrTableUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendVO10 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            this.repository.instructionAttrPropertyUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionExecuteStatusVerify")
    @PostMapping("/execution-status/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionExecuteStatusVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String instructionId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionExecuteStatusVerify(tenantId, instructionId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionExecuteMaterialLotVerify")
    @PostMapping("/execution-material-lot/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionExecuteMaterialLotVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionVO16 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionExecuteMaterialLotVerify(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionExecuteMaterialVerify")
    @PostMapping("/execution-material/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionExecuteMaterialVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionVO17 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionExecuteMaterialVerify(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionExecuteLocatorVerify")
    @PostMapping("/locator/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionExecuteLocatorVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionVO18 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionExecuteLocatorVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("instructionExecuteQtyVerify")
    @PostMapping("/qty/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionExecuteQtyVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionVO19 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionExecuteQtyVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionExecuteLocatorBatchVerify")
    @PostMapping("/locator/batch/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionExecuteLocatorBatchVerify(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<MtInstructionVO18> dtos) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionExecuteLocatorBatchVerify(tenantId, dtos);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("instructionExecuteMaterialBatchVerify")
    @PostMapping("/material/batch/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionExecuteMaterialBatchVerify(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<MtInstructionVO17> dtos) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionExecuteMaterialBatchVerify(tenantId, dtos);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionExecuteStatusBatchVerify")
    @PostMapping("/status/batch/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionExecuteStatusBatchVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody List<String> instructionIdList) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionExecuteStatusBatchVerify(tenantId, instructionIdList);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("instructionExecuteQtyBatchVerify")
    @PostMapping("/qty/batch/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionExecuteQtyBatchVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody List<MtInstructionVO19> dtos) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionExecuteQtyBatchVerify(tenantId, dtos);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("instructionExecuteMaterialLotBatchVerify")
    @PostMapping("/materialLot/batch/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionExecuteMaterialLotBatchVerify(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<MtInstructionVO16> dtos) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionExecuteMaterialLotBatchVerify(tenantId, dtos);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionExecuteBatchVerify")
    @PostMapping("/execute/batch/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionExecuteBatchVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody List<MtInstructionVO8> dtos) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionExecuteBatchVerify(tenantId, dtos);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionBatchExecute")
    @PostMapping("/batch/execute")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionVO22>> instructionBatchExecute(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtInstructionVO21 dtos) {
        ResponseData<List<MtInstructionVO22>> responseData = new ResponseData<List<MtInstructionVO22>>();
        try {
            responseData.setRows(repository.instructionBatchExecute(tenantId, dtos));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
