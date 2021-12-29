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
import tarzan.method.domain.entity.MtBomComponentHis;
import tarzan.method.domain.repository.MtBomComponentHisRepository;

/**
 * 装配清单行历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@RestController("mtBomComponentHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-bom-component-his")
@Api(tags = "MtBomComponentHis")
public class MtBomComponentHisController extends BaseController {

    @Autowired
    private MtBomComponentHisRepository repository;

    @ApiOperation(value = "bomComponentHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomComponentHis>> bomComponentHisQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomComponentId) {
        ResponseData<List<MtBomComponentHis>> responseData = new ResponseData<List<MtBomComponentHis>>();
        try {
            responseData.setRows(repository.bomComponentHisQuery(tenantId, bomComponentId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitBomComponentHisBatchQuery")
    @PostMapping(value = {"/property/list/limit-events"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomComponentHis>> eventLimitBomComponentHisBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtBomComponentHis>> responseData = new ResponseData<List<MtBomComponentHis>>();
        try {
            responseData.setRows(
                            this.repository.eventLimitBomComponentHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
