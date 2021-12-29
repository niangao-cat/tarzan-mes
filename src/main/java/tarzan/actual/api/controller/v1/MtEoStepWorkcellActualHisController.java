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
import tarzan.actual.domain.entity.MtEoStepWorkcellActualHis;
import tarzan.actual.domain.repository.MtEoStepWorkcellActualHisRepository;
import tarzan.actual.domain.vo.MtEoStepWorkcellActualHisVO;
import tarzan.actual.domain.vo.MtEoStepWorkcellActualVO10;
import tarzan.actual.domain.vo.MtEoStepWorkcellActualVO9;

/**
 * 执行作业-工艺路线步骤执行明细历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtEoStepWorkcellActualHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-step-workcell-actual-his")
@Api(tags = "MtEoStepWorkcellActualHis")
public class MtEoStepWorkcellActualHisController extends BaseController {

    @Autowired
    private MtEoStepWorkcellActualHisRepository repository;

    @ApiOperation(value = "eoWkcActualHisByEventQuery")
    @PostMapping(value = {"/limit-event/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWorkcellActualHis>> eoWkcActualHisByEventQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eventId) {

        ResponseData<List<MtEoStepWorkcellActualHis>> responseData =
                        new ResponseData<List<MtEoStepWorkcellActualHis>>();
        try {
            responseData.setRows(this.repository.eoWkcActualHisByEventQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcActualHisQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWorkcellActualHis>> eoWkcActualHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoStepWorkcellActualHisVO condition) {

        ResponseData<List<MtEoStepWorkcellActualHis>> responseData =
                        new ResponseData<List<MtEoStepWorkcellActualHis>>();
        try {
            responseData.setRows(this.repository.eoWkcActualHisQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAndOperationLimitWkcStepActualHisQuery")
    @PostMapping(value = {"/actual/his/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWorkcellActualVO10>> eoAndOperationLimitWkcStepActualHisQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtEoStepWorkcellActualVO9 condition) {

        ResponseData<List<MtEoStepWorkcellActualVO10>> responseData =
                new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoAndOperationLimitWkcStepActualHisQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
