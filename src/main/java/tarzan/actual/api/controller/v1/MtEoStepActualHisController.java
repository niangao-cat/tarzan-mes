package tarzan.actual.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.actual.domain.entity.MtEoStepActualHis;
import tarzan.actual.domain.repository.MtEoStepActualHisRepository;

/**
 * 执行作业-工艺路线步骤执行实绩 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtEoStepActualHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-step-actual-his")
@Api(tags = "MtEoStepActualHis")
public class MtEoStepActualHisController extends BaseController {
    @Autowired
    private MtEoStepActualHisRepository repository;

    @ApiOperation(value = "eoStepActualHisQuery")
    @PostMapping(value = {"/limit-eo/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualHis>> eoStepActualHisQuery(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody String eoId) {
        ResponseData<List<MtEoStepActualHis>> responseData = new ResponseData<List<MtEoStepActualHis>>();
        try {
            responseData.setRows(this.repository.eoStepActualHisQuery(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepActualHisByEventQuery")
    @PostMapping(value = {"/limit-event/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepActualHis>> eoStepActualHisByEventQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eventId) {
        ResponseData<List<MtEoStepActualHis>> responseData = new ResponseData<List<MtEoStepActualHis>>();
        try {
            responseData.setRows(this.repository.eoStepActualHisByEventQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
