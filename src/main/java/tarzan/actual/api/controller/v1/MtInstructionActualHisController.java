package tarzan.actual.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.actual.domain.repository.MtInstructionActualHisRepository;
import tarzan.actual.domain.vo.MtInstructionActualHisVO;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

/**
 * 指令实绩汇总历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtInstructionActualHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-instruction-actual-his")
@Api(tags = "MtInstructionActualHis")
public class MtInstructionActualHisController extends BaseController {

    @Autowired
    private MtInstructionActualHisRepository mtInstructionActualHisRepository;

    @ApiOperation(value = "instructionActualLatestHisGet")
    @PostMapping(value = "/instruction/his/get")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInstructionActualHisVO> instructionActualLatestHisGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String actualId) {
        ResponseData<MtInstructionActualHisVO> responseData = new ResponseData<>();
        try {
            responseData.setRows(mtInstructionActualHisRepository.instructionActualLatestHisGet(tenantId, actualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
