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
import tarzan.method.domain.entity.MtBomHis;
import tarzan.method.domain.repository.MtBomHisRepository;
import tarzan.method.domain.vo.MtBomHisVO1;
import tarzan.method.domain.vo.MtBomHisVO2;
import tarzan.method.domain.vo.MtBomHisVO3;

/**
 * 装配清单头历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@RestController("mtBomHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-bom-his")
@Api(tags = "MtBomHis")
public class MtBomHisController extends BaseController {

    @Autowired
    private MtBomHisRepository repository;

    @ApiOperation(value = "bomAllHisCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> bomAllHisCreate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomHisVO1 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.bomAllHisCreate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomHis>> bomHisQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomId) {
        ResponseData<List<MtBomHis>> responseData = new ResponseData<List<MtBomHis>>();
        try {
            responseData.setRows(this.repository.bomHisQuery(tenantId, bomId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomAllHisQuery")
    @PostMapping(value = {"/all"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomHisVO2>> bomAllHisQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomId) {
        ResponseData<List<MtBomHisVO2>> responseData = new ResponseData<List<MtBomHisVO2>>();
        try {
            responseData.setRows(this.repository.bomAllHisQuery(tenantId, bomId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitBomAllHisQuery")
    @PostMapping(value = {"/limit-event"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomHisVO3>> eventLimitBomAllHisQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eventId) {
        ResponseData<List<MtBomHisVO3>> responseData = new ResponseData<List<MtBomHisVO3>>();
        try {
            responseData.setRows(this.repository.eventLimitBomAllHisQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitBomHisBatchQuery")
    @PostMapping(value = {"/limit-events"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomHis>> eventLimitBomHisBatchQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> eventIds) {
        ResponseData<List<MtBomHis>> responseData = new ResponseData<List<MtBomHis>>();
        try {
            responseData.setRows(this.repository.eventLimitBomHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
