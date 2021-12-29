package tarzan.actual.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.actual.domain.repository.MtHoldActualRepository;
import tarzan.actual.domain.vo.MtHoldActualVO;
import tarzan.actual.domain.vo.MtHoldActualVO2;

/**
 * 保留实绩 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtHoldActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-hold-actual")
@Api(tags = "MtHoldActual")
public class MtHoldActualController extends BaseController {
    @Autowired
    private MtHoldActualRepository repository;

    @ApiOperation(value = "holdCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtHoldActualVO2> holdCreate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtHoldActualVO dto) {
        ResponseData<MtHoldActualVO2> responseData = new ResponseData<MtHoldActualVO2>();
        try {
            responseData.setRows(repository.holdCreate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
