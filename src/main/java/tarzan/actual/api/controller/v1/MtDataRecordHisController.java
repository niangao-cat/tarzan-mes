package tarzan.actual.api.controller.v1;

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
import tarzan.actual.domain.repository.MtDataRecordHisRepository;
import tarzan.actual.domain.vo.MtNcRecordHisVO;

/**
 * 数据收集实绩历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:01:00
 */
@RestController("mtDataRecordHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-data-record-his")
@Api(tags = "MtDataRecordHis")
public class MtDataRecordHisController extends BaseController {
    @Autowired
    private MtDataRecordHisRepository mtDataRecordHisRepository;

    @ApiOperation(value = "dataRecordLatestHisGet")
    @PostMapping(value = {"/data/record/latest/his"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcRecordHisVO> dataRecordLatestHisGet(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody String dataRecordId) {
        ResponseData<MtNcRecordHisVO> responseData = new ResponseData<MtNcRecordHisVO>();
        try {
            responseData.setRows(this.mtDataRecordHisRepository.dataRecordLatestHisGet(tenantId, dataRecordId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
