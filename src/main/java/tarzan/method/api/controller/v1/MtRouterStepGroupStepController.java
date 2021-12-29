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
import tarzan.method.api.dto.MtRouterStepGroupStepDTO2;
import tarzan.method.app.service.MtRouterStepGroupStepService;
import tarzan.method.domain.entity.MtRouterStepGroupStep;
import tarzan.method.domain.repository.MtRouterStepGroupStepRepository;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO1;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO2;

/**
 * 工艺路线步骤组行步骤 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@RestController("mtRouterStepGroupStepController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-step-group-step")
@Api(tags = "MtRouterStepGroupStep")
public class MtRouterStepGroupStepController extends BaseController {
    @Autowired
    private MtRouterStepGroupStepService service;
    @Autowired
    private MtRouterStepGroupStepRepository repository;

    @ApiOperation(value = "UI删除工艺路线步骤组行步骤")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeRouterStepGroupStepForUi(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtRouterStepGroupStepDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.removeRouterStepGroupStepForUi(tenantId, dto.getRouterStepGroupStepId(), dto.getRouterStepId());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "groupStepLimitStepQuery")
    @PostMapping(value = {"/limit-step/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStepGroupStep>> groupStepLimitStepQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String routerStepId) {
        ResponseData<List<MtRouterStepGroupStep>> responseData = new ResponseData<List<MtRouterStepGroupStep>>();
        try {
            responseData.setRows(this.repository.groupStepLimitStepQuery(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "stepLimitStepGroupGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtRouterStepGroupStepVO> stepLimitStepGroupGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerStepId) {
        ResponseData<MtRouterStepGroupStepVO> responseData = new ResponseData<MtRouterStepGroupStepVO>();
        try {
            responseData.setRows(this.repository.stepLimitStepGroupGet(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
    @ApiOperation(value = "routerStepGroupStepBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStepGroupStepVO>> routerStepGroupStepBatchGet(
            @PathVariable("organizationId") Long tenantId, @RequestBody List<String> routerStepIds) {
        ResponseData<List<MtRouterStepGroupStepVO>> responseData = new ResponseData<List<MtRouterStepGroupStepVO>>();
        try {
            responseData.setRows(this.repository.routerStepGroupStepBatchGet(tenantId, routerStepIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerStepLimitGroupStepQuery")
    @PostMapping(value = {"/group/step/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> routerStepLimitGroupStepQuery(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody String routerStepId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.routerStepLimitGroupStepQuery(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerStepGroupStepAttrPropertyUpdate")
    @PostMapping(value = {"/group/step/attr/property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> routerStepGroupStepAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody MtRouterStepGroupStepVO1 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.routerStepGroupStepAttrPropertyUpdate(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "groupSteplimitStepBatchQuery")
    @PostMapping(value = {"/limit-step/batch/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStepGroupStepVO2>> groupSteplimitStepBatchQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody List<String> routerStepIds) {
        ResponseData<List<MtRouterStepGroupStepVO2>> responseData = new ResponseData<List<MtRouterStepGroupStepVO2>>();
        try {
            responseData.setRows(this.repository.groupSteplimitStepBatchQuery(tenantId, routerStepIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
