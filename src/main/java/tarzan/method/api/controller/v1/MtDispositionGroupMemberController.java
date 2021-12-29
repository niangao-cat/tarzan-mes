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
import tarzan.method.domain.entity.MtDispositionGroupMember;
import tarzan.method.domain.repository.MtDispositionGroupMemberRepository;

/**
 * 处置组分配 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:47
 */
@RestController("mtDispositionGroupMemberController.v1")
@RequestMapping("/v1/{organizationId}/mt-disposition-group-member")
@Api(tags = "MtDispositionGroupMember")
public class MtDispositionGroupMemberController extends BaseController {

    @Autowired
    private MtDispositionGroupMemberRepository repository;

    @ApiOperation(value = "dispositionGroupMemberQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtDispositionGroupMember>> dispositionGroupMemberQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String dispositionGroupId) {
        ResponseData<List<MtDispositionGroupMember>> responseData = new ResponseData<List<MtDispositionGroupMember>>();
        try {
            responseData.setRows(repository.dispositionGroupMemberQuery(tenantId,
                            dispositionGroupId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "dispositionFunctionLimitDispositionGroupQuery")
    @PostMapping(value = {"/group"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtDispositionGroupMember>> dispositionFunctionLimitDispositionGroupQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String dispositionFunctionId) {
        ResponseData<List<MtDispositionGroupMember>> responseData = new ResponseData<List<MtDispositionGroupMember>>();
        try {
            responseData.setRows(repository
                            .dispositionFunctionLimitDispositionGroupQuery(tenantId, dispositionFunctionId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
