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
import tarzan.method.domain.entity.MtBomReferencePointHis;
import tarzan.method.domain.repository.MtBomReferencePointHisRepository;

/**
 * 装配清单行参考点关系历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@RestController("mtBomReferencePointHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-bom-reference-point-his")
@Api(tags = "MtBomReferencePointHis")
public class MtBomReferencePointHisController extends BaseController {

    @Autowired
    private MtBomReferencePointHisRepository repository;

    @ApiOperation(value = "bomReferencePointHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomReferencePointHis>> bomReferencePointHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String bomReferencePointId) {
        ResponseData<List<MtBomReferencePointHis>> responseData = new ResponseData<List<MtBomReferencePointHis>>();
        try {
            responseData.setRows(
                            repository.bomReferencePointHisQuery(tenantId, bomReferencePointId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitBomReferencePointHisBatchQuery")
    @PostMapping(value = {"/property/list/limit-events"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomReferencePointHis>> eventLimitBomReferencePointHisBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtBomReferencePointHis>> responseData = new ResponseData<List<MtBomReferencePointHis>>();
        try {
            responseData.setRows(this.repository
                            .eventLimitBomReferencePointHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
