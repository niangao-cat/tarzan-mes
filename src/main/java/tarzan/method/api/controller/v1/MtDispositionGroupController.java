package tarzan.method.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.method.domain.entity.MtDispositionGroup;
import tarzan.method.domain.repository.MtDispositionGroupRepository;

/**
 * 处置组 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:47
 */
@RestController("mtDispositionGroupController.v1")
@RequestMapping("/v1/{organizationId}/mt-disposition-group")
@Api(tags = "MtDispositionGroup")
public class MtDispositionGroupController extends BaseController {

    @Autowired
    private MtDispositionGroupRepository repository;

    @ApiOperation(value = "dispositionGroupPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtDispositionGroup> dispositionGroupPropertyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String dispositionGroupId) {
        ResponseData<MtDispositionGroup> responseData = new ResponseData<MtDispositionGroup>();
        try {
            responseData.setRows(
                            repository.dispositionGroupPropertyGet(tenantId, dispositionGroupId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "dispositionGroupRouterQuery")
    @PostMapping(value = {"/router"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> dispositionGroupRouterQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String dispositionGroupId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(
                            repository.dispositionGroupRouterQuery(tenantId, dispositionGroupId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
