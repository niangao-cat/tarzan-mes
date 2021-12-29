package tarzan.order.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.order.domain.repository.MtEoBatchChangeHistoryRepository;
import tarzan.order.domain.vo.MtEoBatchChangeHistoryVO;
import tarzan.order.domain.vo.MtEoBatchChangeHistoryVO2;
import tarzan.order.domain.vo.MtEoBatchChangeHistoryVO3;
import tarzan.order.domain.vo.MtEoBatchChangeHistoryVO4;
import tarzan.order.domain.vo.MtEoBatchChangeHistoryVO5;
import tarzan.order.domain.vo.MtEoBatchChangeHistoryVO6;

/**
 * 执行作业变更记录 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@RestController("mtEoBatchChangeHistoryController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-batch-change-history")
@Api(tags = "MtEoBatchChangeHistory")
public class MtEoBatchChangeHistoryController extends BaseController {
    @Autowired
    private MtEoBatchChangeHistoryRepository repository;

    @ApiOperation(value = "relTargetEoQuery")
    @PostMapping(value = {"/rel/target"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoBatchChangeHistoryVO>> relTargetEoQuery(@PathVariable("organizationId") Long tenantId,
                                                                         @RequestBody String eoId) {
        ResponseData<List<MtEoBatchChangeHistoryVO>> responseData = new ResponseData<List<MtEoBatchChangeHistoryVO>>();
        try {
            responseData.setRows(this.repository.relTargetEoQuery(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "relSourceEoQuery")
    @PostMapping(value = {"/rel/source"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoBatchChangeHistoryVO2>> relSourceEoQuery(@PathVariable("organizationId") Long tenantId,
                                                                          @RequestBody String eoId) {
        ResponseData<List<MtEoBatchChangeHistoryVO2>> responseData =
                        new ResponseData<List<MtEoBatchChangeHistoryVO2>>();
        try {
            responseData.setRows(this.repository.relSourceEoQuery(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRelQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoBatchChangeHistoryVO3>> eoRelQuery(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody String eoId) {
        ResponseData<List<MtEoBatchChangeHistoryVO3>> responseData =
                        new ResponseData<List<MtEoBatchChangeHistoryVO3>>();
        try {
            responseData.setRows(this.repository.eoRelQuery(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRelUpdate")
    @PostMapping(value = {"/rel/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoRelUpdate(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody MtEoBatchChangeHistoryVO4 dto,
                                            @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoRelUpdate(tenantId, dto, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "relSourceEoTreeQuery")
    @PostMapping(value = {"/rel/source/tree"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoBatchChangeHistoryVO6>> relSourceEoTreeQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<List<MtEoBatchChangeHistoryVO6>> responseData =
                        new ResponseData<List<MtEoBatchChangeHistoryVO6>>();
        try {
            responseData.setRows(this.repository.relSourceEoTreeQuery(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "relTargetEoTreeQuery")
    @PostMapping(value = {"/rel/target/tree"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoBatchChangeHistoryVO5>> relTargetEoTreeQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<List<MtEoBatchChangeHistoryVO5>> responseData =
                        new ResponseData<List<MtEoBatchChangeHistoryVO5>>();
        try {
            responseData.setRows(this.repository.relTargetEoTreeQuery(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
