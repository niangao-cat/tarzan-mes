package tarzan.dispatch.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.dispatch.domain.repository.MtDispatchStrategyOrgRelRepository;
import tarzan.dispatch.domain.vo.*;
import java.util.List;

/**
 * 调度策略与组织关系表 管理 API
 *
 * @author yiyang.xie 2020-02-03 19:42:38
 */
@RestController("mtDispatchStrategyOrgRelController.v1")
@RequestMapping("/v1/{organizationId}/mt-dispatch-strategy-org-rel")
@Api(tags = "MtDispatchStrategyOrgRel")
public class MtDispatchStrategyOrgRelController extends BaseController {

    @Autowired
    private MtDispatchStrategyOrgRelRepository repository;

    @ApiOperation(value = "dispatchStrategyOrganizationRelationUpdate")
    @PostMapping(value = "/dispatch/strategy/org-rel/update", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> dispatchStrategyOrganizationRelationUpdate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtDispatchStrategyOrgRelVO1 vo) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.dispatchStrategyOrganizationRelationUpdate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "organizationLimitDispatchStrategyQuery")
    @PostMapping(value = "/strategy/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtDispatchStrategyOrgRelVO3>> organizationLimitDispatchStrategyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtDispatchStrategyOrgRelVO2 vo) {
        ResponseData<List<MtDispatchStrategyOrgRelVO3>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.organizationLimitDispatchStrategyQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "propertyLimitDispatchStrategyOrganizationRelationQuery")
    @PostMapping(value = "/relation/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtDispatchStrategyOrgRelVO5>> propertyLimitDispatchStrategyOrganizationRelationQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtDispatchStrategyOrgRelVO4 vo) {
        ResponseData<List<MtDispatchStrategyOrgRelVO5>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.propertyLimitDispatchStrategyOrganizationRelationQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
