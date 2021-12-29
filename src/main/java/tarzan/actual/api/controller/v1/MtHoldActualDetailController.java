package tarzan.actual.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.actual.domain.repository.MtHoldActualDetailRepository;
import tarzan.actual.domain.vo.*;

/**
 * 保留实绩明细 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtHoldActualDetailController.v1")
@RequestMapping("/v1/{organizationId}/mt-hold-actual-detail")
@Api(tags = "MtHoldActualDetail")
public class MtHoldActualDetailController extends BaseController {

    @Autowired
    private MtHoldActualDetailRepository repository;

    @ApiOperation(value = "holdDetailPropertyGet")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtHoldActualDetailVO> holdDetailPropertyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtHoldActualDetailVO dto) {
        ResponseData<MtHoldActualDetailVO> result = new ResponseData<MtHoldActualDetailVO>();
        try {
            result.setRows(repository.holdDetailPropertyGet(tenantId, dto));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "objectLimitHoldingDetailGet")
    @PostMapping(value = {"/holding-detail/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> objectLimitHoldingDetailGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtHoldActualDetailVO2 dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.objectLimitHoldingDetailGet(tenantId, dto));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "propertyLimitHoldDetailQuery")
    @PostMapping(value = {"/limit-property/holding-detail"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtHoldActualDetailVO3>> propertyLimitHoldDetailQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtHoldActualDetailVO dto) {
        ResponseData<List<MtHoldActualDetailVO3>> result = new ResponseData<List<MtHoldActualDetailVO3>>();
        try {
            result.setRows(repository.propertyLimitHoldDetailQuery(tenantId, dto));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;

    }

    @ApiOperation(value = "holdRelease")
    @PostMapping(value = {"/hold/release"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> holdRelease(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtHoldActualDetailVO4 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            // 释放保留明细
            repository.holdRelease(tenantId, dto);
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;

    }

    @ApiOperation(value = "holdIsExpiredVerify")
    @PostMapping(value = {"/hold/expired/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> holdIsExpiredVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtHoldActualDetailVO3 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            // 校验保留是否到期
            repository.holdIsExpiredVerify(tenantId, dto);
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;

    }

    @ApiOperation(value = "holdExpiredReleaseTimeGet")
    @PostMapping(value = {"/hold/expired/release/time"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtHoldActualDetailVO5> holdExpiredReleaseTimeGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtHoldActualDetailVO3 dto) {
        ResponseData<MtHoldActualDetailVO5> result = new ResponseData<MtHoldActualDetailVO5>();
        try {
            // 获取保留到期释放时间
            result.setRows(repository.holdExpiredReleaseTimeGet(tenantId, dto));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "objectLimitAllHoldQuery")
    @PostMapping(value = {"/limit-object/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtHoldActualDetailVO3>> objectLimitAllHoldQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtHoldActualDetailVO2 dto) {
        ResponseData<List<MtHoldActualDetailVO3>> result = new ResponseData<List<MtHoldActualDetailVO3>>();
        try {
            // 根据对象获取保留实绩明细,包含“HOLD_TYPE”为“FUTURE”类型的数据
            result.setRows(repository.objectLimitAllHoldQuery(tenantId, dto));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;

    }

    @ApiOperation(value = "objectLimitFutureHoldDetailQuery")
    @PostMapping(value = {"/limit-object/hold-future/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtHoldActualDetailVO3>> objectLimitFutureHoldDetailQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtHoldActualDetailVO2 dto) {
        ResponseData<List<MtHoldActualDetailVO3>> result = new ResponseData<List<MtHoldActualDetailVO3>>();
        try {
            // 根据对象获取将来保留明细，筛选头“HOLD_TYPE”为“FUTURE”类型的数据
            result.setRows(repository.objectLimitFutureHoldDetailQuery(tenantId, dto));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "futureHoldVerify")
    @PostMapping(value = {"/future/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> futureHoldVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtHoldActualDetailVO6 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            // 校验步骤是否为将来保留步骤
            repository.futureHoldVerify(tenantId, dto);
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "expiredReleaseTimeLimitHoldRelease")
    @PostMapping(value = {"/limit-expired/hold-release"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtHoldActualDetailVO7>> expiredReleaseTimeLimitHoldRelease(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtHoldActualDetailVO8 dto) {
        ResponseData<List<MtHoldActualDetailVO7>> result = new ResponseData<List<MtHoldActualDetailVO7>>();
        try {
            // 根据保留到期释放时间释放保留
            result.setRows(repository.expiredReleaseTimeLimitHoldRelease(tenantId, dto));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "eoStepFutureHoldCancel")
    @PostMapping(value = {"/step/future/hold-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepFutureHoldCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtHoldActualDetailVO9 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            // 执行作业步骤将来取消
            repository.eoStepFutureHoldCancel(tenantId, dto);
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "eoStepFutureHold")
    @PostMapping(value = {"/step/future/hold"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepFutureHold(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtHoldActualDetailVO11 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            // 执行作业步骤将来保留
            repository.eoStepFutureHold(tenantId, dto);
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "eoStepHold")
    @PostMapping(value = {"/step/hold"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepHold(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtHoldActualDetailVO10 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            // 执行作业步骤保留
            repository.eoStepHold(tenantId, dto);
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "eoStepHoldCancel")
    @PostMapping(value = {"/step/hold-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStepHoldCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtHoldActualDetailVO10 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            // 执行作业步骤取消
            repository.eoStepHoldCancel(tenantId, dto);
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }
}
