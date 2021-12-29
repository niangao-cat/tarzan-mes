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
import tarzan.method.domain.entity.MtBomSubstituteGroupHis;
import tarzan.method.domain.repository.MtBomSubstituteGroupHisRepository;

/**
 * 装配清单行替代组历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@RestController("mtBomSubstituteGroupHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-bom-substitute-group-his")
@Api(tags = "MtBomSubstituteGroupHis")
public class MtBomSubstituteGroupHisController extends BaseController {

    @Autowired
    private MtBomSubstituteGroupHisRepository repository;

    @ApiOperation(value = "bomSubstituteGroupHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteGroupHis>> bomSubstituteGroupHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String bomSubstituteGroupId) {
        ResponseData<List<MtBomSubstituteGroupHis>> responseData = new ResponseData<List<MtBomSubstituteGroupHis>>();
        try {
            responseData.setRows(repository.bomSubstituteGroupHisQuery(tenantId,
                            bomSubstituteGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitBomSubstituteGroupHisBatchQuery")
    @PostMapping(value = {"/limit-events"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteGroupHis>> eventLimitBomSubstituteGroupHisBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtBomSubstituteGroupHis>> responseData = new ResponseData<List<MtBomSubstituteGroupHis>>();
        try {
            responseData.setRows(this.repository
                            .eventLimitBomSubstituteGroupHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
