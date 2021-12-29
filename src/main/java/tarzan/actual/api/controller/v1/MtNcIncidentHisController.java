package tarzan.actual.api.controller.v1;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
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
import tarzan.actual.api.dto.MtNcIncidentDTO;
import tarzan.actual.domain.entity.MtNcIncidentHis;
import tarzan.actual.domain.repository.MtNcIncidentHisRepository;

/**
 * 不良事故历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:48
 */
@RestController("mtNcIncidentHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-nc-incident-his")
@Api(tags = "MtNcIncidentHis")
public class MtNcIncidentHisController extends BaseController {

    @Autowired
    private MtNcIncidentHisRepository repository;

    @ApiOperation(value = "ncIncidentNumLimitNcIncidentHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcIncidentHis>> ncIncidentNumLimitNcIncidentHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtNcIncidentDTO dto) {
        ResponseData<List<MtNcIncidentHis>> responseData = new ResponseData<List<MtNcIncidentHis>>();
        try {
            List<MtNcIncidentHis> mtNcIncidentHis = this.repository.ncIncidentNumLimitNcIncidentHisQuery(tenantId,
                            dto.getSiteId(), dto.getIncidentNumber());
            if (CollectionUtils.isNotEmpty(mtNcIncidentHis)) {
                responseData.setRows(mtNcIncidentHis);
            }
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitNcIncidentHisQuery")
    @PostMapping(value = {"/limit-event/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcIncidentHis>> eventLimitNcIncidentHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eventId) {
        ResponseData<List<MtNcIncidentHis>> responseData = new ResponseData<List<MtNcIncidentHis>>();
        try {
            List<MtNcIncidentHis> mtNcIncidentHis = this.repository.eventLimitNcIncidentHisQuery(tenantId, eventId);
            if (CollectionUtils.isNotEmpty(mtNcIncidentHis)) {
                responseData.setRows(mtNcIncidentHis);
            }

        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
