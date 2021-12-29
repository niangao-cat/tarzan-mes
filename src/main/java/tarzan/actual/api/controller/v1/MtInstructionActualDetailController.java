package tarzan.actual.api.controller.v1;

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
import tarzan.actual.api.dto.MtInstructionActualDetailDTO;
import tarzan.actual.api.dto.MtInstructionActualDetailDTO2;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO2;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO3;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO4;

/**
 * 指令实绩明细表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtInstructionActualDetailController.v1")
@RequestMapping("/v1/{organizationId}/mt-instruction-actual-detail")
@Api(tags = "MtInstructionActualDetail")
public class MtInstructionActualDetailController extends BaseController {
    @Autowired
    private MtInstructionActualDetailRepository repository;

    @ApiOperation(value = "instructionActualDetailCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> instructionActualDetailCreate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInstructionActualDetailDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtInstructionActualDetail param = new MtInstructionActualDetail();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.instructionActualDetailCreate(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitInstructionActualDetailQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionActualDetailVO>> propertyLimitInstructionActualDetailQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtInstructionActualDetailDTO2 dto) {
        ResponseData<List<MtInstructionActualDetailVO>> responseData =
                        new ResponseData<List<MtInstructionActualDetailVO>>();
        try {
            MtInstructionActualDetail param = new MtInstructionActualDetail();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.propertyLimitInstructionActualDetailQuery(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "instructionLimitActualDetailQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionActualDetail>> instructionLimitActualDetailQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String instructionId) {
        ResponseData<List<MtInstructionActualDetail>> responseData =
                        new ResponseData<List<MtInstructionActualDetail>>();
        try {
            responseData.setSuccess(true);
            responseData.setRows(repository.instructionLimitActualDetailQuery(tenantId, instructionId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "instructionActualLimitDetailBatchQuery")
    @PostMapping(value = {"/property/batch/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionActualDetail>> instructionActualLimitDetailBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> actualIds) {
        ResponseData<List<MtInstructionActualDetail>> responseData =
                        new ResponseData<List<MtInstructionActualDetail>>();
        try {
            responseData.setSuccess(true);
            responseData.setRows(repository.instructionActualLimitDetailBatchQuery(tenantId, actualIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "instructActDetailAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property-update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> instructActDetailAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendVO10 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            this.repository.instructActDetailAttrPropertyUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "instructionLimitActualDetailBatchQuery")
    @PostMapping(value = {"/actual/detail/batch/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionActualDetailVO2>> instructionLimitActualDetailBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> instructionIdList) {
        ResponseData<List<MtInstructionActualDetailVO2>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.instructionLimitActualDetailBatchQuery(tenantId, instructionIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "instructionActualDetailBatchCreate")
    @PostMapping(value = {"/actual/detail/batch/create"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInstructionActualDetailVO3>> instructionActualDetailBatchCreate(
                    @PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtInstructionActualDetailVO4> actualDetailMessageList) {
        ResponseData<List<MtInstructionActualDetailVO3>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.instructionActualDetailBatchCreate(tenantId, actualDetailMessageList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
