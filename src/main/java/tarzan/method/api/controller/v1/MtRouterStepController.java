package tarzan.method.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.method.api.dto.MtRouterStepDTO2;
import tarzan.method.api.dto.MtRouterStepDTO3;
import tarzan.method.api.dto.MtRouterStepDTO4;
import tarzan.method.app.service.MtRouterStepService;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.method.domain.vo.*;

/**
 * 工艺路线步骤 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@RestController("mtRouterStepController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-step")
@Api(tags = "MtRouterStep")
public class MtRouterStepController extends BaseController {

    @Autowired
    private MtRouterStepService service;

    @Autowired
    private MtRouterStepRepository repository;

    @ApiOperation(value = "UI查询工艺路线下步骤列表")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStepDTO4>> queryRouterStepListForUi(@PathVariable("organizationId") Long tenantId,
                                                                         String routerId, @ApiIgnore @SortDefault(value = MtRouterStep.FIELD_SEQUENCE,
            direction = Sort.Direction.ASC) PageRequest pageRequest) {
        ResponseData<List<MtRouterStepDTO4>> responseData = new ResponseData<List<MtRouterStepDTO4>>();
        try {
            responseData.setRows(this.service.queryRouterStepListForUi(tenantId, routerId, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存工艺路线步骤")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveRouterStepForUi(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody MtRouterStepDTO4 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.service.saveRouterStepForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存工艺路线步骤")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeRouterStepForUi(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody String routerStepId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.service.removeRouterStepForUi(tenantId, routerStepId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerStepGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtRouterStep> routerStepGet(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody String routerStepId) {
        ResponseData<MtRouterStep> responseData = new ResponseData<MtRouterStep>();
        try {
            responseData.setRows(this.repository.routerStepGet(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerStepBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStep>> routerStepBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> routerStepIds) {
        ResponseData<List<MtRouterStep>> responseData = new ResponseData<List<MtRouterStep>>();
        try {
            responseData.setRows(this.repository.routerStepBatchGet(tenantId, routerStepIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationStepQuery")
    @PostMapping(value = {"/limit-operation/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> operationStepQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtRouterStepDTO2 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.operationStepQuery(tenantId, dto.getOperationId(),
                            dto.getRouterId()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "parentStepQuery")
    @PostMapping(value = {"/parent"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtRouterStepVO6> parentStepQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<MtRouterStepVO6> responseData = new ResponseData<MtRouterStepVO6>();
        try {
            responseData.setRows(this.repository.parentStepQuery(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "subStepQuery")
    @PostMapping(value = {"/sub"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtRouterStepVO10> subStepQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<MtRouterStepVO10> responseData = new ResponseData<MtRouterStepVO10>();
        try {
            responseData.setRows(this.repository.subStepQuery(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stepNameLimitRouterStepGet")
    @PostMapping(value = {"/limit-step-name/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> stepNameLimitRouterStepGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtRouterStepDTO3 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.stepNameLimitRouterStepGet(tenantId, dto.getRouterId(),
                            dto.getStepName()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerStepQueueDecisionTypeGet")
    @PostMapping(value = {"/queue-decision-type"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> routerStepQueueDecisionTypeGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.routerStepQueueDecisionTypeGet(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerStepListQuery")
    @PostMapping(value = {"/limit-router/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStepVO5>> routerStepListQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerId) {
        ResponseData<List<MtRouterStepVO5>> responseData = new ResponseData<List<MtRouterStepVO5>>();
        try {
            responseData.setRows(this.repository.routerStepListQuery(tenantId, routerId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerEntryRouterStepGet")
    @PostMapping(value = {"/limit-router/entry"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> routerEntryRouterStepGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.routerEntryRouterStepGet(tenantId, routerId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerEntryRouterStepBatchGet")
    @PostMapping(value = {"/limit-router/batch/entry"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStepVO17>> routerEntryRouterStepBatchGet(
            @PathVariable("organizationId") Long tenantId, @RequestBody List<String> routerIds) {
        ResponseData<List<MtRouterStepVO17>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.routerEntryRouterStepBatchGet(tenantId, routerIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bottomStepOperationQuery")
    @PostMapping(value = {"/bottom-step-operation"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterVO>> bottomStepOperationQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<List<MtRouterVO>> responseData = new ResponseData<List<MtRouterVO>>();
        try {
            responseData.setRows(this.repository.bottomStepOperationQuery(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerComponentQtyCalculate")
    @PostMapping(value = {"/router-component-qty/calculation"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStepVO1>> routerComponentQtyCalculate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtRouterStepVO2 condition) {
        ResponseData<List<MtRouterStepVO1>> responseData = new ResponseData<List<MtRouterStepVO1>>();
        try {
            responseData.setRows(this.repository.routerComponentQtyCalculate(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "propertyLimitRouterStepPropertyQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStepVO12>> propertyLimitRouterStepPropertyQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtRouterStepVO11 dto) {
        ResponseData<List<MtRouterStepVO12>> result = new ResponseData<List<MtRouterStepVO12>>();
        try {
            result.setRows(repository.propertyLimitRouterStepPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "eoLimitUnStartRouterStepQuery")
    @PostMapping(value = {"/limit-eo/unstart/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStepVO14>> eoLimitUnStartRouterStepQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtRouterStepVO13 dto) {
        ResponseData<List<MtRouterStepVO14>> result = new ResponseData<List<MtRouterStepVO14>>();
        try {
            result.setRows(repository.eoLimitUnStartRouterStepQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "routerStepAttrPropertyUpdate")
    @PostMapping(value = {"/router/step/attr/property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> routerStepAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtExtendVO10 vo) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.routerStepAttrPropertyUpdate(tenantId, vo);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "routerStepListBatchQuery")
    @PostMapping(value = {"/limit-router/batch/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStepVO15>> routerStepListBatchQuery(@PathVariable("organizationId") Long tenantId,
                                                                         @RequestBody List<String> routerIds) {
        ResponseData<List<MtRouterStepVO15>> responseData = new ResponseData<List<MtRouterStepVO15>>();
        try {
            responseData.setRows(this.repository.routerStepListBatchQuery(tenantId, routerIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
