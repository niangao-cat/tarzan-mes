package tarzan.method.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.method.api.dto.MtOperationSubstepDTO;
import tarzan.method.app.service.MtOperationSubstepService;
import tarzan.method.domain.entity.MtOperationSubstep;

/**
 * 工艺子步骤 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
@RestController("mtOperationSubstepController.v1")
@RequestMapping("/v1/{organizationId}/mt-operation-substep")
@Api(tags = "MtOperationSubstep")
public class MtOperationSubstepController extends BaseController {
    @Autowired
    private MtOperationSubstepService service;

    @ApiOperation(value = "UI查询工艺子步骤列表")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtOperationSubstepDTO>> queryOperationSubstepListForUi(
            @PathVariable("organizationId") Long tenantId, String operationId,
            @ApiIgnore @SortDefault(value = MtOperationSubstep.FIELD_SEQUENCE,
                    direction = Sort.Direction.ASC) PageRequest pageRequest) {
        ResponseData<Page<MtOperationSubstepDTO>> responseData = new ResponseData<Page<MtOperationSubstepDTO>>();
        try {
            responseData.setRows(service.queryOperationSubstepListForUi(tenantId, operationId, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI删除工艺子步骤")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeOperationSubstepForUi(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody String operationSubstepId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.removeOperationSubstepForUi(tenantId, operationSubstepId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
