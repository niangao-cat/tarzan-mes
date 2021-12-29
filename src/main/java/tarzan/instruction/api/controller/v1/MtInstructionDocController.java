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
import tarzan.instruction.api.dto.MtInstructionDocDTO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.api.dto.MtInstructionDocDTO3;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionDocVO4;
import tarzan.instruction.domain.vo.MtInstructionDocVO5;

/**
 * 指令单据头表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@RestController("mtInstructionDocController.v1")
@RequestMapping("/v1/{organizationId}/mt-instruction-doc")
@Api(tags = "MtInstructionDoc")
public class MtInstructionDocController extends BaseController {

    @Autowired
    private MtInstructionDocRepository repository;

    @ApiOperation("propertyLimitInstructionDocQuery")
    @PostMapping("/limit-property")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitInstructionDocQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtInstructionDocDTO dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtInstructionDocVO4 mtInstructionDoc = new MtInstructionDocVO4();
            BeanUtils.copyProperties(dto, mtInstructionDoc);
            responseData.setRows(repository.propertyLimitInstructionDocQuery(tenantId, mtInstructionDoc));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocPropertyGet")
    @PostMapping("/property")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionDoc> instructionDocPropertyGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String instructionDocId) {
        ResponseData<MtInstructionDoc> responseData = new ResponseData<MtInstructionDoc>();
        try {
            responseData.setRows(repository.instructionDocPropertyGet(tenantId, instructionDocId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocPropertyBatchGet")
    @PostMapping("/property-batch/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionDoc>> instructionDocPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody List<String> instructionDocIdList) {

        ResponseData<List<MtInstructionDoc>> responseData = new ResponseData<List<MtInstructionDoc>>();
        try {
            responseData.setRows(repository.instructionDocPropertyBatchGet(tenantId, instructionDocIdList));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocNextNumGet")
    @PostMapping("/next-num")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> instructionDocNextNumGet(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String siteId) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.instructionDocNextNumGet(tenantId, siteId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocUpdate")
    @PostMapping("/save")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionDocVO3> instructionDocUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionDocDTO2 vo,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {

        ResponseData<MtInstructionDocVO3> responseData = new ResponseData<MtInstructionDocVO3>();
        try {
            responseData.setRows(repository.instructionDocUpdate(tenantId, vo, fullUpdate));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocRelease")
    @PostMapping("/release")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionDocRelease(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionDocDTO3 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionDocRelease(tenantId, dto.getInstructionDocId(), dto.getEventRequestId());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocReleaseVerify")
    @PostMapping("/release-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionDocReleaseVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String instructionDocId) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionDocReleaseVerify(tenantId, instructionDocId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocCancel")
    @PostMapping("/cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionDocCancel(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionDocDTO3 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionDocCancel(tenantId, dto.getInstructionDocId(), dto.getEventRequestId());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocCancelVerify")
    @PostMapping("/cancel-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionDocCancelVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String instructionDocId) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionDocCancelVerify(tenantId, instructionDocId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocComplete")
    @PostMapping("/complete")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionDocComplete(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionDocDTO3 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionDocComplete(tenantId, dto.getInstructionDocId(), dto.getEventRequestId());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocCompleteVerify")
    @PostMapping("/complete-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionDocCompleteVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String instructionDocId) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionDocCompleteVerify(tenantId, instructionDocId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocCompletedCancel")
    @PostMapping("/completed-cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionDocCompletedCancel(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInstructionDocDTO3 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionDocCompletedCancel(tenantId, dto.getInstructionDocId(), dto.getEventRequestId());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocCompletedCancelVerify")
    @PostMapping("/completed-cancel-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> instructionDocCompletedCancelVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String instructionDocId) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.instructionDocCompletedCancelVerify(tenantId, instructionDocId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("instructionDocLimitInstructionAndActualQuery")
    @PostMapping("/limit-instruction-actual")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionDocVO5> instructionDocLimitInstructionAndActualQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String instructionDocId) {

        ResponseData<MtInstructionDocVO5> responseData = new ResponseData<MtInstructionDocVO5>();
        try {
            responseData.setRows(repository.instructionDocLimitInstructionAndActualQuery(tenantId, instructionDocId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
