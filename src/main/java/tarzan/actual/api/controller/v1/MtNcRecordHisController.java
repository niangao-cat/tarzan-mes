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
import tarzan.actual.domain.entity.MtNcRecordHis;
import tarzan.actual.domain.repository.MtNcRecordHisRepository;

/**
 * 不良代码记录历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:55
 */
@RestController("mtNcRecordHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-nc-record-his")
@Api(tags = "MtNcRecordHis")
public class MtNcRecordHisController extends BaseController {

    @Autowired
    private MtNcRecordHisRepository repository;

    @ApiOperation(value = "ncRecordLimitHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcRecordHis>> ncRecordLimitHisQuery(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody String ncRecordId) {
        ResponseData<List<MtNcRecordHis>> responseData = new ResponseData<List<MtNcRecordHis>>();
        try {
            responseData.setRows(this.repository.ncRecordLimitHisQuery(tenantId, ncRecordId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
