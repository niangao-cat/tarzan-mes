package tarzan.dispatch.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.dispatch.domain.entity.MtEoDispatchHistory;
import tarzan.dispatch.domain.repository.MtEoDispatchHistoryRepository;
import tarzan.dispatch.domain.vo.MtEoDispatchHistoryVO1;

/**
 * 调度历史表，记录历史发布的调度结果和版本 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:58
 */
@RestController("mtEoDispatchHistoryController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-dispatch-history")
@Api(tags = "MtEoDispatchHistory")
public class MtEoDispatchHistoryController extends BaseController {

    @Autowired
    private MtEoDispatchHistoryRepository repository;

    @ApiOperation(value = "wkcLimitDispatchedHisQuery")
    @PostMapping(value = "/property/dispatched/limit-wkc", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchHistory>> wkcLimitDispatchedHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchHistoryVO1 vo) {
        ResponseData<List<MtEoDispatchHistory>> responseData = new ResponseData<List<MtEoDispatchHistory>>();
        try {
            responseData.setRows(this.repository.wkcLimitDispatchedHisQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
