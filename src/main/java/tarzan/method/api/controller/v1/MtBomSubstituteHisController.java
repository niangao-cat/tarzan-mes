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
import tarzan.method.domain.entity.MtBomSubstituteHis;
import tarzan.method.domain.repository.MtBomSubstituteHisRepository;

/**
 * 装配清单行替代项历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@RestController("mtBomSubstituteHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-bom-substitute-his")
@Api(tags = "MtBomSubstituteHis")
public class MtBomSubstituteHisController extends BaseController {

    @Autowired
    private MtBomSubstituteHisRepository repository;

    @ApiOperation(value = "bomSubstituteHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteHis>> bomSubstituteHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String bomSubstituteId) {
        ResponseData<List<MtBomSubstituteHis>> responseData = new ResponseData<List<MtBomSubstituteHis>>();
        try {
            responseData.setRows(repository.bomSubstituteHisQuery(tenantId, bomSubstituteId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitBomSubstituteHisBatchQuery")
    @PostMapping(value = {"/limit-events"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteHis>> eventLimitBomSubstituteHisBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtBomSubstituteHis>> responseData = new ResponseData<List<MtBomSubstituteHis>>();
        try {
            responseData.setRows(
                            this.repository.eventLimitBomSubstituteHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
